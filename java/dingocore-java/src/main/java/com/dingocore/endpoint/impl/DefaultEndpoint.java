package com.dingocore.endpoint.impl;

import com.dingocore.DefaultDingo;
import com.dingocore.State;
import com.dingocore.TopicNode;
import com.dingocore.endpoint.Endpoint;
import com.dingocore.endpoint.Service;
import com.dingocore.mqtt.BusConnection;

import static com.dingocore.State.INIT;
import static com.dingocore.State.READY;

public class DefaultEndpoint implements Endpoint, TopicNode {

    public DefaultEndpoint(DefaultDingo system, String identifier) {
        this.system = system;
        this.identifier = identifier;
    }

    public String topic() {
        return this.system.topic(this.identifier);
    }

    @Override
    public void setName(String name) {
        this.name = name;
        getBusConnection().publish($topic("name"), name, 1, true);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
        getBusConnection().publish($topic("manufacturer"), this.manufacturer, 1, true);
    }

    @Override
    public String getManufacturer() {
        return this.manufacturer;
    }

    @Override
    public void setModel(String model) {
        this.model = model;
        getBusConnection().publish($topic("model"), this.model, 1, true);
    }

    @Override
    public String getModel() {
        return this.model;
    }

    @Override
    public void setHardwareRevision(String hardwareRevision) {
        this.hardwareRevision = hardwareRevision;
        getBusConnection().publish($topic("hw/revision"), this.hardwareRevision, 1, true);
    }

    @Override
    public String getHardwareRevision() {
        return this.hardwareRevision;
    }

    @Override
    public void setFirmwareName(String firmwareName) {
        getBusConnection().publish($topic("fw/name"), this.firmwareName, 1, true);
        this.firmwareName = firmwareName;

    }

    @Override
    public String getFirmwareName() {
        return this.firmwareName;
    }

    @Override
    public void setFirmwareRevision(String firmwareRevision) {
        getBusConnection().publish($topic("fw/revision"), this.firmwareRevision, 1, true);
        this.firmwareRevision = firmwareRevision;

    }

    @Override
    public String getFirmwareRevision() {
        return this.firmwareRevision;
    }

    @Override
    public Service addService(String identifier) {
        return new DefaultService(this, identifier);
    }

    public BusConnection getBusConnection() {
        return this.system.getBusConnection();
    }

    private final DefaultDingo system;

    private final String identifier;

    private String name;

    private String manufacturer;

    private String model;

    private String hardwareRevision;

    private String firmwareName;

    private String firmwareRevision;
}
