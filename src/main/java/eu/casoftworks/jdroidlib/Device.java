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
package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.enums.*;
import eu.casoftworks.jdroidlib.util.Ip4Address;

/**
 * Represents a physical device.
 * @author Simon Cahill
 */
public class Device {
    
    private final AndroidVersion version;
    private final String serialNo;
    private final Ip4Address ipAddr;
    private final boolean connectedViaTcpIp;
    private DeviceState state;
    
    /**
     * Constructor for devices connected via USB and/or emulated devices.
     * @param serialNo The device's serial number (at least the one seen by ADB)
     * @param version The Android version currently installed on the device.
     * @param state The current state of the device.
     */
    Device(String serialNo, AndroidVersion version, DeviceState state) {
        this.serialNo = serialNo;
        this.version = version;
        this.state = state;
        this.ipAddr = null;
        this.connectedViaTcpIp = false;
    }
    
    /**
     * Constructor for devices connected via TCP/IP.
     * @param ipAddr The IP address of the device in string form.
     * @param port The port of the device. If set to 0 (zero), {@link Ip4Address#ADB_DEFAULT_PORT} will be used!
     * @param version The Android version installed on the device.
     * @param state The device's current state.
     */
    Device (String ipAddr, short port, AndroidVersion version, DeviceState state) {
        this.ipAddr = Ip4Address.fromAddress(ipAddr, port);
        connectedViaTcpIp = true;
        this.version = version;
        this.state = state;
        this.serialNo = null;
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
    //</editor-fold>
    
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
    
}
