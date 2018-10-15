package com.dingocore.hap.client.codec;

import java.util.concurrent.CompletableFuture;

import com.dingocore.hap.client.model.Characteristic;

public class CharacteristicEventsRequest implements SyncRequest {

    public CharacteristicEventsRequest(Characteristic characteristic, boolean enable) {
        this.characteristic = characteristic;
        this.enable = enable;
        this.future = new CompletableFuture<>();
    }

    public CompletableFuture<Object> getFuture() {
        return this.future;
    }

    public Characteristic getCharacteristic() {
        return this.characteristic;
    }

    public boolean isEnable() {
        return this.enable;
    }

    public boolean isDisable() {
        return ! this.enable;
    }

    public boolean getValue() {
        return this.enable;
    }

    private final Characteristic characteristic;
    private final boolean enable;

    private final CompletableFuture<Object> future;
}
