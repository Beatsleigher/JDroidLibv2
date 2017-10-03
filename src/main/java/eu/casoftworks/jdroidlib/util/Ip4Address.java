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
package eu.casoftworks.jdroidlib.util;

/**
  Represents an IPv4 address.
 * @author Simon Cahill
 */
public class Ip4Address {
    
    public static final short ADB_DEFAULT_PORT = 5555;
    
    public static final String IP_MATCH_REGEX = "([0-9]{1,3}[\\.]){3}[0-9]{1,3}";
    
    private final byte[] addressBytes;
    private final short port;
    
    /**
     * Constructor allowing definition of both address bytes and the port.
     * @param ipAddress The actual IP address (e.g. 127.0.0.1)
     * @param port The port of the device. (e.g. 5555)
     */
    Ip4Address(byte[] ipAddress, short port) {
        addressBytes = ipAddress;
        this.port = port;
    }
    
    /**
     * Default constructor.
     * Initializes the port variable with the value of {@link Ip4Address#ADB_DEFAULT_PORT}
     * @param ipAddress The actual IP address (e.g. 127.0.0.1)
     */
    Ip4Address(byte[] ipAddress) {
        this(ipAddress, ADB_DEFAULT_PORT);
    }
    
    /**
     * Gets the actual IP address (IP/byte segments) represented by this object.
     * @return The IP segments as an array of bytes.
     */
    public byte[] getAddressBytes() { return addressBytes; }
    
    /**
     * Gets the port used to communicate with the device.
     * @return 
     */
    public short getPort() { return port; }
    
    @Override
    public String toString() { return String.format("%d.%d.%d.%d:%d", addressBytes[0], addressBytes[1], addressBytes[2], addressBytes[3], port); }
    
    /**
     * Returns an instance of {@link Ip4Address} from the given address.
     * @param address The IP address in string form.
     * @return An instance of {@link Ip4Address}
     */
    public static Ip4Address fromAddress(String address) { return new Ip4Address(getIpFromString(address)); }
    
    /**
     * Returns an instance of {@link Ip4Address} from the given address and port.
     * @param address The IP address in string form.
     * @param port The port of the device.
     * @return An instance of {@link Ip4Address}
     */
    public static Ip4Address fromAddress(String address, short port) { return new Ip4Address(getIpFromString(address), port > 0 ? port : ADB_DEFAULT_PORT); }
     
    /**
     * Returns an instance of {@link Ip4Address} from the given address.
     * @param ipSegments The IP segments as a byte array.
     * @return An instance of {@link Ip4Address}
     */
    public static Ip4Address fromIp(byte[] ipSegments) { return new Ip4Address(ipSegments); }
    
    /**
     * Returns an instance of {@link Ip4Address} from the given address and port.
     * @param ipSegments The IP segments as a byte array.
     * @param port The port of the device.
     * @return An instance of {@link Ip4Address}
     */
    public static Ip4Address fromIp(byte[] ipSegments, short port) { return new Ip4Address(ipSegments, port > 0 ? port : ADB_DEFAULT_PORT); }
    
    /**
     * Gets an array of bytes from a string, representing an IPv4 address.
     * 
     * @param ipAddress The IP address to parse. Must <b >not</b> contain port numbers!
     * @return A byte array containing the individual bytes (segments) of the passed IP address.
     */
    public static byte[] getIpFromString(String ipAddress) {
        byte[] ipSegments = new byte[4];
        getIpFromString(ipAddress, ipSegments);
        return ipSegments;
    }
    
    /**
     * Parses a given IP string to an array of bytes, fully representing the IP address.
     * @param ipAddress The IP address in string form.
     * @param segments The byte[] representation of the IP address.
     */
    public static void getIpFromString(String ipAddress, byte[] segments) {
        if (!ipAddress.matches(IP_MATCH_REGEX))
            throw new IllegalArgumentException("Invalid IP address found!");
        if (segments == null || segments.length != 4)
            throw new IllegalArgumentException("Segment array must be initialized and have a length of four (4)!");
        
        String[] strSegments = ipAddress.split("."); // Split on dot (.)
        
        for (int i = 0; i < segments.length; i++) {
            segments[i] = Byte.parseByte(strSegments[i]);
        }
        
    }
    
}
