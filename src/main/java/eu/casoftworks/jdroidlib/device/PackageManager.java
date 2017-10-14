package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.util.*;

/**
 * Device class; represents a device's package manager.
 * Acts as an interface to the device's package manager and allows listing of
 * users, packages, permissions, etc.
 * Packages can be installed and removed via this class.
 */
public class PackageManager {

    private final Device parentDevice;
    private final AndroidController adbController;

    PackageManager(Device device) {
        parentDevice = device;
        adbController = AndroidController.getControllerOrNull();
    }

    public void installPackage(ApkFile appFile) {

    }

}
