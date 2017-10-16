package eu.casoftworks.jdroidlib.device.pm;

import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.device.fs.*;
import eu.casoftworks.jdroidlib.interfaces.*;

/**
 * Represents a package installed on a given {@link Device}
 */
public class Package {

    private final String name;
    private final IFile associatedFile;
    private final String installer;

    public Package(String pkgName, IFile assocFile) {
        this(pkgName, assocFile, null);
    }

    public Package(String pkgName, Device device, String assocFile) {
        this(pkgName, new AndroidFile(device, assocFile), null);
    }

    public Package(String pkgName, IFile assocFile, String installer) {
        name = pkgName;
        associatedFile = assocFile;
        this.installer = installer;
    }

    public Package(String pkgName, Device device, String assocFile, String installer) {
        this(pkgName, new AndroidFile(device, assocFile), installer);
    }

    /**
     * Gets the .apk file associated with this package (on-device)
     * @return The {@link IFile} pointing to the location of this package.
     */
    public IFile getAssociatedFile() {
        return associatedFile;
    }

    /**
     * Gets the name of this package.
     * @return The name of this package.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the installer for this package.
     * @return The installer.
     */
    public String getInstaller() {
        return installer;
    }
}
