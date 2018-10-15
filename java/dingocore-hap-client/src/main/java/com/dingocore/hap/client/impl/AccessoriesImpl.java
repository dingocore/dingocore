package com.dingocore.hap.client.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.dingocore.hap.client.model.Accessories;
import com.dingocore.hap.client.model.Accessory;
import com.dingocore.hap.client.model.EventableCharacteristic;

public class AccessoriesImpl implements Accessories {

    public AccessoriesImpl(PairedConnectionImpl pairedConnection) {
        this.pairedConnection = pairedConnection;
    }

    public PairedConnectionImpl getPairedConnection() {
        return this.pairedConnection;
    }

    @Override
    public Optional<Accessory> find(int aid) {
        return this.accessories.stream().filter(e -> e.getAID() == aid).findFirst();
    }

    public void addAccessory(Accessory accessory) {
        this.accessories.add(accessory);
        this.listeners.stream().forEach(accessory::addListener);
    }

    @Override
    public List<Accessory> accessories() {
        return this.accessories;
    }

    public void addListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.addListener( listener );
        this.accessories.forEach(e->{
            e.addListener(listener);

        });
    }

    public void removeListener(Consumer<EventableCharacteristic> listener) {
        this.listeners.removeListener( listener );
        this.accessories.forEach( e->{
            e.removeListener(listener);
        });
    }

    public void removeAllListeners() {
        this.listeners.removeAllListeners();
        this.accessories.forEach( e->{
            e.removeAllListeners();
        });
    }

    public ListenerSet getListeners() {
        return listeners;
    }

    public String toString() {
        return "[Accessories " + this.accessories + "]";
    }

    private final PairedConnectionImpl pairedConnection;

    private List<Accessory> accessories = new ArrayList<>();

    private NonLeafCharacteristicListenerSet listeners = new NonLeafCharacteristicListenerSet();

}
