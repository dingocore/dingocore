package com.dingocore.endpoint;

public interface Endpoint {
    void setName(String name);
    String getName();

    void setManufacturer(String manufacturer);
    String getManufacturer();

    void setModel(String model);
    String getModel();

    void setHardwareRevision(String revision);
    String getHardwareRevision();

    void setFirmwareName(String name);
    String getFirmwareName();

    void setFirmwareRevision(String revision);
    String getFirmwareRevision();

    Service addService(String identifier);
}
