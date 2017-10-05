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

package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.concurrent.*;

/**
 * Interface defining methods for the many ways to execute a
 * command in JDroidLib!
 * The name is a pun. A punny pun.
 */
public interface IExecutioner {

    String LOCK = "U iz lawked ε=ε=ε=┌(;*´Д`)ﾉ"; // Shit, that looks painful...

    /**
     * Executes a command (defined by passing an ICommand object)
     * without returning anything from the process.
     * @param command The command to executed.
     * @see ICommand
     */
    void executeCommandNoOutput(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * and returns the command's output as a String.
     * @param command The command to execute.
     * @return The command output.
     * @see ICommand
     */
    String executeCommandReturnOutput(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * and returns the command's exit code.
     * @param command The command to execute.
     * @return The process's exit code.
     * @see ICommand
     */
    int executeCommandReturnExitCode(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * and returns both the process's exit code and the command's
     * output.
     * @param command The command to execute.
     * @return The process's exit code and output.
     *
     * @see ICommand
     * @see ITuple2
     */
    ITuple2<Integer, String> executeCommand(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * and returns the Process object that is executing.
     * @param command The command to execute.
     * @return The Process object being executed.
     *
     * @see ICommand
     * @see Process
     */
    Process executeCommandReturnProcess(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    // Define Async methods now

    /**
     * Executes a command (defined by passing an ICommand object)
     * asynchronously and returns only the task scheduling execution.
     * @param command The command to execute.
     * @return The task scheduling execution.
     *
     * @see ICommand
     * @see Future
     */
    Future executeCommandNoOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * asynchronously and returns the task scheduling execution,
     * along with the command's output in String form.
     *
     * @param command The command to execute.
     * @return The task scheduling exection with the command's output in string form
     *
     * @see ICommand
     * @see Future
     *
     */
    Future<String> executeCommandReturnOutputAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * asynchronously and returns the task scheduling execution
     * along with the process's exit code.
     * @param command The command to execute.
     *@return The task scheduling execution and the process's exit code.
     *
     * @see ICommand
     * @see Future
     */
    Future<Integer> executeCommandReturnExitCodeAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Execute a command (defined by passing an ICommand object)
     * asynchronously along with both the process's exit code
     * and the command's output in String form.
     *
     * @param command The command to execute.
     * @return The task scheduling execution and both the process's exit code
     * and the command's output.
     *
     * @see ICommand
     * @see Future
     */
    Future<ITuple2<Integer, String>> executeCommandAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Executes a command (defined by passing an ICommand object)
     * asynchronously and returns the task scheduling execution,
     * along with the actual process executing.
     * @param command The command to execute.
     * @return The task scheduling execution and the process
     * being executed.
     *
     * @see ICommand
     * @Future
     */
    Future<Process> executeCommandReturnProcessAsync(ICommand command) throws IOException, IllegalDeviceStateException, InterruptedException;

    /**
     * Gets the timeout for the current object.
     * @return
     */
    long getTimeout();

    /**
     * Sets the timeout for this object.
     * @param timeout The time to wait before forcfully terminating a process.
     */
    void setTimeout(long timeout);

    /**
     * Gets the unit of time for the timeout.
     * @return The unit of time.
     *
     * @see TimeUnit
     */
    TimeUnit getTimeUnit();

    /**
     * Sets the unit of time for the timeout.
     * @param timeUnit The unit of time.
     *
     * @see TimeUnit
     */
    void setTimeUnit(TimeUnit timeUnit);

}
