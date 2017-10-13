package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.device.buildprop.*;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;
import java.util.*;

/**
 * Device class; represents {@link Device} build properties.
 * Allows retrieval and manipulation.
 */
public class BuildProp {

    private final Device parentDevice;
    private final AndroidController adbController;

    BuildProp(Device device) {
        this.parentDevice = device;
        this.adbController = AndroidController.getControllerOrNull();
    }

    AdbShellCommand getGetPropCommand(String prop) {
        return new AdbShellCommand.Factory()
                .setDevice(parentDevice)
                .setCommandTag("getprop")
                .setCommandArgs(prop != null ? prop : "")
                .create();
    }

    /**
     * Gets all of the {@link Device}'s build properties in form of a list.
     * @return All build properties found on the parent {@link Device}
     * @throws DeviceException If an error occurs.
     */
    public List<BuildProperty> getProperties() throws DeviceException {
        List<BuildProperty> properties = new ArrayList<>();
        String cmdOutput;
        try {
            cmdOutput = adbController.executeCommandReturnOutput(
                getGetPropCommand(null)
            );

            try (BufferedReader reader = new BufferedReader(new StringReader(cmdOutput))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.replace('[', '\0').replace(']','\0').trim();
                    if (line.isEmpty() || !line.contains(":"))
                        continue;

                    String[] split = line.split(":");
                    properties.add(new BuildProperty(split[0], split[1]));
                }
            }
        } catch (IOException | InterruptedException | IllegalDeviceStateException e) {
            e.printStackTrace();
            throw new DeviceException(e);
        }

        return properties;
    }

    /**
     * Gets a single build property from the parent {@link Device}
     * @param prop the property to get
     * @return
     * @throws DeviceException
     */
    public BuildProperty getProperty(String prop) throws DeviceException {
        String cmdOutput;
        try {
            cmdOutput = adbController.executeCommandReturnOutput(
                getGetPropCommand(prop)
            );

            try (BufferedReader reader = new BufferedReader(new StringReader(cmdOutput))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    line = line.replace('[', '\0').replace(']','\0').trim();
                    if (line.isEmpty() || !line.contains(":"))
                        continue;

                    String[] split = line.split(":");
                    return new BuildProperty(split[0], split[1]);
                }
            }
        } catch (IOException | InterruptedException | IllegalDeviceStateException e) {
            e.printStackTrace();
            throw new DeviceException(e);
        }
        throw new DeviceException(String.format("Build property %s was not found!", prop));
    }

    /**
     * Sets the parent {@link Device}'s build property.
     * @param prop The property to modify.
     * @throws DeviceException If an error occurs.
     */
    public void setProperty(BuildProperty prop) throws DeviceException {
        try {
            adbController.executeCommandNoOutput(
                new AdbShellCommand.Factory()
                    .setDevice(parentDevice)
                    .setCommandTag("setprop")
                    .setCommandArgs(prop.getKey(), prop.getValue().toString())
                    .create()
            );
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            throw new DeviceException(e);
        }
    }

}
