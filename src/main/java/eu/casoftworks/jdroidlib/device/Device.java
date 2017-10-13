/*
 * Copyright (c) 2017, Simon Cahill
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.enums.*;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.interfaces.*;
import eu.casoftworks.jdroidlib.util.Ip4Address;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * Represents a physical device.
 * @author Simon Cahill
 */
public class Device implements IDevice {

    //<editor-fold desc="Static Members" defaultstate="collapsed" >

    private static List<Device> encounteredDevices;

    static {
        encounteredDevices = new ArrayList<>();
    }

    public static Device getDevice(String serialNo) {
        for (Device device : encounteredDevices) {
            if (device.serialNo.contentEquals(serialNo))
                return device;
        }
        return null;
    }

    public static Device getDevice(String serialNo, String productString, String modelString, DeviceState state) {
        Device device = getDevice(serialNo);
        if (device == null)
            return new Device(serialNo, productString, modelString, state);

        if (device.state != state)
            device.state = state;

        return device;
    }

    public static Device getDevice(Ip4Address inetAddress) {
        for (Device device : encounteredDevices) {
            if (device.ipAddr == inetAddress)
                return device;
        }
        return null;
    }

    public static Device getDevice(Ip4Address inetAddress, String productString, String modelString, DeviceState state) {
        Device device = getDevice(inetAddress);
        if (device == null)
            return new Device(inetAddress, productString, modelString, state);

        if (device.state != state)
            device.state = state;

        return device;
    }
    //</editor-fold>

    private final String serialNo;
    private final Ip4Address ipAddr;
    private final boolean connectedViaTcpIp;
    private final String productString;
    private final String modelString;
    private DeviceState state;

    // Effectively final
    private AndroidVersion version;
    private double sdkVersion;
    private SuperUser su;
    private FileSystem fileSystem;
    private BusyBox busyBox;
    private Battery battery;
    private BuildProp buildProp;

    /**
     * Constructor for devices connected via USB and/or emulated devices.
     * @param serialNo The device's serial number (at least the one seen by ADB)
     * @param productString The device's product string
     * @param modelString The device's model string
     * @param state The current state of the device.
     */
    Device(String serialNo, String productString, String modelString, DeviceState state) {
        this.serialNo = serialNo;
        this.productString = productString;
        this.modelString = modelString;
        this.state = state;
        this.ipAddr = null;
        this.connectedViaTcpIp = false;
        init();
    }
    
    /**
     * Constructor for devices connected via TCP/IP.
     * @param ipAddr The IP address of the device in string form.
     * @param port The port of the device. If set to 0 (zero), {@link Ip4Address#ADB_DEFAULT_PORT} will be used!
     * @param productString The device's product string.
     * @param modelString The device's model string.
     * @param state The device's current state.
     */
    Device (String ipAddr, short port, String productString, String modelString, DeviceState state) {
        this(Ip4Address.fromAddress(ipAddr, port), productString, modelString, state);
    }

    Device (Ip4Address ipAddr, String productString, String modelString, DeviceState state) {
        this.ipAddr = ipAddr;
        connectedViaTcpIp = true;
        this.productString = productString;
        this.modelString = modelString;
        this.state = state;
        this.serialNo = null;
        init();
    }

    private void init() {
        version = setAndroidVersion();
        sdkVersion = setSdkVersion();
        su = new SuperUser(this);
        fileSystem = new FileSystem(this);
        busyBox = new BusyBox(this);
        battery = new Battery(this);
        buildProp = new BuildProp(this);
    }

    //<editor-fold desc="Getter methods for final variables" defaultstate="collapsed" >
    /**
     * Gets the version of Android currently installed on the device.
     * @see AndroidVersion
     * @return The version of Android.
     */
    public AndroidVersion getVersion() { return version; }
    
    /**
     * Gets the device's serial number (as seen by ADB).
     * This method will return {@code null} if the device is connected via TCP/IP!
     * @return The device's serial number as seen by ADB.
     */
    public String getSerialNumber() { return serialNo; }
    
    /**
     * Gets the device's IP address (and port).
     * This method will return {@code null} if the device is connected via TCP/IP!
     * @return The device's IPv4 address and port.
     */
    public Ip4Address getIpAddress() { return ipAddr; }
    
    /**
     * Gets a value indicating whether this device is connected to the host via
     * TCP/IP or not.
     * @return {@code true} if the device is connected via TCP/IP. {@code false} otherwise.
     */
    public boolean isConnectedViaTcpIp() { return connectedViaTcpIp; }

    /**
     * Gets the device's product string.
     * @return The product ID.
     */
    public String getProductString() { return  productString; }

    /**
     * Gets the device's model string.
     * @return The model ID.
     */
    public String getModelString() { return  modelString; }
    //</editor-fold>

    //<editor-fold desc="Purely device-related stuff (device info)" defaultstate="collapsed" >
    /**
     * {@inheritDoc}
     */
    @Override
    public String getID() {
        if (connectedViaTcpIp) {
            return ipAddr.toString();
        } else return serialNo;
    }

    /**
     * Sets the device's Android version.
     * @return The version of Android installed on the device.
     *
     * @see AndroidVersion
     */
    private AndroidVersion setAndroidVersion() {
        try {
            return AndroidVersion.fromVersionString(AndroidController.getController().executeCommandReturnOutput(AdbShellCommand.getRetrieveAndroidVersionCommand(this)));
        } catch (Exception e) {
            // General catch; if anything goes wrong print the stack trace
            // and return an unknown Android version.
            e.printStackTrace();
        }
        return AndroidVersion.Unknown;
    }

    /**
     * Sets the device's SDK version.
     * @return
     */
    private double setSdkVersion() {
        try {
            return Double.parseDouble(
                    AndroidController.getController().executeCommandReturnOutput(AdbShellCommand.getRetrieveAndroidSdkVersionCommand(this))
            );
        } catch (Exception e) {
            // Same as with the Android version
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Gets the device's current state.
     * @see DeviceState
     * @return The device's current state. E.g. UNAUTHORIZED
     */
    public DeviceState getDeviceState() { return state; }
    
    /**
     * Sets the device's state.
     * This method is package-private for good reason!
     * @param newState The device's new state (e.g. ONLINE or RECOVERY)
     */
    void setDeviceState(DeviceState newState) { this.state = newState; }

    /**
     * Gets the version of Android installed on the device
     * represented by this object.
     * @return An {@link AndroidVersion} value.
     */
    public AndroidVersion getAndroidVersion() { return version; }

    /**
     * Gets the SDK version installed on the device represented by
     * this object.
     * @return
     */
    public double getSdkVersion() { return sdkVersion; }
    //</editor-fold>

    /**
     * Reboots this {@link Device} into a specified operating mode.
     * @param mode The mode to reboot to.
     * @throws DeviceException If an error occurs.
     *
     * @see RebootMode
     */
    public void rebootDevice(RebootMode mode) throws DeviceException {
        try {
            AndroidController.getControllerOrNull().executeCommandNoOutput(
                new AdbCommand.Factory()
                    .setDevice(this)
                    .setCommandTag("reboot")
                    .setCommandArgs(mode.getMode())
                    .create()
            );
        } catch (IOException | IllegalDeviceStateException | InterruptedException e) {
            e.printStackTrace();
            throw new DeviceException(e);
        }
    }

    /**
     * Reboots this {@link Device} to Android.
     * @throws DeviceException If an error occurs.
     */
    public void rebootDevice() throws DeviceException {
        rebootDevice(RebootMode.Device);
    }

    /**
     * Gets the {@link SuperUser} object associated with this {@link Device}.
     * @return An instance of {@link SuperUser}.
     *
     * @see SuperUser
     */
    public SuperUser getSuperUser() { return su; }

    /**
     * Shortcut for determining whether a device is rooted or not.
     * Calls {@link SuperUser#isInstalled()}
     * @return {@code true} if the {@link Device} is rooted, {@code false} otherwise.
     *
     * @see SuperUser
     * @see SuperUser#isInstalled()
     */
    public boolean hasRoot() { return getSuperUser().isInstalled(); }

    /**
     * Gets an instance of {@link FileSystem} representing the file system on this device.
     * @return An instance of {@link FileSystem}.
     *
     * @see FileSystem
     */
    public FileSystem getFileSystem() { return fileSystem; }

    /**
     * Gets an object referencing the {@link Device}'s
     * busybox installation.
     * @return An instance of {@link BusyBox}
     */
    public BusyBox getBusyBox() {
        return busyBox;
    }

    /**
     * Gets the device's battery.
     * @return The {@link Device}'s {@link Battery}
     */
    public Battery getBattery() { return battery; }
}
