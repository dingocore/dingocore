package com.dingocore.hap.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.dingocore.hap.client.model.CharacteristicType;
import com.dingocore.hap.client.model.Characteristics;
import com.dingocore.hap.client.model.EventableCharacteristic;
import com.dingocore.hap.client.model.Characteristic;
import com.dingocore.hap.client.model.Service;
import com.dingocore.hap.client.model.ServiceType;

public class ServiceImpl implements Service {

    public ServiceImpl(AccessoryImpl accessory, int iid, ServiceType type) {
        this.accessory = accessory;
        this.iid = iid;
        this.type = type;
        this.listeners = new NonLeafCharacteristicListenerSet(accessory.getListeners());
    }

    @Override
    public AccessoryImpl getAccessory() {
        return this.accessory;
    }

    @Override
    public int getIID() {
        return this.iid;
    }

    @Override
    public ServiceType getType() {
        return this.type;
    }

    public void addCharacteristic(Characteristic characteristic) {
        this.characteristics.add(characteristic);
        if (characteristic instanceof EventableCharacteristic) {
            this.listeners.stream().forEach(((EventableCharacteristic) characteristic)::addListener);
        }
    }

    @Override
    public List<Characteristic> getCharacteristics() {
        return this.characteristics;
    }

    @Override
    public Optional<Characteristic> findCharacteristic(CharacteristicType type) {
        return this.characteristics.stream().filter(e->e.getType() == type).findFirst();
    }

    @Override
    public String getName() {
        Optional<Characteristic> chr = findCharacteristic(Characteristics.NAME);
        if ( chr.isPresent() ) {
            return (String) chr.get().getValue();
        }
        return this.type.getName();
    }

    @Override
    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.characteristics.forEach(e -> {
            if (e instanceof EventableCharacteristicImpl) {
                ((EventableCharacteristicImpl) e).addListener(listener);
            }
        });
    }

    @Override
    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.characteristics.forEach(e -> {
            if (e instanceof EventableCharacteristicImpl) {
                ((EventableCharacteristicImpl) e).removeListener(listener);
            }
        });
    }

    @Override
    public void removeAllListeners() {
        this.characteristics.forEach(e -> {
            if (e instanceof EventableCharacteristicImpl) {
                ((EventableCharacteristicImpl) e).removeAllListeners();
            }
        });
    }

    @Override
    public String toString() {
        return "[Service: iid=" + this.iid + "; type=" + this.type + "; characteristics=" + this.characteristics + "]";
    }

    private final int iid;

    private final ServiceType type;

    private final AccessoryImpl accessory;

    private List<Characteristic> characteristics = new ArrayList<>();

    private NonLeafCharacteristicListenerSet listeners;
}
