package eu.casoftworks.jdroidlib.device.fs;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.interfaces.*;
import eu.casoftworks.jdroidlib.util.*;

import java.io.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.logging.*;

public class AndroidDirectory implements IDirectory {

    // All these fields should be effectively final!
    private String name;
    private IDirectory parentDir;
    private String fullName;
    private Device hostDevice;
    private String fullPath;
    private String owner;
    private String group;

    public AndroidDirectory(Device hostDevice, String fullPath) {
        // Do some error checking
        if (hostDevice == null)
            throw new IllegalArgumentException("Host device must not be null!");
        if (fullPath == null || fullPath.isEmpty())
            throw new IllegalArgumentException("fullName must not be null!");

        this.hostDevice = hostDevice;
        this.fullPath = fullPath;

        if (fullPath.equalsIgnoreCase(LINUX_PATH_SEPARATOR)) {
            // I'm the root of all evil
            parentDir = this;
            fullName = fullPath;
            name = fullName;
        } else {
            String[] iGotDaSplits = fullPath.split(LINUX_PATH_SEPARATOR);
            name = fullName = iGotDaSplits[iGotDaSplits.length - 1];

            parentDir = new AndroidDirectory(hostDevice, fullPath.replace(LINUX_PATH_SEPARATOR + getName(), ""));
        }

        init();

    }

    private void init() {
        String[] splitOutput; // Split output on whitespace(+n)
        try {
            // Example output (from nixCraft):
            // -rw-r--r-- 1 vivek admin 2558 Jan  8 07:41 filename
            splitOutput = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                    new AdbShellCommand.Factory()
                            .setDevice(getHostDevice())
                            .setCommandTag(LS_CMD)
                            .setCommandArgs("-l", CURRENT_DIR)
                            .create()
            ).split("\\s+");

            owner = splitOutput[2]; // vivek in example
            group = splitOutput[3]; // admin in example
        } catch (IOException | IllegalDeviceStateException | InterruptedException e) {
            e.printStackTrace();
            owner = group = NOT_AVAILABLE;
        }
    }

    /**
     * Gets the name of the directory.
     *
     * @return The name of the directory.
     */
    @Override
    public String getName() {
        return name;
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
     * Gets the {@link Device} the file is hosted on.
     *
     * @return The hosting {@link Device}.
     */
    @Override
    public Device getHostDevice() {
        return hostDevice;
    }

    /**
     * The permissions for the current file system entry.
     *
     * @return The file system entry's permissions.
     */
    @Override
    public PermissionSet getPermissions() throws DeviceException {
        String[] statArgs = new String[STAT_PERMS_ARGS.length + 1];
        statArgs[statArgs.length - 1] = getFullPath();
        try {
            return PermissionSet.fromStatOutput(
                    AndroidController.getControllerOrNull().executeCommandReturnOutput(
                            new AdbShellCommand.Factory()
                                    .setDevice(getHostDevice())
                                    .setCommandTag(STAT_CMD)
                                    .setCommandArgs(statArgs)
                                    .create()
                    )
            );
        } catch (IOException | InterruptedException | IllegalDeviceStateException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occurred getting directory permissions from device!", ex);
            ex.printStackTrace();
            throw new DeviceException(ex);
        }
    }

    /**
     * Gets the full path of the file system entry.
     *
     * @return E.g. /usr/local/bin/myfile.txt
     */
    @Override
    public String getFullPath() {
        return fullPath;
    }

    /**
     * Gets a value indicating whether the file is hidden on the device's file system.
     *
     * @return {@code true} if the file/directory is hidden. {@code false} otherwise.
     */
    @Override
    public boolean isHidden() {
        return name.startsWith(FILE_EXTENSION_PRECHAR);
    }

    /**
     * Gets a value indicating whether the file/directory exists.
     *
     * @return {@code true} if the file/directory exists. {@code false} otherwise.
     */
    @Override
    public boolean exists() throws DeviceException {
        String cmdOutput;
        try {
            cmdOutput = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                    new AdbShellCommand.Factory()
                            .setCommandTag(DIR_EXISTS_CMD.replace(CMD_ARG_DIR, getFullPath()))
                            .setDevice(getHostDevice())
                            .create()
            );
            if (cmdOutput.trim().equalsIgnoreCase(OUTPUT_FILE_EXISTS))
                return true;
            else return false;
        } catch (IOException | IllegalDeviceStateException | InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to determine whether file exists on device!", ex);
            ex.printStackTrace();
            throw new DeviceException(ex);
        }
    }

    /**
     * Gets the owner of the file/directory.
     *
     * @return The owner.
     */
    @Override
    public String getOwner() {
        return owner;
    }

    /**
     * Gets the group the file/directory is associated with.
     *
     * @return The group.
     */
    @Override
    public String getGroup() {
        return group;
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

    /**
     * Attempts to pull the directory (and its contents) from the given {@link Device} to a library-specific location on the host.
     *
     * @return The storage location of the pulled directory.
     */
    @Override
    public File pull() throws FileCouldNotBePulledException {
        return pull(IResourceManager.getJDroidLibTmpDirectory());
    }

    /**
     * Attempts to pull the directory (and its contents) from the {@link Device} to a given location on the host.
     *
     * @param location The location to pull the directory to.
     *
     * @return The new location of the pulled directory. (location/directory if directory is not specified in location)
     */
    @Override
    public File pull(File location) throws FileCouldNotBePulledException {
        return pull(location.getAbsolutePath());
    }

    /**
     * The same as {@link IDirectory#pull(File)}.
     *
     * @param location The location to pull the directory to.
     *
     * @return The new location of the pulled directory on the host.
     */
    @Override
    public File pull(String location) throws FileCouldNotBePulledException {
        File newFile = null;
        String cmdOutput;

        try {
            cmdOutput = AndroidController
                    .getControllerOrNull()
                    .executeCommandReturnOutput(
                            new AdbCommand
                                    .Factory()
                                    .setCommandTag(PULL_CMD)
                                    .setCommandArgs(
                                            getFullPath(),
                                            location
                                    )
                                    .setDevice(getHostDevice())
                                    .create()
                    );
        } catch (IOException | IllegalDeviceStateException | InterruptedException e) {
            e.printStackTrace();
            throw new FileCouldNotBePulledException(
                    String.format(
                            "The file %s could not be pulled from the device (%s)!\n" +
                                    "Source dest: %s" +
                                    "An exception occurred within JDroidLib; the stack trace has been printed to stderr!\n" +
                                    "Error message: %s\n" +
                                    "Further details: %s",
                            getFullPath(), getHostDevice().getID(), location, e.getMessage(), e.toString()
                    )
            );
        }

        if (cmdOutput.isEmpty() || cmdOutput.contains(OUTPUT_ERR)) {
            throw new FileCouldNotBePulledException(
                    String.format(
                            "The file %s could not be pulled from the device (%s)!\n" +
                                    "ADB output: %s",
                            getFullPath(), getHostDevice().getID(), cmdOutput
                    )
            );
        }

        Logger.getLogger(AndroidFile.class.getName()).log(Level.FINE, cmdOutput);
        newFile = new File(location);

        return newFile;
    }

    /**
     * Attempts to get all the contents (files and directories) of the directory.
     *
     * @return The file's raw contents.
     */
    @Override
    public List<IFileSystemEntry> getContents() {
        return getHostDevice().getFileSystem().listDirectoryContents();
    }

    /**
     * Attempts to list all the files in a directory.
     *
     * @return A list of all files in a directory.
     */
    @Override
    public List<IFile> getFiles() {
        // Times like these I miss C#; I want LINQ!
        List<IFile> fileList = new ArrayList<>();

        getContents().forEach(x -> {
            if (IFileSystemEntry.isFile(x))
                fileList.add((IFile)x);
        });

        return fileList;
    }

    /**
     * Attempts to list all the directories within a directory.
     *
     * @return A list of all directories in a directory.
     */
    @Override
    public List<IDirectory> getDirectories() {
        List<IDirectory> dirList = new ArrayList<>();

        getContents().forEach(x -> {
            if (!IFileSystemEntry.isFile(x))
                dirList.add((IDirectory) x);
        });

        return dirList;
    }

    /**
     * Attempts to create a directory on the device's file system.
     *
     * @return {@code true} if directory creation was successful. {@code false} otherwise.
     */
    @Override
    public boolean mkdir() throws CannotTouchException {
        String output;
        try {
            output = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                    new AdbShellCommand.Factory()
                            .setDevice(getHostDevice())
                            .setCommandTag(MKDIR_CMD)
                            .setCommandArgs(getFullPath())
                            .create()
            );
            return output.isEmpty();
        } catch (IOException | IllegalDeviceStateException | InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not create directory on device!", ex);
            ex.printStackTrace();
            throw new CannotTouchException(ex);
        }
    }

    /**
     * Attempts to remove the directory from the file system.
     *
     * @param recurse Sets a value determining whether to recurse through the directory to delete it. (Directories with content cannot be deleted without it)
     *
     * @return {@code true} if deletion was successful. {@code false} otherwise.
     */
    @Override
    public boolean remove(boolean recurse) throws CannotRemoveException {
        try {
            AndroidController.getControllerOrNull().executeCommandNoOutput(
                    new AdbShellCommand.Factory()
                            .setDevice(getHostDevice())
                            .setCommandTag(RM_CMD)
                            .setCommandArgs(recurse ? REMOVE_RECURSE : "", getFullPath())
                            .create()
            );
            return !exists();
        } catch (IOException | InterruptedException | DeviceException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not remove directory from device!", ex);
            ex.printStackTrace();
            throw new CannotRemoveException(ex);
        }
    }

}
