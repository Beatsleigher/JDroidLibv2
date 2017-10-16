package eu.casoftworks.jdroidlib.device.pm;

import eu.casoftworks.jdroidlib.device.*;

import java.io.*;

/**
 * This object represents an Android application package (.apk) which can be used to install
 * applications to Android {@link Device}s
 */
public final class ApkFile extends File {

    public static final String EXTENSION = ".apk";

    ApkFile(String s) {
        super(s);
    }

    /**
     * Gets a new instance of this class.
     * @param apkFile The application package to reference.
     * @return A new instance of {@link ApkFile}
     */
    public static ApkFile fromFile(File apkFile) {
        if (!apkFile.exists())
            throw new IllegalArgumentException("Application package must exist on host!");
        if (!apkFile.getAbsolutePath().endsWith(EXTENSION))
            throw new IllegalArgumentException(String.format("Application packages must end with %s!", EXTENSION));

        return new ApkFile(apkFile.getAbsolutePath());
    }

}
