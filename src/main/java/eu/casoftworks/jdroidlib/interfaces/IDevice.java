package eu.casoftworks.jdroidlib.interfaces;

/**
 * Rudimentary interface defining the most basic of device.
 * Basically, a device has an ID (identifier).
 */
public interface IDevice {

    /**
     * Gets a device's identifier (ID)
     * @return A (unique) string for identifying a device.
     */
    String getID();

}
