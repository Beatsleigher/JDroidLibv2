package eu.casoftworks.jdroidlib.enums;

/**
 * Flags for use during application installation on a given {@link eu.casoftworks.jdroidlib.device.Device}.
 */
public enum InstallFlag {

    /**
     * Forward-lock application.
     * (Disallow future downgrades)
     */
    ForwardLock("l"),

    /**
     * Replace an existing application.
     */
    ReplaceExisting("r"),

    /**
     * Allow test packages.
     */
    AllowTestPackages("t"),

    /**
     * Install application on device SD card.
     */
    InstallOnSd("s"),

    /**
     * Allow version code downgrade.
     * (Debuggable packages ONLY)
     */
    AllowDowngrade("d"),

    /**
     * Partial application install.
     * (Only applicable when installing multiple application)
     */
    PartialApplicationInstall("p"),

    /**
     * Grant application all runtime permissions.
     */
    GrantAllPermissions("g");

    String arg;

    InstallFlag(String arg) {
        this.arg = arg;
    }

    public String getArg() { return arg; }

    /**
     * Get all the arguments from each provided flag.
     * @param flags The flags to use.
     * @return The arguments ready for command execution.
     */
    public static String getArguments(InstallFlag... flags) {
        StringBuilder sBuilder = new StringBuilder("-");
        for (InstallFlag flag : flags)
            sBuilder.append(flag.getArg());

        return sBuilder.toString();
    }

}
