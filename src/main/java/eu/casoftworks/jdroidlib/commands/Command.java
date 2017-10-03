/*
 * Copyright (c) 2017, Simon Cahill
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package eu.casoftworks.jdroidlib.commands;

import eu.casoftworks.jdroidlib.Device;
import eu.casoftworks.jdroidlib.enums.CommandType;
import eu.casoftworks.jdroidlib.interfaces.ICommand;
import java.util.Arrays;

import java.util.List;

/**
 * Abstract class implementing ICommand.java
 * This class provides methods for creating the different types of commands 
 * used in and by JDroidLib.
 * @author Simon Cahill
 */
public class Command implements ICommand {
    
    private final CommandType commandType;
    private final String commandTag;
    private final String[] commandArgs;
    private final Device device;
    
    /**
     * The only constructor available for this class.
     * @param device The device to execute the command on. May be null!
     * @param cmdType The type of command
     * @param cmdTag The command tag (e.g. ls)
     * @param cmdArguments The command arguments (e.g. -la)
     */
    Command(Device device, CommandType cmdType, String cmdTag, String... cmdArguments) {
        this.device = device;
        commandType = cmdType;
        commandTag = cmdTag;
        commandArgs = cmdArguments;
    }

    /**
     * {@inheritDoc}
     * @return The type of the command
     */
    @Override
    public final CommandType getCommandType() { return commandType; }

    /**
     * {@inheritDoc}
     * @return Get the command tag.
     */
    @Override
    public final String getCommandTag() { return commandTag; }

    /**
     * {@inheritDoc}
     * @return The command arguments as an array of strings.
     */
    @Override
    public final String[] getCommandArguments() { return commandArgs; }

    /**
     * {@inheritDoc}
     * @return The command arguments as a list of strings.
     */
    @Override
    public List<String> getCommandArgumentsAsList() { return Arrays.asList(commandArgs); }
    
    /**
     * {@inheritDoc}
     * @return The device to execute this command on.
     * May be null!
     */
    @Override
    public Device getDevice() { return device; }
    
    /**
     * Gets a string representation of the current object.
     * @return 
     */
    @Override
    public String toString() {
        StringBuilder sBuilder = new StringBuilder();
        
        if (device != null) {
            sBuilder.append("-d")
                    .append(' ')
                    .append("<device serial/ip>")
                    .append(' ');
        }
        
        sBuilder.append(getCommandTag());
        getCommandArgumentsAsList().forEach(x -> sBuilder.append(x).append(' '));
        
        return sBuilder.toString();
    }
    
}
