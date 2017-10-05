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
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.interfaces.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

public class AndroidController {

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
        if (deviceList.isEmpty())
            return null;

        return deviceList.get(0);
    }

}
