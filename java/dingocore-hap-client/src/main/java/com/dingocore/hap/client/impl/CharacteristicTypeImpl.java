package com.dingocore.hap.client.impl;

import java.util.Set;
import java.util.UUID;

import com.dingocore.hap.client.model.impl.PermissionsImpl;
import com.dingocore.hap.client.model.CharacteristicType;
import com.dingocore.hap.client.model.Format;
import com.dingocore.hap.client.model.Permission;
import com.dingocore.hap.client.model.Unit;

/**
 * Created by bob on 9/13/18.
 */
public class CharacteristicTypeImpl implements CharacteristicType {

    public CharacteristicTypeImpl(UUID uuid,
                                  String type,
                                  String name,
                                  Format format,
                                  Unit unit,
                                  Number minimumValue,
                                  Number maximumValue,
                                  Number stepValue,
                                  Set<Permission> permissions) {
        this.uuid = uuid;
        this.type = type;
        this.name = name;
        this.format = format;
        this.unit = unit;
        this.minimumValue = minimumValue;
        this.maximumValue = maximumValue;
        this.stepValue = stepValue;
        this.permissions = new PermissionsImpl(permissions);
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
    public String getName() {
        return this.name;
    }

    @Override
    public PermissionsImpl getPermissions() {
        return this.permissions;
    }

    @Override
    public Format getFormat() {
        return this.format;
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    @Override
    public Number getMinimumValue() {
        return this.minimumValue;
    }

    @Override
    public Number getMaximumValue() {
        return this.maximumValue;
    }

    @Override
    public Number getStepValue() {
        return this.stepValue;
    }

    @Override
    public String toString() {
        return "[CharacteristicType: name=" + this.name + "]";
    }

    private final UUID uuid;
    private final String type;

    private final String name;

    private final PermissionsImpl permissions;

    private final Format format;
    private final Unit unit;

    private Number minimumValue;
    private Number maximumValue;
    private Number stepValue;
}
