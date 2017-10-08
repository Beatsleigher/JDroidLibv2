package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.enums.*;
import eu.casoftworks.jdroidlib.util.*;

import java.util.*;

/**
 * Defines a filesystem entry on a given device.
 */
public interface IFileSystemEntry {

    /**
     * Gets the {@link Device} the file is hosted on.
     * @return The hosting {@link Device}.
     */
    Device getHostDevice();

    /**
     * The permissions for the current file system entry.
     * @return The file system entry's permissions.
     */
    PermissionSet getPermissions();

    /**
     * Gets the full path of the file system entry.
     *
     * @return E.g. /usr/local/bin/myfile.txt
     */
    String getFullPath();

    /**
     * Gets a value indicating whether the file is hidden on the device's file system.
     * @return {@code true} if the file/directory is hidden. {@code false} otherwise.
     */
    boolean isHidden();

    /**
     * Gets a value indicating whether the file/directory exists.
     * @return {@code true} if the file/directory exists. {@code false} otherwise.
     */
    boolean exists();

    /**
     * Gets the owner of the file/directory.
     * @return The owner.
     */
    String getOwner();

    /**
     * Gets the group the file/directory is associated with.
     * @return The group.
     */
    String getGroup();

    /**
     * Gets the size (in bytes) used by the file/directory on the file system.
     * @return The sire of the file/directory in bytes.
     */
    long getSize();

    /**
     * Gets a value indicating whether an instance of this interface represents a file or not.
     * @param fsEntry The file system entry to check.
     * @return {@code true} if the file system entry is a file. {@code false} otherwise.
     */
    static boolean isFile(IFileSystemEntry fsEntry) {
        return fsEntry instanceof IFile;
    }

}
