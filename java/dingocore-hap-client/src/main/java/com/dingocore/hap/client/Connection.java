package com.dingocore.hap.client;

import java.util.concurrent.ExecutionException;

/**
 * Created by bob on 8/27/18.
 */
public interface Connection {

    default PairedConnection pair(String pin) throws ExecutionException, InterruptedException {
        return pair( new SimplePinSupplier(pin));
    }

    PairedConnection pair(PinSupplier pinSupplier) throws ExecutionException, InterruptedException;

    PairedConnection pairVerify() throws ExecutionException, InterruptedException;
}
