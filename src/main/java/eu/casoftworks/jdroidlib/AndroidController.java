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

import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.enums.*;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.interfaces.*;
import eu.casoftworks.jdroidlib.util.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class AndroidController implements IExecutioner {

    public static final String LONG_LIST_LINE_REGEX = "(([A-z0-9.:\\-_]{1,})([\\s]+)?){5}";

    private final IResourceManager resourceManager;
    private final IExecutioner commander;

    private List<Device> deviceList;

    //<editor-fold desc="Singleton and Init" defaultstate="collapsed" >
    private static AndroidController controller;

    /**
     * Gets the running instance of this class.
     * If this class has not yet been initialized, a
     * new instance will be created and then returned.
     * @return The instance of AndroidController
     */
    public static AndroidController getController() throws InterruptedException, ExecutionException, PlatformNotSupportedException, IOException {
        return controller != null ? controller : (controller = new AndroidController());
    }

    private AndroidController() throws InterruptedException, ExecutionException, PlatformNotSupportedException, IOException {
        resourceManager = ResourceManager.getInstance();
        commander = new Commander(resourceManager);
        deviceList = new ArrayList<>();
    }
    //</editor-fold>

    /**
     * Starts the ADB server.
     * @throws IllegalDeviceStateException
     * @throws InterruptedException
     * @throws IOException
     */
    public void startServer() throws IllegalDeviceStateException, InterruptedException, IOException {
        commander.executeCommandNoOutputAsync(AdbCommand.getStartServerCommand());
    }

    public void stopServer() throws IllegalDeviceStateException, InterruptedException, IOException {
        commander.executeCommandNoOutputAsync(AdbCommand.getStopServerCommand());
    }

    /**
     * Attempts to connect the first found device from the list of devices.
     * @return The first (connected) device found.
     */
    public Device getDevice() {

        try {
            refreshDevices();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (deviceList.isEmpty())
            return null;

        return deviceList.get(0);
    }

    private void refreshDevices() throws IllegalDeviceStateException, InterruptedException, IOException, ExecutionException {
        deviceList.clear();
        try (BufferedReader reader = new BufferedReader(new StringReader(commander.executeCommandReturnOutputAsync(AdbCommand.getDevicesLongCommand()).get()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("list") || line.isEmpty() || line.startsWith("*"))
                    continue;

                // Split string in to segments
                ITuple5<String, String, String, String, String> parsedLine = parseDeviceOutputLine(line);

                // Determine if USB or TCP/IP device
                if (Ip4Address.isIp4Address(parsedLine.getItem1())) {
                    Device.getDevice(Ip4Address.fromAddress(parsedLine.getItem1()), parsedLine.getItem3(), parsedLine.getItem4(), DeviceState.valueOf(parsedLine.getItem2()));
                }
            }
        }
    }

    private ITuple5<String, String, String, String, String> parseDeviceOutputLine(String line) {

        if (!line.matches(LONG_LIST_LINE_REGEX))
            throw new IllegalArgumentException("Line must be from LONG list output!");

        String serialOrIp = null, state = null, product = null, model = null, device = null;
        String[] parts = line.split("(\\s+)");

        serialOrIp = parts[0];
        state = parts[1];
        product = parts[2].split(":")[1];
        model = parts[3].split(":")[1];
        device = parts[4].split(":")[1];

        return new Tuple5<>(serialOrIp, state, product, model, device);
    }

    //<editor-fold desc="IExecutioner Implementation" defaultstate="collapsed"
    @Override
    public void executeCommandNoOutput(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        commander.executeCommandNoOutput(command);
    }

    @Override
    public String executeCommandReturnOutput(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandReturnOutput(command);
    }

    @Override
    public int executeCommandReturnExitCode(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandReturnExitCode(command);
    }

    @Override
    public ITuple2<Integer, String> executeCommand(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommand(command);
    }

    @Override
    public Process executeCommandReturnProcess(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandReturnProcess(command);
    }

    @Override
    public Future executeCommandNoOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandNoOutputAsync(command);
    }

    @Override
    public Future<String> executeCommandReturnOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandReturnOutputAsync(command);
    }

    @Override
    public Future<Integer> executeCommandReturnExitCodeAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandReturnExitCodeAsync(command);
    }

    @Override
    public Future<ITuple2<Integer, String>> executeCommandAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandAsync(command);
    }

    @Override
    public Future<Process> executeCommandReturnProcessAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException {
        return commander.executeCommandReturnProcessAsync(command);
    }

    @Override
    public long getTimeout() {
        return commander.getTimeout();
    }

    @Override
    public void setTimeout(long timeout) {
        commander.setTimeout(timeout);
    }

    @Override
    public TimeUnit getTimeUnit() {
        return commander.getTimeUnit();
    }

    @Override
    public void setTimeUnit(TimeUnit timeUnit) {
        commander.setTimeUnit(timeUnit);
    }
    //</editor-fold>

}
