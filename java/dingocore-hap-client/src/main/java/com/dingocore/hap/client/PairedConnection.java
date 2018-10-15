package com.dingocore.hap.client;

import java.util.concurrent.ExecutionException;

import com.dingocore.hap.client.model.Accessories;

/**
 * Created by bob on 8/30/18.
 */
public interface PairedConnection {
    Accessories accessories() throws ExecutionException, InterruptedException;
}
