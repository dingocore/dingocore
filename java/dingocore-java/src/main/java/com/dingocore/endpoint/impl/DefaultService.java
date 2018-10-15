package com.dingocore.endpoint.impl;

import java.util.function.Consumer;

import com.dingocore.ServiceType;
import com.dingocore.TopicNode;
import com.dingocore.endpoint.Property;
import com.dingocore.endpoint.Service;
import com.dingocore.mqtt.BusConnection;

public class DefaultService implements Service, TopicNode {
    public DefaultService(DefaultEndpoint endpoint, String identifier) {
        this.endpoint = endpoint;
        this.identifier = identifier;
    }

    public String topic() {
        return this.endpoint.topic(this.identifier);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        getBusConnection().publish($topic("name"), this.name, 1, true);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setType(ServiceType type) {
        this.type = type;
        getBusConnection().publish($topic("type"), this.type.getIdentifier(), 1, true);
    }

    @Override
    public ServiceType getType() {
        return this.type;
    }

    @Override
    public Property addBooleanProperty(String identifier) {
        return new BooleanProperty(this, identifier);
    }

    @Override
    public Property addBooleanProperty(String identifier, Consumer<Boolean> handler) {
        return new BooleanProperty(this, identifier, handler);
    }

    @Override
    public Property addFloatProperty(String identifier) {
        return new FloatProperty(this, identifier);
    }

    @Override
    public Property addFloatProperty(String identifier, Consumer<Double> handler) {
        return new FloatProperty(this, identifier, handler);
    }

    public BusConnection getBusConnection() {
        return this.endpoint.getBusConnection();
    }

    private final DefaultEndpoint endpoint;

    private final String identifier;

    private String name;

    private ServiceType type;
}
