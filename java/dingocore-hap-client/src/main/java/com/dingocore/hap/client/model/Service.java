package com.dingocore.hap.client.model;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public interface Service {

    Accessory getAccessory();
    int getIID();
    ServiceType getType();
    List<Characteristic> getCharacteristics();
    Optional<Characteristic> findCharacteristic(CharacteristicType type);

    String getName();

    void addListener(Consumer<EventableCharacteristic> listener);
    void removeListener(Consumer<EventableCharacteristic> listener);
    void removeAllListeners();

}
