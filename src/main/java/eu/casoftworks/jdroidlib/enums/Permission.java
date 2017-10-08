package eu.casoftworks.jdroidlib.enums;

import java.util.*;

/**
 * Defines different permissions found on a Linux file system.
 */
public enum Permission {

    /**
     * No permissions at all.
     */
    None(0, "---"),

    /**
     * File can only be executed.
     */
    ExecuteOnly(1, "--x"),

    /**
     * File can only be written to.
     */
    WriteOnly(2, "-W-"),

    /**
     * File can be written to and executed.
     */
    WriteAndExecute(3, "-wx"),

    /**
     * File can only be read.
     */
    ReadOnly(4, "r--"),

    /**
     * File can be read and executed.
     */
    ReadAndExecute(5, "r-x"),

    /**
     * File can be read and written to.
     */
    ReadAndWrite(6, "rw-"),

    /**
     * File can be read from, written to, and executed.
     * (Full Access)
     */
    ReadWriteAndExecute(7, "rwx");

    int octal;
    String string;

    Permission(int i, String str) {
        octal = i;
        string = str;
    }

    /**
     * Gets the octal value of a permission.
     * @return The octal value. E.g. 7
     */
    public int getOctal() { return octal; }

    /**
     * Gets a string representation of a permission.
     * @return The string representation. E.g. rwx
     */
    public String getString() { return string; }

    /**
     * Gets a Permission value from an octal.
     * Contraints:
     * The octal must be > 0 && < 7!
     * @param octal The octal to parse.
     * @return A Permission value.
     */
    public static Permission fromOctal(int octal) {
        for (Permission permission : values()) {
            if (permission.octal == octal)
                return permission;
        }
        return null;
    }

}
