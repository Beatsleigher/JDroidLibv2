package eu.casoftworks.jdroidlib.enums;

import java.util.*;

/**
 * Represents different filters for listing {@link eu.casoftworks.jdroidlib.device.pm.Package}s on a given device {@link eu.casoftworks.jdroidlib.device.Device}.
 */
public enum PackageFilter {

    /* Options:
      -f: see their associated file
      -d: filter to only show disabled packages
      -e: filter to only show enabled packages
      -s: filter to only show system packages
      -3: filter to only show third party packages
      -i: see the installer for the packages
      -u: also include uninstalled packages*/

    /**
     * Show only disabled packages.
     */
    OnlyDisabled("-d"),

    /**
     * Show only enabled packages.
     */
    OnlyEnabled("-e"),

    /**
     * Show only system packages.
     */
    OnlySystem("-s"),

    /**
     * Show only third-party packages.
     */
    OnlyThirdParty("-3"),

    /**
     * Also fetch the installer for the packages.
     */
    WithInstaller("-i"),

    /**
     * Include uninstalled packages.
     */
    IncludeUninstalled("-u");

    String arg;

    PackageFilter(String arg) {
        this.arg = arg;
    }

    /**
     * Gets the arg represented by this enum entry.
     * @return
     */
    public String getArg() { return this.arg; }

    /**
     * Get a list of args corresponding the list of {@link PackageFilter} passed.
     * @param filters The filters.
     * @return Arguments for command execution.
     */
    public static List<String> getArgs(PackageFilter... filters) {
        List<String> args = new ArrayList<>();
        for (PackageFilter filter : filters)
            args.add(filter.getArg());
        return args;
    }

}
