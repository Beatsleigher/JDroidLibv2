package eu.casoftworks.jdroidlib.enums;

import java.util.*;

/**
 * Defines different permissions found on a Linux file system.
 */
public enum Permission {

    Read(4),

    Write(2),

    Execute(1);

    int octal;

    Permission(int i) {
        octal = i;
    }

    public int getOctal() { return octal; }

    public static Permission fromOctal(int octal) {
        for (Permission permission : values()) {
            if (permission.octal == octal)
                return permission;
        }
        return null;
    }

    public static int getOctal(EnumSet<Permission> permissions) {
        int result = 0;
        for (Permission perm : permissions) {
            if (result < 7)
                result = result | perm.octal;
            else
                throw new UnsupportedOperationException(String.format("Permission octal has max of 7! Current perm octal would be %d!", (result | perm.octal)));
        }

        return result;

    }

}
