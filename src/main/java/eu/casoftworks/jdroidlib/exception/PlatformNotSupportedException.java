/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.casoftworks.jdroidlib.exception;

/**
 * An exception notifying that the current platform is not supported.
 * @author Simon Cahill
 */
public class PlatformNotSupportedException extends Exception {
    
    public PlatformNotSupportedException() {
        super();
    }
    
    public PlatformNotSupportedException(String msg) {
        super(msg);
    }
    
}
