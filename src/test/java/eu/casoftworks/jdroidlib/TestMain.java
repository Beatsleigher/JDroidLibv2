package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.concurrent.*;

public class TestMain {

    public static void main(String[] args) {
        AndroidController adbController;
        try {
            adbController = AndroidController.getController();
        } catch (InterruptedException | ExecutionException | PlatformNotSupportedException | IOException e) {
            e.printStackTrace();
        }

    }

}
