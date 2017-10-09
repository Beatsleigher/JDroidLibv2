package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.device.*;

import java.io.*;
import java.util.*;

public interface IDirectory extends IFileSystemEntry {

    /**
     * Gets the name of the directory.
     * @return The name of the directory.
     */
    String getName();

    /**
     * Gets the parent directory of the file.
     * @return The directory the file is stored in.
     */
    IDirectory getParentDirectory();

    /**
     * Attempts to pull the directory (and its contents) from the given {@link Device} to a library-specific location on the host.
     * @return The storage location of the pulled directory.
     */
    File pull();

    /**
     * Attempts to pull the directory (and its contents) from the {@link Device} to a given location on the host.
     * @param location The location to pull the directory to.
     * @return The new location of the pulled directory. (location/directory if directory is not specified in location)
     */
    File pull(File location);

    /**
     * The same as {@link IDirectory#pullDirectory(File)}.
     * @param location The location to pull the directory to.
     * @return The new location of the pulled directory on the host.
     */
    File pull(String location);

    /**
     * Attempts to get all the contents (files and directories) of the directory.
     * @return The file's raw contents.
     */
    List<IFileSystemEntry> getContents();

    /**
     * Attempts to list all the files in a directory.
     * @return A list of all files in a directory.
     */
    List<IFile> getFiles();

    /**
     * Attempts to list all the directories within a directory.
     * @return A list of all directories in a directory.
     */
    List<IDirectory> getDirctories();

    /**
     * Attempts to create a directory on the device's file system.
     * @return {@code true} if directory creation was successful. {@code false} otherwise.
     */
    boolean mkdir();

    /**
     * Attempts to remove the directory from the file system.
     * @param recurse Sets a value determining whether to recurse through the directory to delete it. (Directories with content cannot be deleted without it)
     * @return {@code true} if deletion was successful. {@code false} otherwise.
     */
    boolean remove(boolean recurse);

}
