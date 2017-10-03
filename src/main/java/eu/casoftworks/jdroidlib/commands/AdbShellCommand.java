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

/**
 * Represents a Linux shell command executed via ADB.
 * BE WARNED; Shell commands can be run as root!
 * @author Simon Cahill
 */
public class AdbShellCommand extends AdbCommand {
    
    private final boolean runAsRoot;
    
    /**
     * The default and only constructor.
     * @param device
     * @param cmdType
     * @param cmdTag
     * @param runAsRoot
     * @param cmdArguments 
     */
    AdbShellCommand(Device device, CommandType cmdType, String cmdTag, boolean runAsRoot, String... cmdArguments) {
        super(device, cmdType, cmdTag, cmdArguments);
        this.runAsRoot = runAsRoot;
    }
    
    /**
     * Gets a value indicating whether this command should be run as the root
     * user or not.
     * @return {@code true} if command should be run as root.
     */
    public boolean runAsRoot() { return runAsRoot; }
    
    public class Factory {
        
        private Device device;
        private CommandType cmdType = CommandType.AdbShellCommand;
        private String cmdTag;
        private boolean runAsRoot;
        private String[] cmdArgs;
        
        public Factory setDevice(Device device) {
            this.device = device;
            return this;
        }
        
        public Factory setCommandTag(String cmdTag) {
            this.cmdTag = cmdTag;
            return this;
        }
        
        public Factory runAsRoot(boolean runAsRoot) {
            this.runAsRoot = runAsRoot;
            return this;
        }
        
        public Factory setCommandArgs(String... args) {
            this.cmdArgs = args;
            return this;
        }
        
        public AdbShellCommand create() { return new AdbShellCommand(device, cmdType, cmdTag, runAsRoot, cmdArgs); }
        
    }
    
}
