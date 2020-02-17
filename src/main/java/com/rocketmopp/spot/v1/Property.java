package com.rocketmopp.spot.v1;

public enum Property {
    USER_AGENT("user-agent"),
    CLIENT_ID("client-id"),
    CLIENT_FINGERPRINT("client-fingerprint"),
    TOUCH_SUPPORT("touch-support"),
    SCREEN_RESOLUTION("screen-resolution");

    private String value;

    Property(String value) {
        this.value = value;
    }

    public String toString() {
        return value;
    }
}