package eu.casoftworks.jdroidlib.device.fs;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.exception.*;
import eu.casoftworks.jdroidlib.exception.FileAlreadyExistsException;
import eu.casoftworks.jdroidlib.interfaces.*;
import eu.casoftworks.jdroidlib.util.*;

import java.io.*;
import java.nio.file.*;
import java.text.*;
import java.time.*;
import java.util.*;
import java.util.logging.*;

/**
 * Represents a file stored on an Android {@link Device}.
 * AndroidFiles may be instantiated by the library, but can also be used extensively in any applications using the library.
 */
public class AndroidFile implements IFile {

    // All these fields should be effectively final!
    private String name;
    private String extension;
    private IDirectory parentDir;
    private String fullName;
    private Device hostDevice;
    private String fullPath;
    private String owner;
    private String group;
    private long size;
    private int noOfSymlinks;
    private Date mTime;

    /**
     * Object constructor.
     * @param fullPath
     */
    public AndroidFile(Device hostDevice, String fullPath) {

        // Do some error checking
        if (hostDevice == null)
            throw new IllegalArgumentException("Host device must not be null!");
        if (fullPath == null || fullPath.isEmpty())
            throw new IllegalArgumentException("fullName must not be null!");

        this.hostDevice = hostDevice;
        this.fullPath = fullPath;

        String[] iGotDaSplits = fullPath.split(LINUX_PATH_SEPARATOR);

        fullName = iGotDaSplits[iGotDaSplits.length];

        if (name.contains(FILE_EXTENSION_PRECHAR) && !name.startsWith(FILE_EXTENSION_PRECHAR)) {
            String[] tmp = name.split(FILE_EXTENSION_PRECHAR);
            name = tmp[0]; // First instance in array is name of the file
            extension = tmp[tmp.length - 1]; // Account for there being periods in the filename; Only show last extension.
        } else {
            name = fullName; // No extensions found; name is effectively full name.
            extension = null;
        }

        parentDir = new AndroidDirectory(hostDevice, fullPath.replace(LINUX_PATH_SEPARATOR + getFullName(), ""));

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
                    .setCommandArgs("-l", getFullPath())
                    .create()
            ).split("\\s+");

            noOfSymlinks = Integer.parseInt(splitOutput[1]); // 1 in example
            owner = splitOutput[2]; // vivek in example
            group = splitOutput[3]; // admin in example
            size = Long.parseLong(splitOutput[4]); // 2558 in example
            mTime = DateFormat.getDateInstance().parse(String.join(" ", splitOutput[5], splitOutput[6], splitOutput[7]));

        } catch (IOException | IllegalDeviceStateException | InterruptedException e) {
            e.printStackTrace();
            owner = group = NOT_AVAILABLE;
            size = -1;
        } catch (ParseException ex) {
            mTime = Date.from(Instant.MIN);
        }
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
    public File pull() throws FileCouldNotBePulledException {
        return pull(IResourceManager.combinePaths(IResourceManager.getJDroidLibTmpDirectory(), getFullName()));
    }

    /**
     * Attempts to pull the file from the {@link Device} to a given location on the host.
     *
     * @param location The location to pull the file to.
     *
     * @return The new location of the pulled file. (location/file if file is not specified in location)
     */
    @Override
    public File pull(File location) throws FileCouldNotBePulledException {
        return pull(location.getAbsolutePath());
    }

    /**
     * The same as {@link IFile#pull(File)}.
     *
     * @param location The location to pull the file to.
     *
     * @return The new location of the pulled file on the host.
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
     * Copies a file or directory to the desired location on the device's file system.
     *
     * @param destination The destination to copy the file/directory.
     */
    @Override
    public void copyTo(IFileSystemEntry destination) throws DeviceException {
        if (destination.exists())
            throw new FileAlreadyExistsException(String.format("The file %s already exists!", destination.getFullPath()));

        String cmdOutput;
        try {
            cmdOutput = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                    new AdbShellCommand.Factory()
                            .setDevice(getHostDevice())
                            .setCommandTag(CP_CMD)
                            .setCommandArgs(getFullPath(), destination.getFullPath())
                            .create()
            );
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            throw new DeviceException(ex);
        }

        if (cmdOutput.isEmpty())
            throw new DeviceException(cmdOutput);

    }

    /**
     * Moves a file or directory to the desired location on the device's file system.
     *
     * @param destination The location to move the file to.
     *
     * @throws DeviceException
     */
    @Override
    public void moveTo(IFileSystemEntry destination) throws DeviceException {
        if (destination.exists())
            throw new FileAlreadyExistsException(String.format("The file %s already exists!", destination.getFullPath()));

        String cmdOutput;
        try {
            cmdOutput = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                    new AdbShellCommand.Factory()
                            .setDevice(getHostDevice())
                            .setCommandTag(MV_CMD)
                            .setCommandArgs(getFullPath(), destination.getFullPath())
                            .create()
            );
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
            throw new DeviceException(ex);
        }

        if (cmdOutput.isEmpty())
            throw new DeviceException(cmdOutput);

    }

    /**
     * Attempts to get the file's contents.
     * The file may be pulled to the host in order to retrieve the bytes!
     *
     * NOT RECOMMENDED FOR FILES > 30MiB!
     *
     * @return The file's raw contents.
     */
    @Override
    public byte[] getContents() throws DeviceException {
            try {
                if (isText()) {
                    return AndroidController.getControllerOrNull()
                            .executeCommandReturnOutput(
                                    new AdbCommand.Factory()
                                            .setDevice(getHostDevice())
                                            .setCommandTag(CAT_CMD)
                                            .setCommandArgs(getFullPath())
                                            .create()
                            ).getBytes();
                } else {
                    File pulledFile = pull();
                    byte[] bytes = Files.readAllBytes(pulledFile.toPath());
                    return bytes;
                }
            } catch (IOException | InterruptedException ex) {
                Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error occurred getting file contents from device!", ex);
                ex.printStackTrace();
                throw new DeviceException(
                    String.format(
                            "Failed to fetch contents from file %s on device [[%s]]!\n" +
                                "An exception occurred within the library!\n" +
                                "Error message: %s\n" +
                                "Further details:\n" +
                                "%s",
                            getFullPath(), getHostDevice().getID(), ex.getMessage(), ex.toString()
                    )
                );
            }
    }

    /**
     * Attempts to get string contents from a file.
     *
     * @return The file's contents.
     */
    @Override
    public String getContentsAsString() throws DeviceException {
        return new String(getContents()); // Whatever dude.
    }

    /**
     * Attempts to create a file on the device's file system.
     *
     * @return {@code true} if file creation was successful. {@code false} otherwise.
     */
    @Override
    public boolean touch() throws CannotTouchException {
        String output;
        try {
            output = AndroidController.getControllerOrNull().executeCommandReturnOutput(
                    new AdbShellCommand.Factory()
                        .setDevice(getHostDevice())
                        .setCommandTag(TOUCH_CMD)
                        .setCommandArgs(getFullPath())
                        .create()
            );
            return output.isEmpty();
        } catch (IOException | IllegalDeviceStateException | InterruptedException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not touch file on device!", ex);
            ex.printStackTrace();
            throw new CannotTouchException(ex);
        }
    }

    /**
     * {@inheritDoc}
     * @return
     * @throws DeviceException
     */
    @Override
    public boolean isText() throws DeviceException {
        try {
            return AndroidController.getControllerOrNull().executeCommandReturnOutput(
                new AdbCommand.Factory()
                    .setDevice(getHostDevice())
                    .setCommandTag(FILE_CMD)
                    .setCommandArgs(getFullPath())
                    .create()
            ).contains("text");
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Error occurred determining file contents!", ex);
            ex.printStackTrace();
            throw new DeviceException(ex);
        }
    }

    /**
     * Gets the file's MTIME ((last) modification time).
     * @return A date noting the last time the file was modified.
     */
    @Override
    public Date getModTime() { return mTime; }

    /**
     * Attempts to remove a file from the {@link Device}'s {@link java.io.FileSystem}
     * @return {@code true} if file was removed. {@code false} otherwise.
     */
    @Override
    public boolean remove(boolean force) throws CannotRemoveException {
        try {
            AndroidController.getControllerOrNull().executeCommandNoOutput(
                    new AdbShellCommand.Factory()
                            .setDevice(getHostDevice())
                            .setCommandTag(RM_CMD)
                            .setCommandArgs(force ? REMOVE_FORCE : "", getFullPath())
                            .create()
            );
            return !exists();
        } catch (IOException | InterruptedException | DeviceException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Could not remove file from device!", ex);
            ex.printStackTrace();
            throw new CannotRemoveException(ex);
        }
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
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, "An error occurred getting file permissions from device!", ex);
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
                    .setCommandTag(FILE_EXISTS_CMD.replace(CMD_ARG_FILE, getFullPath()))
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
        return size;
    }

}
