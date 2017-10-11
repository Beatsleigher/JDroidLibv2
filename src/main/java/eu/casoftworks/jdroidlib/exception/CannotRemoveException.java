package eu.casoftworks.jdroidlib.exception;

/**
 * Device-class exception that is thrown when a file or directory cannot be removed
 * from the host {@link eu.casoftworks.jdroidlib.device.Device}.
 */
public class CannotRemoveException extends DeviceException {

    public CannotRemoveException() { super(); }

    public CannotRemoveException(String msg) { super(msg); }

    public CannotRemoveException(Throwable thr) { super(thr); }

}
