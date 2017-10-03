/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.commands.AdbCommand;
import eu.casoftworks.jdroidlib.enums.DeviceState;
import eu.casoftworks.jdroidlib.exception.IllegalDeviceStateException;
import eu.casoftworks.jdroidlib.interfaces.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * <b >This is NOT a public class!</b>
 * @author Simon Cahill
 */
class Commander {
    
    private final IResourceManager resMan;
    
    Commander(IResourceManager resMan) {
        this.resMan = resMan;
    }
    
    /**
     * Takes an instance of ICommand, determines what type of command it is and 
     * passes it through to the correct methods.
     * This method does not return command output!
     * @param command The command to execute.
     */
    void executeCommandNoOutput(ICommand command) {
        switch (command.getCommandType()) {
            case AdbCommand:
            case AdbShellCommand:
                executeAdbCommandNoOutput(command);
                return;
            case FastbootCommand:
                executeFastbootCommandNoOutput(command);
                return;
            default:
                throw new UnsupportedOperationException("These command types are not yet supported!");
        }
    }
    
    /**
     * Takes an instance of ICommand, determines what type of command it is and
     * passes it to the correct method.
     * Command execution is asynchronous
     * This method does not return command output!
     * @param command The command to execute.
     * @return The execution task
     */
    Future executeCommandAsyncNoOutput(ICommand command) {
        return new FutureTask(() -> {
            switch (command.getCommandType()) {
                case AdbCommand:
                case AdbShellCommand:
                    executeAdbCommandNoOutput(command);
                case FastbootCommand:
                    executeFastbootCommandNoOutput(command);
                default:
                    throw new UnsupportedOperationException("These command types are not yet supported!");
            }
        });
    }
    
    /**
     * Takes an instance of ICommand, determines what type of command it is and
     * passes it on to the correct method.
     * This method returns command output!
     * @param command The command to execute.
     * @return The command output.
     */
    String executeCommand(ICommand command) {
        switch (command.getCommandType()) {
            case AdbCommand:
            case AdbShellCommand:
                return executeAdbCommand(command);
            case FastbootCommand:
                return executeFastbootCommand(command);
            default:
                throw new UnsupportedOperationException("These command types are not yet supported!");
        }
    }
    
    /**
     * Takes an instance of ICommand, determines what type of command it is and
     * passes it on to the correct method.
     * Command execution is asynchronous!
     * This method returns command output!
     * @param command The command to execute!
     * @return The execution task containing the command's output.
     */
    FutureTask<String> executeCommandAsync(ICommand command) {
        return new FutureTask<String>(() -> {
            switch (command.getCommandType()) {
                case AdbCommand:
                case AdbShellCommand:
                    return executeAdbCommandNoOutput(command);
                case FastbootCommand:
                    return executeFastbootCommandNoOutput(command);
                default:
                    throw new UnsupportedOperationException("These command types are not yet supported!");
            }
        });
    }
    
    void executeAdbCommandNoOutput(ICommand protoCommand) {
        AdbCommand command = (AdbCommand)protoCommand;
        List<String> args = new ArrayList<>();
        
        
        
    }
    
    String executeAdbCommand(ICommand command) {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }
    
    void executeFastbootCommandNoOutput(ICommand command) {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }
    
    String executeFastbootCommand(ICommand command) {
        throw new UnsupportedOperationException("This method is not yet implemented.");
    }
    
    /**
     * Prepares all the arguments as required to execute the process.
     * @param command The command to execute.
     * @return A list of arguments to be executed on either ADB or fastboot servers.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalDeviceStateException If the targeted device (if any) is in an illegal state.
     */
    private List<String> getProcArgs(ICommand command) throws IOException, IllegalDeviceStateException {
        List<String> args = new ArrayList<>();
        
        switch (command.getCommandType()) {
            case AdbCommand:
                args.add(resMan.getAdb().getAbsolutePath());
                args.addAll(getDeviceArgs(command.getDevice()));
                break;
            case AdbShellCommand:
                args.add(resMan.getAdb().getAbsolutePath());
                args.addAll(getDeviceArgs(command.getDevice()));
                args.add("shell");
                break;
            case FastbootCommand:
                args.add(resMan.getFastboot().getAbsolutePath());
                args.addAll(getDeviceArgs(command.getDevice()));
                break;
            default:
                throw new UnsupportedOperationException("This funcationality is not yet implemented in JDroidLib!");
        }
        
        args.add(command.getCommandTag());
        args.addAll(command.getCommandArgumentsAsList());
        
        return args;
    }
    
    /**
     * Get arguments specific to targeting specific devices.
     * @param device The device to target the command towards.
     * @return A list of arguments.
     * @throws IllegalDeviceStateException If the device in question is in an illegal mode for command execution.
     */
    private List<String> getDeviceArgs(Device device) throws IllegalDeviceStateException {
        List<String> args = new ArrayList<>();
        
        if (device == null)
            return args; // No device attached to command; execute service-wide.
        
        //<editor-fold desc="Error detection and prevention" defaultstate="collapsed" >
        switch (device.getDeviceState()) {
            case Offline:
                throw new IllegalDeviceStateException(
                        String.format(
                                "The device (%s) is currently offline! Commands cannot be targeted at offline devices!",
                                device.isConnectedViaTcpIp() ? device.getIpAddress().toString() : device.getSerialNumber()
                        )
                );
            case Unauthorized:
                throw new IllegalDeviceStateException(
                        String.format(
                                "The device (%s) is currently unauthorized! The host must be authozied by the device before commaands can be issued!",
                                device.isConnectedViaTcpIp() ? device.getIpAddress().toString() : device.getSerialNumber()
                        )
                );
            case Unknown:
                throw new IllegalDeviceStateException(
                        String.format(
                                "The device (%s) is currently in an unknown state! Has the device malfunctioned?",
                                device.isConnectedViaTcpIp() ? device.getIpAddress().toString() : device.getSerialNumber()
                        )
                );
        }
        //</editor-fold>
            
        args.add("-s"); // Notify server to target -specific device.
        if (device.isConnectedViaTcpIp()) {
            // Doubt it's worth checking if the device is in fastboot mode.
            args.add(device.getIpAddress().toString());
        } 
        
        args.add(device.getSerialNumber());
        
        return args;
    }
    
}
