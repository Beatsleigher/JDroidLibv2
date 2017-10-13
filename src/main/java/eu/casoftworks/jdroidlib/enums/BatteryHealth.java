package eu.casoftworks.jdroidlib.enums;

/**
 * Battery health indicator.
 */
public enum BatteryHealth {
    /**
     * The health of the battery is unknown - Could indicate a dead battery.
     */
    BATTERY_HEALTH_UNKNOWN(1),

    /**
     * The battery is in good health. Good on the phone-holder. Pat him on the back.
     */
    BATTERY_HEALTH_GOOD(2),

    /**
     * The battery is over heating!
     */
    BATTERY_HEALTH_OVERHEAT(3),

    /**
     * The battery is dead, replace at will.
     */
    BATTERY_HEALTH_DEAD(4),

    /**
     * The battery is getting too many volts.
     */
    BATTERY_HEALTH_OVER_VOLTAGE(5),

    /**
     * The battery has failed, but the failure doesn't ring any bells, sorry.
     */
    BATTERY_HEALTH_UNSPECIFIED_FAILURE(6),

    /**
     * The battery is cold. It's like cold-starting an engine. They don't like it.
     */
    BATTERY_HEALTH_COLD(7);

    int statusCode;

    BatteryHealth(int statusCode) {
        this.statusCode = statusCode;
    }

    public static BatteryHealth fromCode(int statusCode) {
        for (BatteryHealth health : values()) {
            if (health.statusCode == statusCode)
                return health;
        }
        return BATTERY_HEALTH_UNKNOWN;
    }

}
