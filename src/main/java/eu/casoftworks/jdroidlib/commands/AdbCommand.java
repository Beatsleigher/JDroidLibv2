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
package eu.casoftworks.jdroidlib.commands;

import eu.casoftworks.jdroidlib.device.Device;
import eu.casoftworks.jdroidlib.enums.CommandType;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.interfaces.ICommand;
import eu.casoftworks.jdroidlib.util.*;

/**
 * Represents an ADB (Android Debug Bridge) command to be executed
 * via the ADB server.
 * @author Simon Cahill
 */
public class AdbCommand extends Command {
    
    AdbCommand(Device device, CommandType cmdType, String cmdTag, String... cmdArguments) {
        super(device, cmdType, cmdTag, cmdArguments);
    }
    
    public static class Factory {
        
        CommandType cmdType = CommandType.AdbCommand;
        String cmdTag = null;
        String[] cmdArgs = null;
        Device device = null;
        
        public Factory setCommandTag(String cmdTag) {
            this.cmdTag = cmdTag;
            return this;
        }
        
        public Factory setCommandArgs(String... args) {
            this.cmdArgs = args;
            return this;
        }
        
        public Factory setDevice(Device device) {
            this.device = device;
            return this;
        }
        
        public AdbCommand create() { return new AdbCommand(device, cmdType, cmdTag, cmdArgs); }
        
    }

    /**
     * Gets the command for starting the ADB server.
     * @return An instance of {@link ICommand}
     */
    public static AdbCommand getStartServerCommand() {
        return new Factory().setCommandTag("start-server").create();
    }

    /**
     * Gets the command for stopping the ADB server.
     * @return An instance of {@link ICommand}
     */
    public static AdbCommand getStopServerCommand() {
        return new Factory().setCommandTag("stop-server").create();
    }

    /**
     * Gets a command for listing devices connected to the host computer.
     * @return An instance of {@link ICommand}
     */
    public static AdbCommand getDevicesCommand() {
        return new Factory().setCommandTag("devices").create();
    }

    /**
     * Gets a command for listing the devices connected to the host.
     * @return An instance of {@link ICommand}
     */
    public static AdbCommand getDevicesLongCommand() {
        return new Factory().setCommandTag("devices").setCommandArgs("-l").create();
    }

    /**
     * Gets a command for connecting to a specific device via TCP/IP.
     * @param ip4Address The IP address to connect to.
     * @return An instance of {@link ICommand}
     */
    public static AdbCommand getConnectDeviceCommand(Ip4Address ip4Address) {
        return new Factory().setCommandTag("connect").setCommandArgs(ip4Address.toString()).create();
    }

    /**
     * Gets a command for disconnecting from a specific device connected via TCP/IP.
     * @param device The device to disconnect from.
     * @return An instance of {@link ICommand}
     * @throws DeviceNotConnectedViaTcpIpException If the device in question is not connected to the
     * host via TCP/IP
     */
    public static AdbCommand getDisconnectDeviceCommand(Device device) throws DeviceNotConnectedViaTcpIpException {
        if (!device.isConnectedViaTcpIp())
            throw new DeviceNotConnectedViaTcpIpException(String.format("The device %s is not connected via TCP/IP!", device.getSerialNumber()));
        return new Factory().setCommandTag("disconnect").setCommandArgs(device.getIpAddress().toString()).create();
    }

    /**
     * Gets a command for disconnecting from all devices connected to the host via TCP/IP.
     * @return An instance of {@link ICommand}
     */
    public static AdbCommand getDisconnectAllDevicesCommand() {
        return new Factory().setCommandTag("disconnect").create();
    }

}
