package com.dingocore.hap.client;

/**
 * Created by bob on 8/29/18.
 */
public class SimplePinSupplier implements PinSupplier {

    public SimplePinSupplier(String pin) {
        this.pin = pin;
    }

    public void set(String pin) {
        this.pin = pin;
    }

    @Override
    public String get() {
        return this.pin;
    }

    private String pin;
}
