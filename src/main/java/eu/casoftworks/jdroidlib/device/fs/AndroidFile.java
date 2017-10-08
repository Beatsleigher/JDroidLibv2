package eu.casoftworks.jdroidlib.device.fs;

import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.interfaces.*;
import eu.casoftworks.jdroidlib.util.*;

import java.io.*;

/**
 * Represents a file stored on an Android {@link Device}.
 * AndroidFiles may be instantiated by the library, but can also be used extensively in any applications using the library.
 */
public class AndroidFile implements IFile {

    public static final String LINUX_PATH_SEPARATOR = "/";
    public static final String FILE_EXTENSION_PRECHAR = ".";

    // All these fields should be effectively final!
    private String name;
    private String extension;
    private IDirectory parentDir;
    private String fullName;

    /**
     * Object constructor.
     * @param fullPath
     */
    public AndroidFile(String fullPath) {

        // Do some error checking
        if (fullPath == null || fullPath.isEmpty())
            throw new IllegalArgumentException("fullName must not be null!");

        String[] iGotDaSplits = fullPath.split(LINUX_PATH_SEPARATOR);

        fullName = iGotDaSplits[iGotDaSplits.length];

        if (name.contains(FILE_EXTENSION_PRECHAR)) {
            String[] tmp = name.split(FILE_EXTENSION_PRECHAR);
            name = tmp[0]; // First instance in array is name of the file
            extension = tmp[tmp.length]; // Account for there being periods in the filename; Only show last extension.
        } else {
            name = fullName; // No extensions found; name is effectively full name.
            extension = null;
        }

        // TODO: Add parent directory
    }

    /**
     * Gets the name of the file without the file extension.
     *
     * @return The name of the file.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Gets the file's extension ONLY.
     *
     * @return The file's extension. e.g. .sh
     */
    @Override
    public String getExtension() {
        return extension;
    }

    /**
     * Gets the fully qualified name of the file (name.ext).
     *
     * @return The full name of the file.
     */
    @Override
    public String getFullName() {
        return fullName;
    }

    /**
     * Gets a value indicating whether this file has one or more extensions.
     *
     * @return {@code true} if the file has one or more extensions. {@code false} otherwise.
     */
    @Override
    public boolean hasExtension() {
        return extension != null;
    }

    /**
     * Gets the parent directory of the file.
     *
     * @return The directory the file is stored in.
     */
    @Override
    public IDirectory getParentDirectory() {
        return parentDir;
    }

    /**
     * Attempts to pull the file from the given {@link Device} to a library-specific location on the host.
     *
     * @return The storage location of the pulled file.
     */
    @Override
    public File pullFile() {
        return pullFile(IResourceManager.combinePaths(IResourceManager.getJDroidLibTmpDirectory(), getFullName()));
    }

    /**
     * Attempts to pull the file from the {@link Device} to a given location on the host.
     *
     * @param location The location to pull the file to.
     *
     * @return The new location of the pulled file. (location/file if file is not specified in location)
     */
    @Override
    public File pullFile(File location) {
        return null;
    }

    /**
     * The same as {@link IFile#pullFile(File)}.
     *
     * @param location The location to pull the file to.
     *
     * @return The new location of the pulled file on the host.
     */
    @Override
    public File pullFile(String location) {
        return null;
    }

    /**
     * Attempts to get the file's contents.
     * The file may be pulled to the host in order to retrieve the bytes!
     *
     * @return The file's raw contents.
     */
    @Override
    public byte[] getContents() {
        return new byte[0];
    }

    /**
     * Attempts to get string contents from a file.
     *
     * @return The file's contents.
     */
    @Override
    public String getContentsAsString() {
        return null;
    }

    /**
     * Attempts to create a file on the device's file system.
     *
     * @return {@code true} if file creation was successful. {@code false} otherwise.
     */
    @Override
    public boolean touch() {
        return false;
    }

    /**
     * Gets the {@link Device} the file is hosted on.
     *
     * @return The hosting {@link Device}.
     */
    @Override
    public Device getHostDevice() {
        return null;
    }

    /**
     * The permissions for the current file system entry.
     *
     * @return The file system entry's permissions.
     */
    @Override
    public PermissionSet getPermissions() {
        return null;
    }

    /**
     * Gets the full path of the file system entry.
     *
     * @return E.g. /usr/local/bin/myfile.txt
     */
    @Override
    public String getFullPath() {
        return null;
    }

    /**
     * Gets a value indicating whether the file is hidden on the device's file system.
     *
     * @return {@code true} if the file/directory is hidden. {@code false} otherwise.
     */
    @Override
    public boolean isHidden() {
        return false;
    }

    /**
     * Gets a value indicating whether the file/directory exists.
     *
     * @return {@code true} if the file/directory exists. {@code false} otherwise.
     */
    @Override
    public boolean exists() {
        return false;
    }

    /**
     * Gets the owner of the file/directory.
     *
     * @return The owner.
     */
    @Override
    public String getOwner() {
        return null;
    }

    /**
     * Gets the group the file/directory is associated with.
     *
     * @return The group.
     */
    @Override
    public String getGroup() {
        return null;
    }

    /**
     * Gets the size (in bytes) used by the file/directory on the file system.
     *
     * @return The sire of the file/directory in bytes.
     */
    @Override
    public long getSize() {
        return 0;
    }
}
