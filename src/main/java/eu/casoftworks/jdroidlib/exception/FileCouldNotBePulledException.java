package eu.casoftworks.jdroidlib.exception;

/**
 * Device-class exception that may be thrown when a pull (file or folder) attempt fails.
 * This exception may be thrown on any instance of {@link eu.casoftworks.jdroidlib.interfaces.IFileSystemEntry}!
 */
public class FileCouldNotBePulledException extends DeviceException {

    public FileCouldNotBePulledException() { super(); }

    public FileCouldNotBePulledException(String msg) { super(msg); }

}
