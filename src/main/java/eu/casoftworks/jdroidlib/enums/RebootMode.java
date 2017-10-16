package eu.casoftworks.jdroidlib.enums;

import eu.casoftworks.jdroidlib.device.*;

public enum RebootMode {

    /**
     * Reboots the {@link Device} to Android.
     */
    Device(""),

    /**
     * Reboots the {@link Device} to recovery mode.
     */
    Recovery("recovery"),

    /**
     * Reboots the {@link Device} to its bootloader (fastboot, Odin, etc)
     */
    Bootloader("bootloader");

    String mode;

    RebootMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the mode to reboot the {@link Device} to.
     * @return The reboot mode.
     */
    public String getMode() { return mode; }

}
