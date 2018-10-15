package com.dingocore.hap.client.impl;

import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import com.dingocore.hap.client.model.EventableCharacteristic;

public class LeafCharacteristicListenerSet extends AbstractListenerSet {

    LeafCharacteristicListenerSet() {
        this(null);
    }

    LeafCharacteristicListenerSet(ListenerSet parent) {
        super( parent );
    }

    public void fireEvent(EventableCharacteristicImpl characteristic) {
        this.listeners.forEach(e->{
            e.accept(characteristic);
        });
    }

    synchronized void addListener(EventableCharacteristicImpl characteristic, Consumer<EventableCharacteristic> listener) throws ExecutionException, InterruptedException {
        this.listeners.add(listener);
        if (this.listeners.size() == 1) {
            characteristic.enableEvents();
        }
    }

    synchronized void removeListener(EventableCharacteristicImpl characteristic, Consumer<EventableCharacteristic> listener) throws ExecutionException, InterruptedException {
        this.listeners.remove(listener);
        if (this.listeners.isEmpty()) {
            characteristic.disableEvents();
        }
    }

    synchronized void removeAllListeners(EventableCharacteristicImpl characteristic) throws ExecutionException, InterruptedException {
        this.listeners.clear();
        characteristic.disableEvents();
    }

}
