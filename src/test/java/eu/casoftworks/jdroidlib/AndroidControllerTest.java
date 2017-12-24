package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.exception.*;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class AndroidControllerTest {

    @Test
    void getController() throws InterruptedException, ExecutionException, IllegalDeviceStateException, PlatformNotSupportedException, IOException {
        assertNotEquals(AndroidController.getController(), null);
    }

    @Test
    void getControllerOrNull() throws InterruptedException, ExecutionException, IllegalDeviceStateException, PlatformNotSupportedException, IOException {
        assertSame(AndroidController.getController(), AndroidController.getControllerOrNull());
    }

    @Test
    void startServer() throws InterruptedException, ExecutionException, IllegalDeviceStateException, PlatformNotSupportedException, IOException {
        try (AndroidController controller = AndroidController.getController()) {
            controller.startServer();
        }
    }

    @Test
    void stopServer() throws InterruptedException, ExecutionException, IllegalDeviceStateException, PlatformNotSupportedException, IOException {
        try (AndroidController controller = AndroidController.getController()) {
            controller.stopServer();
        }
    }

    @Test
    void getDevice() throws InterruptedException, ExecutionException, IllegalDeviceStateException, PlatformNotSupportedException, IOException {
        try (AndroidController controller = AndroidController.getController()) {
            // Detect if one or more devices are connected
            // THE TEST WILL FAIL IF OTHERWISE!

        }
    }

    @Test
    void getDevice1() {
    }

    @Test
    void getDevice2() {
    }

    @Test
    void connectToDevice() {
    }

    @Test
    void disconnectFromDevice() {
    }

    @Test
    void disconnectFromAllDevices() {
    }

    @Test
    void refreshDevices() {
    }

    @Test
    void getDevices() {
    }

    @Test
    void executeCommandNoOutput() {
    }

    @Test
    void executeCommandReturnOutput() {
    }

    @Test
    void executeCommandReturnExitCode() {
    }

    @Test
    void executeCommand() {
    }

    @Test
    void executeCommandReturnProcess() {
    }

    @Test
    void executeCommandNoOutputAsync() {
    }

    @Test
    void executeCommandReturnOutputAsync() {
    }

    @Test
    void executeCommandReturnExitCodeAsync() {
    }

    @Test
    void executeCommandAsync() {
    }

    @Test
    void executeCommandReturnProcessAsync() {
    }

    @Test
    void getTimeout() {
    }

    @Test
    void setTimeout() {
    }

    @Test
    void getTimeUnit() {
    }

    @Test
    void setTimeUnit() {
    }

    @Test
    void close() {
    }

}