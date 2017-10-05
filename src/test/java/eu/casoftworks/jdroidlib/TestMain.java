package eu.casoftworks.jdroidlib;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.concurrent.*;

public class TestMain {

    public static void main(String[] args) {
        try {
            AndroidController adbController = AndroidController.getController();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (PlatformNotSupportedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
