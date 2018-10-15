package com.dingocore.hap.client.model.impl;

import java.util.Set;
import java.util.UUID;

import com.dingocore.hap.client.model.ServiceType;
import com.dingocore.hap.client.model.CharacteristicType;

public class ServiceTypeImpl implements ServiceType {

    public ServiceTypeImpl(String name, UUID uuid, String type, Set<CharacteristicType> requiredCharacteristics, Set<CharacteristicType> optionalCharacteristics) {
        this.name = name;
        this.uuid = uuid;
        this.type = type;
        this.requiredCharacteristics = requiredCharacteristics;
        this.optionalCharacteristics = optionalCharacteristics;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public String getType() {
        return this.type;
    }

    @Override
    public Set<CharacteristicType> getRequiredCharacteristics() {
        return this.requiredCharacteristics;
    }

    @Override
    public Set<CharacteristicType> getOptionalCharacteristics() {
        return this.optionalCharacteristics;
    }

    @Override
    public String toString() {
        return "[ServiceType: name=" + this.name + "; type=" + this.type + "]";
    }

    private final String name;

    private final UUID uuid;

    private final String type;

    private final Set<CharacteristicType> requiredCharacteristics;

    private final Set<CharacteristicType> optionalCharacteristics;
}
