/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.Device;
import eu.casoftworks.jdroidlib.enums.*;

import java.util.*;

/**
 * Command interface.
 * Provides prototypes for commands used within the library.
 * @author Simon Cahill
 */
public interface ICommand {
    
    /**
     * Get the command type.
     * @return The type of the command.
     */
    CommandType getCommandType();
    
    /**
     * Gets the command tag.
     * The command tag (dubbed so by me) is the name of the command.
     * For example:
     *  $ ls -la
     * Command tag: ls
     * Command args: -la
     * @return The command tag
     */
    String getCommandTag();
    
    /**
     * Gets the command's arguments as an array.
     * @return An array of Strings containing the arguments for the command.
     */
    String[] getCommandArguments();
    
    /**
     * Gets the command's arguments as a list of Strings.
     * @return A list of Strings containing the command's arguments.
     */
    List<String> getCommandArgumentsAsList();
    
    /**
     * Gets the device this command should be executed upon.
     * This may be null for server-wide commands!
     * @return The (specific) device to execute the command on.
     */
    Device getDevice();
    
    @Override
    String toString();
    
}
