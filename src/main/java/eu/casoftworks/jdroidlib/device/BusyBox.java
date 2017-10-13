package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;

import java.util.logging.*;

/**
 * Represents BusyBox installations on a {@link Device}
 */
public class BusyBox {

    public static final String VERSION_ARG = "--version";
    public static final String NOT_FOUND = "not found";
    public static final String NOT_AVAILABLE = "n/a";

    private final Device parentDevice;

    private boolean installed;
    private String version;

    BusyBox(Device device) {
        parentDevice = device;
        init();
    }

    private void init() {
        try {
            String cmdOutput = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                new AdbShellCommand.Factory()
                    .setDevice(parentDevice)
                    .setCommandTag("busybox")
                    .setCommandArgs(VERSION_ARG)
                    .create()
            );
            installed = !cmdOutput.contains(NOT_FOUND);
            if (!installed)
                version = NOT_AVAILABLE;
            else
                version = cmdOutput;
        } catch (Exception ex) {
            Logger.getLogger(BusyBox.class.getName()).log(Level.SEVERE, String.format("Couldn't fetch busybox info!\n%s", ex));
            ex.printStackTrace();
        }
    }

    /**
     * Gets a value indicating whether busybox is installed on
     * the parent {@link Device} or not.
     * @return {@code true} if busybox is installed. {@code false} otherwise.
     */
    public boolean hasBusybox() { return installed; }

    /**
     * Gets the version of the busybox installation.
     * @return The version of the busybox installation, or {@link BusyBox#NOT_AVAILABLE} if not installed.
     */
    public String getVersion() { return version; }

    /**
     * Refreshed the data stored in this object.
     */
    public void refresh() { init(); }

}
