/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.exception.PlatformNotSupportedException;
import eu.casoftworks.jdroidlib.interfaces.IResourceManager;
import eu.casoftworks.jdroidlib.util.OsCheck;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.logging.*;
import java.util.zip.*;

/**
 * The library's internal resource manager.
 * @author Simon Cahill
 */
class ResourceManager implements IResourceManager {
    
    //<editor-fold desc="Constants" defaultstate="collapsed">
    public static final String LATEST_PTOOLS_WIN;
    
    public static final String LATEST_PTOOLS_MAC;
    
    public static final String LATEST_PTOOLS_LNX;
    
    public static final String JDROIDLIB_HOME = ".jdroidlib";
    
    public static final String JDROIDLIB_TMP = "jdl_tmp";
    
    public static final String JDROIDLIB_LIB = "jdl_lib";
    
    public static final String PSEPCHAR = File.separator;
    
    public static final String PTOOLS_FNAME = "platform-tools.zip";
    
    public static final String PTOOLS_INSTALL_DIR = "platform-tools";
    
    public static final String ADB_BIN_EXE = "adb.exe";
    
    public static final String ADB_BIN = "adb";
    
    public static final String FASTBOOT_BIN_EXE = "fastboot.exe";
    
    public static final String FASTBOOT_BIN = "fastboot";
    //</editor-fold>
    
    /**
     * Static "constructor"
     */
    static {
        LATEST_PTOOLS_LNX = "https://dl.google.com/android/repository/platform-tools-latest-linux.zip";
        LATEST_PTOOLS_MAC = "https://dl.google.com/android/repository/platform-tools-latest-darwin.zip";
        LATEST_PTOOLS_WIN = "https://dl.google.com/android/repository/platform-tools-latest-windows.zip";
    }

    //<editor-fold desc="Overridden Members" defaultstate="collapsed">
    @Override
    public Future<File> downloadPlatformTools() throws PlatformNotSupportedException {
       return downloadPlatformTools(defaultPlatformToolsDownloadPath()); 
    }

    @Override
    public Future<File> downloadPlatformTools(File location) throws PlatformNotSupportedException {
        return downloadPlatformTools(location.getAbsolutePath());
    }

    @Override
    public Future<File> downloadPlatformTools(String location) throws PlatformNotSupportedException {
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        FutureTask<File> task = null;
        
        switch (osType) {
            default:
            case Other:
                throw new PlatformNotSupportedException(String.format("The platform \"%s\" is not supported by JDroidLib!", System.getProperty("os.name")));
            case Linux:
                task = new FutureTask<File>(() -> {
                    File outputFile = null;
                    try {
                        outputFile = downloadFile(LATEST_PTOOLS_LNX, location);
                    } catch (IOException ex) {
                        Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (outputFile == null)
                            return null;
                    }
                    
                    return outputFile;
                    
                });
                break;
            case MacOS:
                task = new FutureTask<>(() -> {
                   File outputFile = null;
                   
                    try {
                        outputFile = downloadFile(LATEST_PTOOLS_MAC, location);
                    } catch (IOException ex) {
                        Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (outputFile == null)
                            return null;
                    }
                    
                    return outputFile;
                });
                break;
            case Windows:
                task = new FutureTask<>(() -> {
                    File outputFile = null;
                    
                    try {
                        outputFile = downloadFile(LATEST_PTOOLS_WIN, location);
                    } catch (IOException ex) {
                        Logger.getLogger(ResourceManager.class.getName()).log(Level.SEVERE, null, ex);
                    } finally {
                        if (outputFile == null)
                            return null;
                    }
                    return outputFile;
                });
                break;
        }
        
        task.run();
        return task;
    }

    @Override
    public void installFiles() throws InterruptedException, ExecutionException, PlatformNotSupportedException, ZipException, IOException {
        File dlLocation = null;
        if (!(dlLocation = new File(defaultPlatformToolsDownloadPath())).exists()) {
            dlLocation = downloadPlatformTools().get();
        }

        ZipInputStream zipIStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(dlLocation)));
        File outputDir = new File(String.join(PSEPCHAR, getJDroidLibLibDirectory(), PTOOLS_INSTALL_DIR));
        
        // Check if output exists
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        // Write compressed files to FS
        ZipEntry entry = zipIStream.getNextEntry();
        byte[] buffer = new byte[4096];
        while (entry != null){
            String fileName = entry.getName().replace(outputDir.getName(), "");
            File newFile = new File(String.join(PSEPCHAR, outputDir.getAbsolutePath(), fileName));

            if (entry.isDirectory()) {
                newFile.mkdirs();
                zipIStream.closeEntry();
                entry = zipIStream.getNextEntry();
                continue;
            }

            //create directories for subdirectories in zip
            newFile.getParentFile().mkdirs();
            FileOutputStream fileOStream = new FileOutputStream(newFile);
            int len;
            while ((len = zipIStream.read(buffer)) > 0) {
                fileOStream.write(buffer, 0, len);
            }
            fileOStream.flush();
            fileOStream.close();
            //close this ZipEntry
            zipIStream.closeEntry();
            entry = zipIStream.getNextEntry();
        }

    }

    @Override
    public void addToSystemPath(File installLocation) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File getAdb() throws FileNotFoundException {
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        File adb = new File(String.join(PSEPCHAR, getJDroidLibLibDirectory(), PTOOLS_INSTALL_DIR, osType == OsCheck.OSType.Windows ? ADB_BIN_EXE : ADB_BIN));
        if (adb.exists()) 
            return adb;
        else throw new FileNotFoundException("ADB was not found on this system! Install ADB to continue!");
    }

    @Override
    public File getFastboot() throws FileNotFoundException {
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        File adb = new File(String.join(PSEPCHAR, getJDroidLibLibDirectory(), PTOOLS_INSTALL_DIR, osType == OsCheck.OSType.Windows ? FASTBOOT_BIN_EXE : FASTBOOT_BIN));
        if (adb.exists()) 
            return adb;
        else throw new FileNotFoundException("Fastboot was not found on this system! Install fastboot to continue!");
    }
    //</editor-fold>
    
    //<editor-fold desc="Singleton" defaultstate="collapsed" >
    private static ResourceManager _instance;
    
    /**
     * Gets the running instance of this class.
     * @return An instance of IResourceManager
     */
    public static ResourceManager getInstance() throws InterruptedException, ExecutionException, PlatformNotSupportedException, IOException {
        return _instance == null ? (_instance = new ResourceManager()) : _instance;
    }
    
    private ResourceManager() throws InterruptedException, ExecutionException, PlatformNotSupportedException, IOException {
        init();
    }
    //</editor-fold>
    
    private File downloadFile(String urlString, String dlLocation) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        List<Byte> allBytes = new ArrayList<>();
        File localPath = new File(dlLocation);
        localPath.getParentFile().mkdirs();
        localPath.createNewFile();

        // Check stuff
        if (urlString.isEmpty())
            throw new IllegalArgumentException("URL must not be empty!");
        if (dlLocation.isEmpty())
            throw new IllegalArgumentException("No download path specified!");
        if (localPath.exists() && localPath.isDirectory())
            throw new IllegalArgumentException("The referenced file must be a FILE and not a DIRECTORY!");
        
        // Download file
        try (InputStream iStream = urlConnection.getInputStream()) {
            Files.copy(iStream, localPath.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        
        return localPath;
        
    }
    
    String getJDroidLibHomeDirectory() {
        return String.join(PSEPCHAR, System.getProperty("user.home"), JDROIDLIB_HOME);
    }
    
    String getJDroidLibTmpDirectory() {
        return String.join(PSEPCHAR, getJDroidLibHomeDirectory(), JDROIDLIB_TMP);
    }
    
    String getJDroidLibLibDirectory() {
        return String.join(PSEPCHAR, getJDroidLibHomeDirectory(), JDROIDLIB_LIB);
    }
    
    String defaultPlatformToolsDownloadPath() { return String.join(PSEPCHAR, getJDroidLibTmpDirectory(), PTOOLS_FNAME); }

    private void init() throws PlatformNotSupportedException, InterruptedException, ExecutionException, IOException {

        try {
            getAdb();
            getFastboot();
        } catch (FileNotFoundException ex) {
            // ADB isn't installed
            installFiles();
        }

    }

}
