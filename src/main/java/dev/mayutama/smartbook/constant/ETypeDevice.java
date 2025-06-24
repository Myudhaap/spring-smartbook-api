package dev.mayutama.smartbook.constant;

public enum ETypeDevice {
    WEB("web"),
    MOBILE("mobile"),
    DESKTOP("desktop");

    private final String value;

    ETypeDevice(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static final String DEFAULT = "web";
}
