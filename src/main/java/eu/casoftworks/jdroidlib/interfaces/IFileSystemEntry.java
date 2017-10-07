package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.enums.*;

import java.util.*;

/**
 * Defines a filesystem entry on a given device.
 */
public interface IFileSystemEntry {

    EnumSet<Permission> getPermissions();

    String getFullPath();

    boolean isHidden();

    boolean exists();

    String getOwner();

    String getGroup();

    long getSize();

}
