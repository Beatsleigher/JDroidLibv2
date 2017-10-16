package eu.casoftworks.jdroidlib.exception;

import eu.casoftworks.jdroidlib.device.*;

/**
 * Device-class exception that is thrown when a file or directory already
 * exists on the {@link Device}'s file system, when expected otherwise.
 */
public class FileAlreadyExistsException extends DeviceException {

    public FileAlreadyExistsException() { super(); }

    public FileAlreadyExistsException(String msg) { super(msg); }

}
