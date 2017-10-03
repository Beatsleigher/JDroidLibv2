/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.casoftworks.jdroidlib.interfaces;

import eu.casoftworks.jdroidlib.exception.PlatformNotSupportedException;

import java.io.*;
import java.net.MalformedURLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.zip.ZipException;

/**
 * Interface for defining the resource manager used internally by JDroidLib.
 * Contains all the methods necessary for managing resources used by JDroidLib.
 * 
 * @author Simon Cahill
 */
public interface IResourceManager {
    
    /**
     * Downloads the appropriate platform tools for the operating system 
     * and architecture being used by the host.
     * @return The location where the files were downloaded to.
     * @throws IOException
     * @throws MalformedURLException
     * @throws PlatformNotSupportedException
     */
    Future<File> downloadPlatformTools() throws IOException, MalformedURLException, PlatformNotSupportedException;
    
    /**
     * Downloads the appropriate platform tools for the operating system 
     * and architecture being used by the host.
     * @param location The location to download the files to.
     * @return The location where the files were downloaded to.
     * @throws IOException
     * @throws MalformedURLException
     * @throws PlatformNotSupportedException
     */
    Future<File> downloadPlatformTools(File location) throws IOException, MalformedURLException, PlatformNotSupportedException;
    
    /**
     * Downloads the appropriate platform tools for the operating system 
     * and architecture being used by the host.
     * @param location The location to download the files to.
     * @return The location where the files were downloaded to.
     * @throws IOException
     * @throws MalformedURLException
     * @throws PlatformNotSupportedException
     */
    Future<File> downloadPlatformTools(String location) throws IOException, MalformedURLException, PlatformNotSupportedException;
    
    /**
     * Installs the downloaded files to a JDroidLib-specific location.
     * @throws IOException
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws PlatformNotSupportedException
     * @throws ZipException
     */
    void installFiles() throws IOException, InterruptedException, ExecutionException, PlatformNotSupportedException, ZipException, IOException;
    
    /**
     * Enumerates the files in the given installation path
     * and adds the correct files to the system path.
     * @param installLocation The location where the files were installed.
     * @throws IOException
     */
    void addToSystemPath(File installLocation) throws IOException;
    
    /**
     * Attempts to retrieve the path of the ADB executable.
     * @return An object representing the store location of the ADB executable.
     * @throws FileNotFoundException If the file was not found on this system.
     * @see java.io.File
     */
    File getAdb() throws FileNotFoundException;
    
    /**
     * Attempts to retrieve the path of the fastboot executable.
     * @return An object representing the store location of the
     * fastboot executable.
     * @throws FileNotFoundException If the file was not found on this system.
     * @see java.io.File
     */
    File getFastboot() throws FileNotFoundException;
    
}
