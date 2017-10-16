package eu.casoftworks.jdroidlib.enums;

/**
 * Battery status indicator.
 */
public enum BatteryStatus {
    /**
     * Battery status is unknown - Could indicate the battery is dead.
     */
    BATTERY_STATUS_UNKNOWN(1),

    /**
     * The battery is currently charging.
     */
    BATTERY_STATUS_CHARGING(2),

    /**
     * The battery is currently discharging.
     */
    BATTERY_STATUS_DISCHARGING(3),

    /**
     * The battery is not charging, it's idle.
     */
    BATTERY_STATUS_NOT_CHARGING(4),

    /**
     * The battery is fully charged.
     */
    BATTERY_STATUS_FULL(5);

    private int statusCode;

    BatteryStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Gets the status' code.
     * @return
     */
    public int getStatusCode() { return statusCode; }

    public static BatteryStatus fromCode(int code) {
        for (BatteryStatus status : values()) {
            if (code == status.statusCode)
                return status;
        }
        return BATTERY_STATUS_UNKNOWN;
    }

}
