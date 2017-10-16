package eu.casoftworks.jdroidlib.util;

import eu.casoftworks.jdroidlib.device.*;
import eu.casoftworks.jdroidlib.enums.*;

import java.util.*;

public class PermissionSet {

    /**
     * Instantiates a new instance of this class via use of `stat %a %N` output.
     * It is important that the output used matches the output of the above command!
     * @param statOutput The output produced by executing `stat %a %N` on an Android {@link Device}
     * @return An instance of this class.
     *
     * @see Device
     * @see eu.casoftworks.jdroidlib.commands.AdbShellCommand
     */
    public static PermissionSet fromStatOutput(String statOutput) {
        return new PermissionSet(
            // Maybe someone will think of a more efficient way to do this?
            Integer.parseInt(Character.toString(statOutput.charAt(0))),
            Integer.parseInt(Character.toString(statOutput.charAt(1))),
            Integer.parseInt(Character.toString(statOutput.charAt(2)))
        );
    }

    private final Permission ownerPermissions, groupPermissions, userPermissions;

    /**
     * Initialises an instance of this class.
     * @param ownerPermissions The file owner's permissions.
     * @param groupPermissions The group's permissions.
     * @param userPermissions Other users' permissions.
     */
    public PermissionSet(Permission ownerPermissions, Permission groupPermissions, Permission userPermissions) {
        this.ownerPermissions = ownerPermissions;
        this.groupPermissions = groupPermissions;
        this.userPermissions = userPermissions;
    }

    /**
     * Initialises a new instance of this class, where all permissions are the same (e.g. 777 (Read, Write, Execute))
     * @param universalPerms The permissions for all members.
     */
    public PermissionSet(Permission universalPerms) {
        this(universalPerms, universalPerms, universalPerms);
    }

    /**
     * Initialises a new instance of this class using octal values.
     * @param octal The universal octal value (e.g. 7)
     */
    public PermissionSet(int octal) {
        this(Permission.fromOctal(octal));
    }

    /**
     * Initialises a new instance of this class using octal values.
     * @param ownerOctal The owner permission octal (e.g. 6)
     * @param groupOctal The group permission octal (e.g. 4)
     * @param userOctal The (other) user permission octal (e.g. 4)
     */
    public PermissionSet(int ownerOctal, int groupOctal, int userOctal) {
        this(Permission.fromOctal(ownerOctal), Permission.fromOctal(groupOctal), Permission.fromOctal(userOctal));
    }

    public Permission getOwnerPermissions() { return ownerPermissions; }

    public Permission getGroupPermissions() { return groupPermissions; }

    public Permission getUserPermissions() { return userPermissions; }
}
