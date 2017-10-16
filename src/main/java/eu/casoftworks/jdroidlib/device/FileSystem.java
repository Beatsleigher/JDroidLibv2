package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.device.fs.*;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.interfaces.*;

import java.io.*;
import java.util.*;

/**
 * Represents the file system of a given device.
 * Contains functionality for viewing and manipulating said file system.
 *
 * @apiNote ADB must be running as ROOT to list certain paths!
 */
public class FileSystem {

    private final Device parentDevice;
    private final AndroidController adbController;
    private final IDirectory rootDirectory;

    FileSystem(Device device) {
        parentDevice = device;
        adbController = AndroidController.getControllerOrNull();
        rootDirectory = new AndroidDirectory(device, IFileSystemEntry.LINUX_PATH_SEPARATOR);
    }

    /**
     * Lists all file system entries from the root of the {@link Device}'s file system.
     * @return A {@link List<IFileSystemEntry>} containing all entries from the root of the {@link Device}'s file system
     * @apiNote ADB must be running as ROOT to fetch certain paths!
     *
     * @see List
     * @see IFileSystemEntry
     * @see Device
     */
    public List<IFileSystemEntry> listDirectoryContents() throws DeviceException { return listDirectoryContents(rootDirectory); }

    /**
     * Lists all the contents from the passed {@link IDirectory}.
     * @param directory The directory to list the contents from.
     * @return A list containing all {@link IFileSystemEntry} from the directory.
     * @throws DeviceException If an error occurs during listing.
     */
    public List<IFileSystemEntry> listDirectoryContents(IDirectory directory) throws DeviceException {
        return directory.getContents();
    }

    /**
     * Lists all files from the passed {@link IDirectory}.
     * @param directory The directory to list the files from.
     * @return A list containing all {@link IFile}s from the directory.
     * @throws DeviceException if an error occurs during listing.
     */
    public List<IFile> listFiles(IDirectory directory) throws DeviceException {
        return directory.getFiles();
    }

    /**
     * Lists all the directories in the passed {@link IDirectory}.
     * @param directory The directory to list the directories from.
     * @return A list containing {@link IDirectory} instances.
     * @throws DeviceException If an error occurs during listing.
     */
    public List<IDirectory> listDirectories(IDirectory directory) throws DeviceException {
        return directory.getDirectories();
    }

    /**
     * Pulls a file/directory from the device to a library-specified location
     * on the host.
     * @param fsEntry The file/directory to be pulled.
     * @return The new location of the pulled file/directory.
     * @throws DeviceException If an error occurs.
     */
    public File pull(IFileSystemEntry fsEntry) throws DeviceException {
        return fsEntry.pull();
    }

    /**
     * Pulls a file/directory from the device to a specified location on the host.
     *
     * @param fsEntry The file/directory to be pulled.
     * @param location The path on the host to pull the file to.
     * @return The new location of the pulled file/directory.
     * @throws DeviceException If an error occurs.
     */
    public File pull(IFileSystemEntry fsEntry, File location) throws DeviceException {
        return fsEntry.pull(location);
    }

    /**
     * Pulls a file/directory from the device to a specified location on the host.
     *
     * @param fsEntry The file/directory to be pulled.
     * @param location The path on the host to pull the file to.
     * @return The new location of the pulled file/directory.
     * @throws DeviceException If an error occurs.
     */
    public File pull(IFileSystemEntry fsEntry, String location) throws DeviceException {
        return fsEntry.pull(location);
    }

    /**
     * Attempts to push (upload) a file/directory the the parent {@link Device}.
     * @param src The file/directory to push
     * @param dest The location on the {@link Device}
     * @throws IOException If an I/O error occurs on the host.
     * @throws DeviceException If an I/O error occurs on the {@link Device} or the file/directory could not be pushed.
     * @throws InterruptedException If an error occurs during command execution.
     */
    public void push(File src, IFileSystemEntry dest) throws IOException, DeviceException, InterruptedException {
        // Error checking
        if (src.isFile() && !IFileSystemEntry.isFile(dest) || src.isDirectory() && IFileSystemEntry.isFile(dest)) {
            throw new IllegalArgumentException("Files cannot be pushed to directory and vice-versa!");
        }

        if (!src.exists())
            throw new IOException(String.format("Cannot push a file/directory that doesn't exist! (%s)", src.getAbsolutePath()));
        if (dest.exists())
            throw new CannotTouchException(String.format("%s already exists!", dest.getFullPath()));

        String cmdOutput = AndroidController.getControllerOrNull().executeCommandReturnOutput(
            new AdbCommand.Factory()
                .setDevice(parentDevice)
                .setCommandTag("push")
                .setCommandArgs(src.getAbsolutePath(), dest.getFullPath())
                .create()
        );

        if (cmdOutput.contains("adb: error:"))
            throw new FileCouldNotBePushedException(cmdOutput);

    }

}
