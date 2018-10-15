package com.dingocore.hap.client.codec;

import java.util.concurrent.CompletableFuture;

import com.dingocore.hap.client.PinSupplier;
import com.dingocore.hap.client.PairedConnection;

/**
 * Created by bob on 8/27/18.
 */
public class SRPStartRequest {

    public SRPStartRequest(PinSupplier pinSupplier) {
        this.future = new CompletableFuture<>();
        this.pinSupplier = pinSupplier;
    }

    public PinSupplier getPinSupplier() {
        return this.pinSupplier;
    }

    public CompletableFuture<PairedConnection> getFuture() {
        return this.future;
    }

    private final CompletableFuture<PairedConnection> future;
    private final PinSupplier pinSupplier;
}
