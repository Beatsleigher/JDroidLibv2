package eu.casoftworks.jdroidlib.exception;

import eu.casoftworks.jdroidlib.device.*;

/**
 * Device-class exception that is thrown when a file or directory could not
 * be pushed to a {@link Device}
 */
public class FileCouldNotBePushedException extends DeviceException {

    public FileCouldNotBePushedException() { super(); }

    public FileCouldNotBePushedException(String msg) { super(msg); }

}
