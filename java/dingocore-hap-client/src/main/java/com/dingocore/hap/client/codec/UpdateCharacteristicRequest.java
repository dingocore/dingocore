package com.dingocore.hap.client.codec;

import java.util.concurrent.CompletableFuture;

import com.dingocore.hap.client.model.Characteristic;

public class UpdateCharacteristicRequest implements SyncRequest {

    public UpdateCharacteristicRequest(Characteristic characteristic, Object value) {
        this.characteristic = characteristic;
        this.value = value;
        this.future = new CompletableFuture<>();
    }

    public Characteristic getCharacteristic() {
        return this.characteristic;
    }

    public Object getValue() {
        return this.value;
    }

    @Override
    public CompletableFuture<Object> getFuture() {
        return this.future;
    }

    private final Characteristic characteristic;

    private final Object value;

    private final CompletableFuture<Object> future;
}
