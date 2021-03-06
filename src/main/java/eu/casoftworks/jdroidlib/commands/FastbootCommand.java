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

import eu.casoftworks.jdroidlib.device.Device;
import eu.casoftworks.jdroidlib.enums.CommandType;

/**
 * Represents a command that can be executed on a device in Fastboot mode.
 * @author Simon Cahill
 */
public class FastbootCommand extends Command {
    
    FastbootCommand(Device device, CommandType cmdType, String cmdTag, long timeout, String... cmdArguments) {
        super(device, cmdType, cmdTag, timeout, cmdArguments);
    }
    
    public class Factory {
        
        private Device device;
        private CommandType cmdType = CommandType.FastbootCommand;
        private String cmdTag;
        private String[] cmdArgs;
        private long timeout;
        
        public Factory setDevice(Device device) {
            this.device = device;
            return this;
        }
        
        public Factory setCommandTag(String cmdTag) {
            this.cmdTag = cmdTag;
            return this;
        }
        
        public Factory setCommandArgs(String... args) {
            this.cmdArgs = args;
            return this;
        }
        
        public FastbootCommand create() { return new FastbootCommand(device, cmdType, cmdTag, timeout, cmdArgs); }

        public Factory setTimeout(long millis) {
            this.timeout = millis;
            return this;
        }

    }
    
}
