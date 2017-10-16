package eu.casoftworks.jdroidlib.device.buildprop;

/**
 * Represents an Android build property.
 */
public class BuildProperty {

    private final String key;
    private Object value;

    public BuildProperty(String key) {
        this(key, null);
    }

    public BuildProperty(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Gets this build property's key.
     * @return This property's key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Gets the build property's value
     * @return The property's value
     */
    public Object getValue() {
        return value;
    }
}
