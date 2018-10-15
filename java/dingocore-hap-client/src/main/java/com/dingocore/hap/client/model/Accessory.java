package com.dingocore.hap.client.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by bob on 9/10/18.
 */
public interface Accessory {
    Accessories getAccessoriesDB();
    int getAID();
    List<Service> getServices();
    Optional<Service> findService(int iid);
    Optional<Characteristic> findCharacteristic(int iid);

    String getManufacturer();
    String getModel();
    String getName();
    String getHardwareRevision();
    String getFirmwareRevision();

    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();

}
