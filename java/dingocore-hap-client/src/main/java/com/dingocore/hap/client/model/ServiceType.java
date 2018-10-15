package com.dingocore.hap.client.model;

import java.util.Set;
import java.util.UUID;

public interface ServiceType {
    String getName();
    UUID getUUID();
    String getType();
    Set<CharacteristicType> getRequiredCharacteristics();
    Set<CharacteristicType> getOptionalCharacteristics();

}
