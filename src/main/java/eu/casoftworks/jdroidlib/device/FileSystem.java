package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.interfaces.*;

import java.util.*;

/**
 * Represents the file system of a given device.
 * Contains functionality for viewing and manipulating said file system.
 */
public class FileSystem {

    private final Device parentDevice;

    FileSystem(Device device) {
        parentDevice = device;
    }

    public List<IFileSystemEntry> listDirectoryContents() {
        return null; // TODO: IMPLEMENT
    }

}
