package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.concurrent.*;

/**
 * Represents a SuperUser installation on a given {@link Device}.
 *
 * @see Device
 */
public class SuperUser {

    public static final String VERSION_ARG = "--version";
    public static final String NOT_FOUND = "not found";
    public static final String NOT_AVAILABLE = "n/a";

    private final Device parentDevice;

    private boolean installed;
    private String version;

    SuperUser(Device device) {
        parentDevice = device;
    }

    private void refreshData() {
        try {
            String output = AndroidController.getController().executeCommandReturnOutput(new AdbShellCommand.Factory()
                    .setDevice(parentDevice)
                    .setCommandTag("su")
                    .setCommandArgs(VERSION_ARG)
                    .create()
            );
            installed = !output.contains(NOT_FOUND);
            version = installed ? output : NOT_AVAILABLE;
        } catch (IOException | IllegalDeviceStateException | InterruptedException | ExecutionException | PlatformNotSupportedException e) {
            e.printStackTrace();
            // Just to be on the safe side
            installed = false;
            version = NOT_AVAILABLE;
        }
    }

    /**
     * Gets a value indicating whether superuser is installed on a given {@link Device}.
     * @return {@code true} if superuser is installed, {@code false} otherwise.
     *
     * @see Device
     */
    public boolean isInstalled() { refreshData(); return installed; }

    /**
     * Gets the version of superuser that is installed on a given {@link Device}.
     * @return The version of superuser, or {@link SuperUser#NOT_AVAILABLE} if superuser is not installed.
     *
     * @see Device
     * @see SuperUser#NOT_AVAILABLE
     */
    public String getVersion() { refreshData(); return version; }

}
