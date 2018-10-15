package com.dingocore.hap.client.impl;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.dingocore.hap.client.model.CharacteristicType;
import com.dingocore.hap.client.model.EventableCharacteristic;

public class EventableCharacteristicImpl extends CharacteristicImpl implements EventableCharacteristic {

    public EventableCharacteristicImpl(ServiceImpl service, int iid, CharacteristicType type) {
        super(service, iid, type);
        this.listeners = new LeafCharacteristicListenerSet();
    }

    @Override
    public void addListener(Consumer<EventableCharacteristic> listener) {
        try {
            this.listeners.addListener(this, listener);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeListener(Consumer<EventableCharacteristic> listener) {
        try {
            this.listeners.removeListener(this, listener);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeAllListeners() {
        try {
            this.listeners.removeAllListeners(this);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void fireEvent(Object newValue) {
        setStoredValue(newValue);
        this.listeners.fireEvent(this);
    }

    void enableEvents() throws ExecutionException, InterruptedException {
        getService().getAccessory().getAccessoriesDB().getPairedConnection().enableEvents(this);
    }

    void disableEvents() throws ExecutionException, InterruptedException {
        getService().getAccessory().getAccessoriesDB().getPairedConnection().disableEvents(this);
    }

    @Override
    public String toString() {
        return "[EventableCharacteristic: " + super.toString() + "]";
    }

    private LeafCharacteristicListenerSet listeners;

}
