package com.dingocore.endpoint.impl;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class FloatProperty extends AbstractProperty<Double> {

    public FloatProperty(DefaultService service, String identifier) {
        this(service, identifier, null);
    }

    public FloatProperty(DefaultService service, String identifier, Consumer<Double> handler) {
        super(service, identifier, "float", handler);
    }

    @Override
    public void updateValue(Double value) {
        getBusConnection().publish(topic(), value.toString(), 1, true);

    }

    @Override
    protected Consumer<byte[]> wrapHandler(Consumer<Double> handler) {
        return (bytes) -> {
            String str = new String(bytes, StandardCharsets.UTF_8);
            double value = Double.parseDouble(str);
            handler.accept(value);
        };
    }
}
