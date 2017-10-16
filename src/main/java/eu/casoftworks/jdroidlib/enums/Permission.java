package eu.casoftworks.jdroidlib.enums;

import eu.casoftworks.jdroidlib.interfaces.*;

import java.util.*;
import java.util.regex.*;

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

    /**
     * Gets a permission element from a permissions string, where
     * the permission string must be three characters long, and must match
     * {@link IFileSystemEntry#PERMISSION_PATTERN}
     *
     * @param threeChar The permission string. Must match {@link IFileSystemEntry#PERMISSION_PATTERN}
     * @return The {@link Permission} representing the permission string passed.
     */
    public static Permission fromThreeChar(String threeChar) {
        int octal = 0;
        threeChar = threeChar.toLowerCase();

        if (!threeChar.matches(IFileSystemEntry.PERMISSION_PATTERN))
            throw new IllegalArgumentException("Permission string must be three characters long, consisting of R, W, X, or -!");

        for (char _char : threeChar.toCharArray()) {
            if (_char == '-')
                octal = octal | 0;
            else if (_char == 'w')
                octal = octal | WriteOnly.octal;
            else if (_char == 'r')
                octal = octal | ReadOnly.octal;
            else if (_char == 'x')
                octal = octal | ExecuteOnly.octal;
        }

        return fromOctal(octal);

    }

    public static Permission[] fromLsOutput(String lsOutput) {
        List<Permission> perms = new ArrayList<>();
        Pattern pattern = Pattern.compile(IFileSystemEntry.PERMISSION_PATTERN);
        Matcher matcher = pattern.matcher(lsOutput);
        while (matcher.find())
            perms.add(fromThreeChar(lsOutput.substring(matcher.start(), 3)));

        return (Permission[])perms.toArray();
    }

}
