package eu.casoftworks.jdroidlib.exception;

import eu.casoftworks.jdroidlib.device.*;

/**
 * Device-class exception that can be thrown when a file cannot be touched on
 * an Android {@link Device}.
 */
public class CannotTouchException extends DeviceException {

    static String ctd = "Can't touch dis.";
    static String ddd = "DUH duh duh duh, DUH DUH, DUH DUH!";

    public CannotTouchException() { super(); }

    public CannotTouchException(String msg) { super(msg); }

    public CannotTouchException(Throwable throwable) { super(throwable); }


}
