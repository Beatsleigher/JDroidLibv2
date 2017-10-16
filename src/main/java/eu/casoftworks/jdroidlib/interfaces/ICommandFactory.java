package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.device.*;

/**
 * Basic factory prototype for generating commands for use with JDroidLib.
 */
public interface ICommandFactory {

    /**
     * Sets the device to execute the command on.
     * @param device The {@link Device} to execute the command on.
     * @return The updated instance of this object.
     */
    ICommandFactory setDevice(Device device);

    /**
     * Sets the command to execute (AKA command tag)
     * @param tsg The command to execute.
     * @return The updated instance of this object.
     */
    ICommandFactory setCommandTag(String tsg);

    /**
     * Sets the arguments for the command tag.
     * @param args The arguments.
     * @return The updated instance of this object.
     */
    ICommandFactory setCommandArgs(String... args);

    /**
     * Sets a custom timeout for this single command.
     * @param millis The timeout during in ms.
     * @return The updated instance of this object.
     */
    ICommandFactory setTimeout(long millis);

    /**
     * Creates the {@link ICommand} object.
     * @return The newly created {@link ICommand}
     */
    ICommand create();

}
