package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.device.Device;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.*;

/**
 * Defines a file stored on a {@link Device}'s file system.
 */
public interface IFile extends IFileSystemEntry {

    /**
     * Gets the name of the file without the file extension.
     * @return The name of the file.
     */
    String getName();

    /**
     * Gets the file's extension ONLY.
     * @return The file's extension. e.g. .sh
     */
    String getExtension();

    /**
     * Gets the fully qualified name of the file (name.ext).
     * @return The full name of the file.
     */
    String getFullName();

    /**
     * Gets a value indicating whether this file has one or more extensions.
     * @return {@code true} if the file has one or more extensions. {@code false} otherwise.
     */
    boolean hasExtension();

    /**
     * Gets the parent directory of the file.
     * @return The directory the file is stored in.
     */
    IDirectory getParentDirectory();

    /**
     * Attempts to pull the file from the given {@link Device} to a library-specific location on the host.
     * @return The storage location of the pulled file.
     */
    File pull() throws FileCouldNotBePulledException;

    /**
     * Attempts to pull the file from the {@link Device} to a given location on the host.
     * @param location The location to pull the file to.
     * @return The new location of the pulled file. (location/file if file is not specified in location)
     */
    File pull(File location) throws FileCouldNotBePulledException;

    /**
     * The same as {@link IFile#pullFile(File)}.
     * @param location The location to pull the file to.
     * @return The new location of the pulled file on the host.
     */
    File pull(String location) throws FileCouldNotBePulledException;

    /**
     * Attempts to get the file's contents.
     * The file may be pulled to the host in order to retrieve the bytes!
     *
     * NOT RECOMMENDED FOR FILES > 30MiB!
     *
     * @return The file's raw contents.
     */
    byte[] getContents() throws DeviceException;

    /**
     * Attempts to get string contents from a file.
     * @return The file's contents.
     */
    String getContentsAsString() throws DeviceException;

    /**
     * Attempts to create a file on the device's file system.
     * @return {@code true} if file creation was successful. {@code false} otherwise.
     */
    boolean touch() throws CannotTouchException;

    /**
     * Gets a value indicating whether a file is a text file or not.
     * @return {@code true} if the file contains text. {@code false} otherwise.
     */
    boolean isText() throws DeviceException;

    /**
     * Gets the file's MTIME ((last) modification time).
     * @return A date noting the last time the file was modified.
     */
    Date getModTime();

    /**
     * Attempts to remove a file from the {@link Device}'s {@link FileSystem}
     * @return {@code true} if file was removed. {@code false} otherwise.
     */
    boolean remove(boolean force) throws CannotRemoveException;

}
