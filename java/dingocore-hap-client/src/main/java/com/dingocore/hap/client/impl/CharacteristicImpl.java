package com.dingocore.hap.client.impl;

import java.util.concurrent.ExecutionException;

import com.dingocore.hap.client.model.Characteristic;
import com.dingocore.hap.client.model.CharacteristicType;
import com.dingocore.hap.client.model.Permission;
import com.dingocore.hap.client.model.Permissions;

public class CharacteristicImpl implements Characteristic {
    public CharacteristicImpl(ServiceImpl service, int iid, CharacteristicType type) {
        this.service = service;
        this.iid = iid;
        this.type = type;
    }

    @Override
    public ServiceImpl getService() {
        return this.service;
    }

    @Override
    public int getIID() {
        return this.iid;
    }

    @Override
    public CharacteristicType getType() {
        return this.type;
    }

    public void setStoredValue(Object value) {
        this.value = value;
    }

    public void updateValue(Object value) {
        System.err.println( "SHOOT AN UPDATE of " + this + " -> " + value);
        try {
            getService().getAccessory().getAccessoriesDB().getPairedConnection().updateValue(this, value);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public <T> T getValue(Class<T> asType) {
        if ( asType.isInstance(this.value)) {
            return asType.cast(this.value);
        }
        throw new RuntimeException("invalid cast: " + this.value + " to " + asType);
    }

    @Override
    public boolean isWritable() {
        return getType().getPermissions().contains(Permission.PAIRED_WRITE);
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    public Permissions getPermissions() {
        return this.permissions;
    }

    @Override
    public String toString() {
        return "[Characteristics: iid=" + this.iid + "; type=" + this.type + "; value=" + this.value + "; perms=" + this.permissions + "]";
    }

    private final int iid;

    private final CharacteristicType type;

    private final ServiceImpl service;

    private Permissions permissions;

    private Object value;
}
