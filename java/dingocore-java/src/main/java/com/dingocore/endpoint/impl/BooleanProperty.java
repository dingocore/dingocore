package com.dingocore.endpoint.impl;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class BooleanProperty extends AbstractProperty<Boolean> {

    public BooleanProperty(DefaultService service, String identifier) {
        this( service, identifier, null);
    }

    public BooleanProperty(DefaultService service, String identifier, Consumer<Boolean> handler) {
        super( service, identifier, "boolean", handler);
    }

    @Override
    public void updateValue(Boolean value) {
        getBusConnection().publish(topic(), value.toString(), 1, true);
    }

    @Override
    protected Consumer<byte[]> wrapHandler(Consumer<Boolean> handler) {
        return (bytes)->{
            String str = new String(bytes, StandardCharsets.UTF_8);
            boolean value = Boolean.parseBoolean(str);
            System.err.println( "HEARD: " + str  + " (parsed) " + value + " to " + handler);
            handler.accept(value);
        };
    }

}
