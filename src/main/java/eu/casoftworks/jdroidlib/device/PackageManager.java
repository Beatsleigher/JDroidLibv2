package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.device.pm.*;
import eu.casoftworks.jdroidlib.device.pm.Package;
import eu.casoftworks.jdroidlib.enums.*;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.*;

/**
 * Device class; represents a device's package manager.
 * Acts as an interface to the device's package manager and allows listing of
 * users, packages, permissions, etc.
 * Packages can be installed and removed via this class.
 */
public class PackageManager {

    public static final String PKG_LIST_BEGIN = "package:";
    public static final String INSTALLER_DEFINITION = "installer=";

    private final Device parentDevice;
    private final AndroidController adbController;

    PackageManager(Device device) {
        parentDevice = device;
        adbController = AndroidController.getControllerOrNull();
    }

    /**
     * Install a package on to a {@link Device}.
     * @param appFile The .apk to install. See: {@link ApkFile}
     * @param flags The flags to apply. See: {@link InstallFlag}
     * @throws DeviceException
     */
    public void installPackage(ApkFile appFile, InstallFlag... flags) throws DeviceException {
        AdbCommand command = new AdbCommand.Factory()
                .setDevice(parentDevice)
                .setCommandTag("install")
                .setCommandArgs(appFile.getAbsolutePath(), InstallFlag.getArguments(flags))
                .create();
        try {
            adbController.executeCommandNoOutput(command);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new DeviceException(e);
        }
    }

    /**
     * List packages on the {@link Device}
     * @param filters The filters to apply. See: {@link PackageFilter}
     * @return The packages installed on the {@link Device}
     * @throws DeviceException
     */
    public List<Package> listPackages(PackageFilter... filters) throws DeviceException {
        List<String> args = new ArrayList<>();
        args.add("list");
        args.add("packages");
        args.add("-f");
        args.addAll(PackageFilter.getArgs(filters));
        List<Package> packages = new ArrayList<>();

        AdbShellCommand command = new AdbShellCommand.Factory()
                .setDevice(parentDevice)
                .setCommandTag("pm")
                .setCommandArgs((String[])args.toArray())
                .create();

        try {
            try (BufferedReader reader = new BufferedReader(new StringReader(adbController.executeCommandReturnOutput(command)))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.startsWith(PKG_LIST_BEGIN)) continue;

                    line = line.replace(PKG_LIST_BEGIN, "");

                    String installer = null;
                    String file;
                    String pkg;

                    String[] split = line.split(INSTALLER_DEFINITION);

                    if (split.length == 2) {
                        installer = split[1];
                    }

                    split = split[0].trim().split("=");

                    file = split[1];
                    pkg = split[0];

                    packages.add(new Package(pkg, parentDevice, file, installer));

                }
            }
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
            throw new DeviceException(ex);
        }
        return packages;
    }

    /**
     * Removes an installed package from a {@link Device}.
     * @param pkg The {@link Package} to remove.
     * @throws DeviceException
     */
    public void removePackage(Package pkg) throws DeviceException {

        try {
            adbController.executeCommandNoOutput(
                    new AdbShellCommand.Factory()
                        .setDevice(parentDevice)
                        .setCommandTag("pm")
                        .setCommandArgs("uninstall", pkg.getName())
                        .create()
            );
        } catch (InterruptedException | IOException ex) {
            ex.printStackTrace();
            throw new DeviceException(ex);
        }

    }

}
