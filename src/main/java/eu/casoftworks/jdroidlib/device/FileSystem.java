package eu.casoftworks.jdroidlib.device;

/**
 * Represents the file system of a given device.
 * Contains functionality for viewing and manipulating said file system.
 */
public class FileSystem {

    private final Device parentDevice;

    FileSystem(Device device) {
        parentDevice = device;
    }

}
