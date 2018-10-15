package com.dingocore.endpoint.impl;

import java.util.function.Consumer;

import com.dingocore.TopicNode;
import com.dingocore.endpoint.Property;
import com.dingocore.mqtt.BusConnection;

public abstract class AbstractProperty<T> implements Property<T>, TopicNode {

    public AbstractProperty(DefaultService service, String identifier, String datatype) {
        this(service, identifier, datatype, null);

    }

    public AbstractProperty(DefaultService service, String identifier, String datatype, Consumer<T> handler) {
        this.service = service;
        this.identifier = identifier;
        this.handler = handler;
        getBusConnection().publish($topic("name"), this.identifier, 1, true);
        getBusConnection().publish($topic("datatype"), datatype, 1, true);
        getBusConnection().addListener(topic("set"), wrapHandler(handler));
        if (this.handler != null) {
            getBusConnection().publish($topic("settable"), "true", 1, true);
        }
    }

    @Override
    public String topic() {
        return this.service.topic(this.identifier);
    }

    @Override
    public BusConnection getBusConnection() {
        return this.service.getBusConnection();
    }

    protected abstract Consumer<byte[]> wrapHandler(Consumer<T> handler);

    private final DefaultService service;

    private final String identifier;

    private final Consumer<T> handler;

    private String name;
}
