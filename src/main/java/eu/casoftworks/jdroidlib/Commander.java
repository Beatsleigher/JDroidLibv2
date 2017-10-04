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

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * <b >This is NOT a public class!</b>
 * @author Simon Cahill
 */
class Commander implements IExecutioner {
    
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
    public void executeCommandNoOutput(ICommand command) throws IOException, IllegalDeviceStateException {
        executeCommandReturnProcess(command);
    }

    @Override
    public String executeCommandReturnOutput(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
    }

    @Override
    public int executeCommandReturnExitCode(ICommand command) throws IOException, IllegalDeviceStateException {
        return 0;
    }

    @Override
    public ITuple2<Integer, String> executeCommand(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
    }

    @Override
    public Process executeCommandReturnProcess(ICommand command) throws IOException, IllegalDeviceStateException {

        /**
         * Synchronize (lock) on to the LOCK object
         * to prevent multiple command being executed simultaneously.
         * This is to prevent threads accessing the same resources.
         *
         * Should result in a more stable library.
         * I can't stress the SHOULD enough!
         */
        synchronized (LOCK) {
            List<String> args = getProcArgs(command);

            return new ProcessBuilder()
                    .command(args)
                    .directory(new File(((ResourceManager)resMan).getJDroidLibTmpDirectory()))
                    .inheritIO()
                    .redirectErrorStream(true)
                    .start();
        }
    }

    @Override
    public Future executeCommandNoOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
    }

    @Override
    public Future<String> executeCommandReturnOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
    }

    @Override
    public Future<Integer> executeCommandReturnExitCodeAsync(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
    }

    @Override
    public Future<ITuple2<Integer, String>> executeCommandAsync(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
    }

    @Override
    public Future<Process> executeCommandReturnProcessAsync(ICommand command) throws IOException, IllegalDeviceStateException {
        return null;
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

    /**
     * It's kind of obvious, if you ask me, but I get the output from
     * any process object that's not dead, essentially.
     * @param proc The process to retrieve output from.
     * @return The process's output.
     * @throws IOException If an I/O error occurs.
     */
    private String getProcessOutput(Process proc) throws IOException {
        StringBuilder sBuilder = new StringBuilder();

        if (proc == null)
            throw new IllegalArgumentException("Process must not be null!");
        if (!proc.isAlive())
            return sBuilder.toString(); // It might have died already without output.

        // Use try-with-resources to automatically close the reader once it's no longer
        // required. Less work for me!
        try (BufferedReader biStreamReader = new BufferedReader(new InputStreamReader(proc.getInputStream()))) {
            String line = null;

            /*
             * Continue loop while the process is still alive
             * and the output is not null.
             * If process dies, attempting to read from the stream will only cause exceptions
             * and confuse users and developers alike.
             */
            while (proc.isAlive() && (line = biStreamReader.readLine()) != null) {
                sBuilder.append(line).append('\n');
            }
        }
        return sBuilder.toString();
    }

}
