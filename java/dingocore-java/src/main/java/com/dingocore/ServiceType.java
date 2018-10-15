package com.dingocore;

public enum ServiceType {

    SWITCH("switch"),
    OUTLET("outlet");

    ServiceType(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String toString() {
        return getIdentifier();
    }

    private final String identifier;
}
