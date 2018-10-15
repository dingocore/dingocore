package com.dingocore;

public enum State {
    INIT("init"),
    READY("ready"),
    DISCONNECTED("disconnected"),
    SLEEPING("sleeping"),
    LOST("lost"),
    ALERT("alert");

    State(String identifier) {
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

