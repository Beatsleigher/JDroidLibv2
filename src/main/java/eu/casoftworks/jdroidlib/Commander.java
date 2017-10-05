/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.exception.IllegalDeviceStateException;
import eu.casoftworks.jdroidlib.interfaces.*;
import eu.casoftworks.jdroidlib.util.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/**
 * <b >This is NOT a public class!</b>
 * @author Simon Cahill
 */
class Commander implements IExecutioner {
    
    private final IResourceManager resMan;
    private long timeout = 120; // Default is two (2) minutes.
    private TimeUnit timeoutTimeUnit = TimeUnit.SECONDS;

    /**
     * Default and only constructor available for this class.
     * @param resMan
     */
    Commander(IResourceManager resMan) {
        this.resMan = resMan;
    }

    /**
     * {@inheritDoc}
     * @param command The command to executed.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public void executeCommandNoOutput(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        executeCommandReturnProcess(command).waitFor(timeout, timeoutTimeUnit);
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The command's output.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public String executeCommandReturnOutput(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return executeCommand(command).getItem2();
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The process's exit code.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public int executeCommandReturnExitCode(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return executeCommand(command).getItem1();
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return Both the process's exit code and the command's output.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public ITuple2<Integer, String> executeCommand(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        Process proc = executeCommandReturnProcess(command);
        String output = getProcessOutput(proc);
        int exitCode = getExitCode(proc);
        return new Tuple2<Integer, String>(exitCode, output);
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The actual process being executed.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public Process executeCommandReturnProcess(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {

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

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The task being executed.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public Future executeCommandNoOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return new FutureTask(() -> { executeCommandNoOutput(command); return null; });
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The task being executed and the command's output.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public Future<String> executeCommandReturnOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return new FutureTask<>(() -> executeCommandReturnOutput(command));
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The task being executed and the process's exit code.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public Future<Integer> executeCommandReturnExitCodeAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return new FutureTask<>(() -> executeCommandReturnExitCode(command));
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The task being executed along with both the process's exit code and the command's output.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public Future<ITuple2<Integer, String>> executeCommandAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return new FutureTask<>(() -> executeCommand(command));
    }

    /**
     * {@inheritDoc}
     * @param command The command to execute.
     * @return The task being executed along with the actual executing process.
     * @throws IOException If an I/O exception occurs
     * @throws IllegalDeviceStateException If the {@link Device} is in an illegal state while attempting to
     * execute a command on it.
     * @throws InterruptedException If the thread is interrupted during process execution.
     */
    @Override
    public Future<Process> executeCommandReturnProcessAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return new FutureTask<>(() -> executeCommandReturnProcess(command));
    }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public long getTimeout() { return timeout; }

    /**
     * {@inheritDoc}
     * @param timeout The time to wait before forcfully terminating a process.
     */
    @Override
    public void setTimeout(long timeout) { this.timeout = timeout; }

    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public TimeUnit getTimeUnit() { return timeoutTimeUnit; }

    /**
     * {@inheritDoc}
     * @param timeUnit The unit of time.
     *
     */
    @Override
    public void setTimeUnit(TimeUnit timeUnit) { this.timeoutTimeUnit = timeUnit; }

    /**
     * Prepares all the arguments as required to execute the process.
     * @param command The command to execute.
     * @return A list of arguments to be executed on either ADB or fastboot servers.
     * @throws IOException If an I/O error occurs.
     * @throws IllegalDeviceStateException If the targeted device (if any) is in an illegal state.
     */
    private List<String> getProcArgs(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
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

    /**
     * Waits for the passed process to end, if it hasn't already
     * and returns the process's exit code.
     * @param proc The process to get the exit code for.
     * @return The process's exit code.
     * @throws InterruptedException If the thread was interrupted during waiting.
     *
     * @see Process
     *
     */
    private int getExitCode(Process proc) throws InterruptedException {
        if (!proc.isAlive()) {
            return proc.exitValue();
        }

        if (!proc.waitFor(timeout, timeoutTimeUnit)) {
            proc.destroy();
        }

        return getExitCode(proc);
    }

}
