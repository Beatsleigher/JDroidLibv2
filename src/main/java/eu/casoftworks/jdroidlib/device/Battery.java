package eu.casoftworks.jdroidlib.device;

import eu.casoftworks.jdroidlib.*;
import eu.casoftworks.jdroidlib.commands.*;
import eu.casoftworks.jdroidlib.enums.*;
import eu.casoftworks.jdroidlib.exception.*;

import java.io.*;

/**
 * Represents a {@link Device}'s battery.
 * Contains information about said battery. I guess.
 */
public class Battery {

    private final Device parentDevice;
    private final AndroidController adbController;

    private boolean acPowered;
    private boolean usbPowered;
    private boolean wirelessPowered;
    private double maxCurrent;
    private double maxVoltage;
    private int cycles;
    private BatteryStatus status;
    private BatteryHealth health;
    private boolean present;
    private int level;
    private int scale;
    private double voltage;
    private double temperature;
    private String tech;

    /**
     * Instantiates a new Battery.
     *
     * @param device the device
     */
    Battery(Device device) {
        parentDevice = device;
        adbController = AndroidController.getControllerOrNull();
        refresh();
    }

    /**
     * Refresh.
     */
    void refresh() {
        String cmdOutput;
        try {
            cmdOutput = adbController.executeCommandReturnOutput(
                new AdbShellCommand.Factory()
                    .setDevice(parentDevice)
                    .setCommandTag("dumpsys")
                    .setCommandArgs("battery")
                    .create()
            );

            try (BufferedReader reader = new BufferedReader(new StringReader(cmdOutput))) {
                String line;
                while ((line = reader.readLine().toLowerCase().trim()) != null) {
                    if (line.isEmpty() || line.contains("battery service"))
                        continue;
                    if (line.contains("ac"))
                        acPowered = Boolean.parseBoolean(line.substring(13));
                    if (line.contains("usb"))
                        usbPowered = Boolean.parseBoolean(line.substring(14));
                    if (line.contains("wireless"))
                        wirelessPowered = Boolean.parseBoolean(line.substring(18));
                    if (line.contains("charging voltage"))
                        maxVoltage = Double.parseDouble(line.substring(22)) / 10;
                    if (line.contains("charging current"))
                        maxCurrent = Double.parseDouble(line.substring(22)) / 10;
                    if (line.contains("counter"))
                        cycles = Integer.parseInt(line.substring(16));
                    if (line.contains("status"))
                        status = BatteryStatus.fromCode(Integer.parseInt(line.substring(16)));
                    if (line.contains("health"))
                        health = BatteryHealth.fromCode(Integer.parseInt(line.substring(16)));
                    if (line.contains("present"))
                        present = Boolean.parseBoolean(line.substring(10));
                    if (line.contains("level"))
                        level = Integer.parseInt(line.substring(7));
                    if (line.contains("scale"))
                        scale = Integer.parseInt(line.substring(7));
                    if (line.contains("voltage"))
                        voltage = Double.parseDouble(line.substring(9)) / 1000;
                    if (line.contains("temperature"))
                        temperature = Double.parseDouble(line.substring(13)) / 10;
                    if (line.contains("technology"))
                        tech = line.substring(12);
                }
            }

        } catch (IOException | IllegalDeviceStateException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the battery's health.
     *
     * @return A value of {@link BatteryHealth}
     */
    public BatteryHealth getHealth() {
        refresh(); return health;
    }

    /**
     * Gets the battery's status.
     *
     * @return A value of {@link BatteryStatus}
     */
    public BatteryStatus getStatus() {
        refresh(); return status;
    }

    /**
     * Is ac powered boolean.
     *
     * @return boolean
     */
    public boolean isAcPowered() {
        refresh(); return acPowered;
    }

    /**
     * Is charging boolean.
     *
     * @return the boolean
     */
    public boolean isCharging() {
        refresh(); return isAcPowered() || isUsbPowered() || isWirelessPowered();
    }

    /**
     * Is present boolean.
     *
     * @return the boolean
     */
    public boolean isPresent() {
        refresh(); return present;
    }

    /**
     * Is usb powered boolean.
     *
     * @return the boolean
     */
    public boolean isUsbPowered() {
        refresh(); return usbPowered;
    }

    /**
     * Is wireless powered boolean.
     *
     * @return the boolean
     */
    public boolean isWirelessPowered() {
        refresh(); return wirelessPowered;
    }

    /**
     * Gets parent device.
     *
     * @return the parent device
     */
    public Device getParentDevice() {
        refresh(); return parentDevice;
    }

    /**
     * Gets max current.
     *
     * @return the max current
     */
    public double getMaxCurrent() {
        refresh(); return maxCurrent;
    }

    /**
     * Gets max voltage.
     *
     * @return the max voltage
     */
    public double getMaxVoltage() {
        refresh(); return maxVoltage;
    }

    /**
     * Gets temperature.
     *
     * @return the temperature
     */
    public double getTemperature() {
        refresh(); return temperature;
    }

    /**
     * Gets voltage.
     *
     * @return the voltage
     */
    public double getVoltage() {
        refresh(); return voltage;
    }

    /**
     * Gets cycles.
     *
     * @return the cycles
     */
    public int getCycles() {
        refresh(); return cycles;
    }

    /**
     * Gets level.
     *
     * @return the level
     */
    public int getLevel() {
        refresh(); return level;
    }

    /**
     * Gets scale.
     *
     * @return the scale
     */
    public int getScale() {
        refresh(); return scale;
    }

    /**
     * Gets tech.
     *
     * @return the tech
     */
    public String getTech() {
        refresh(); return tech;
    }
}
