package com.dingocore.endpoint;

import java.util.function.Consumer;

import com.dingocore.ServiceType;

public interface Service {

    void setName(String name);

    String getName();

    void setType(ServiceType type);

    ServiceType getType();

    Property<Boolean> addBooleanProperty(String identifier);

    Property<Boolean> addBooleanProperty(String identifier, Consumer<Boolean> handler);

    Property<Double> addFloatProperty(String identifier);

    Property<Double> addFloatProperty(String identifier, Consumer<Double> handler);

}
