package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.util.*;

import java.io.*;

/**
 * Defines a filesystem entry on a given device.
 */
public interface IFileSystemEntry {

    String TOUCH_CMD = "touch";
    String MKDIR_CMD = "mkdir";
    String RM_CMD = "rm";
    String PULL_CMD = "pull";
    String FILE_CMD = "file";
    String CAT_CMD = "cat";
    String STAT_CMD = "stat";
    String FILE_EXISTS_CMD = "if [ -f {file} ]; then echo \"TOUCHED\"; else echo \"UNTOUCHED\"; fi";
    String DIR_EXISTS_CMD = "if [ -d {dir} ]; then echo \"TOUCHED\"; else echo \"UNTOUCHED\"; fi";
    String LS_CMD = "ls";

    String[] STAT_PERMS_ARGS = new String[] { "%a", "%N" };

    String OUTPUT_ERR = "error";
    String OUTPUT_FILE_EXISTS = "TOUCHED";
    String OUTPUT_FILE_NOT_EXISTS = "UNTOUCHED";
    String CURRENT_DIR = ".";
    String PARENT_DIR = "..";

    String CMD_ARG_FILE = "{file}";
    String CMD_ARG_DIR = "{dir}";
    String REMOVE_FORCE = "-f";
    String REMOVE_RECURSE = "-R";

    String LINUX_PATH_SEPARATOR = "/";
    String FILE_EXTENSION_PRECHAR = ".";

    String NOT_AVAILABLE = "n/a";

    /**
     * Gets the {@link Device} the file is hosted on.
     * @return The hosting {@link Device}.
     */
    Device getHostDevice();

    /**
     * The permissions for the current file system entry.
     * @return The file system entry's permissions.
     */
    PermissionSet getPermissions() throws DeviceException;

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
    boolean exists() throws DeviceException;

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
     * Attempts to pull the file or folder (including contents!) to the host system.
     * The file/folder will be pulled to a library-generated directory on the host!
     * @return The new location of the file/folder.
     *
     * @see File
     */
    File pull() throws FileCouldNotBePulledException;

    /**
     * Attempts to pull the file/folder (including contents) to a specified location on the host system.
     * @param location The location to pull the file/folder to.
     * @return The new location of the file/folder on the host system.
     *
     * @see File
     */
    File pull(File location) throws FileCouldNotBePulledException;

    /**
     * Attempts to pull the file/folder (including its contents) to a specified location on the host system.
     * @param location The location to pull the file/folder to.
     * @return The new location of the file/folder on the host.
     *
     * @see File
     */
    File pull(String location) throws FileCouldNotBePulledException;

    /**
     * Gets a value indicating whether an instance of this interface represents a file or not.
     * @param fsEntry The file system entry to check.
     * @return {@code true} if the file system entry is a file. {@code false} otherwise.
     */
    static boolean isFile(IFileSystemEntry fsEntry) {
        return fsEntry instanceof IFile;
    }

}
