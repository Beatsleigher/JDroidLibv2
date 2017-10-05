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
package eu.casoftworks.jdroidlib.enums;

/**
 * List of the different (supported) versions of Android.
 * @author Simon Cahill
 */
public enum AndroidVersion {
    
    Unknown(-1), // Have you tried turning it off and on again?
    
    Cupcake(1.5), // Who has one of these?!
    
    Donut(1.6), // Yeah, I could do with one of those...
    
    Eclair(2.0),
    
    Froyo(2.2),
    
    Gingerbread(2.3), // I started out here!
    
    HoneyComb(3.0), // Never saw one of these. Ever.
    
    IceCreamSandwich(4.0), // Galaxy Nexus!
    
    JellyBean(4.1), // Kinda boring
    
    KitKat(4.4), // Used for a week
    
    Lollipop(5), // Hey, kids! Come with me! I've got lollipops!
    
    Marshmellow(6),
    
    Nougat(7),
    
    Oreo(8);
    
    double versionNumber;
    
    AndroidVersion(double versionNo) {
        this.versionNumber = versionNo;
    }
 
    double getVersionNumber() { return versionNumber; }

    /**
     * Gets a value from this enumeration via a version string from Android
     * @param versionString The version to attempt to retrieve.
     * @return The desired Android version
     */
    public static AndroidVersion fromVersionString(String versionString) {
        String[] splitString = versionString.split(".", 2);
        double parsableVersion = Double.parseDouble(String.join(".", splitString));

        for (AndroidVersion version : values())
            if (version.versionNumber == parsableVersion)
                return version;
        return Unknown;

    }

    public static AndroidVersion fromVersionNumber(double versionNumber) {
        for (AndroidVersion version : values())
            if (version.versionNumber == versionNumber)
                return version;
        return Unknown;
    }

}
