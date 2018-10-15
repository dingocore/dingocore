package com.dingocore.hap.client.codec;

import java.util.concurrent.CompletableFuture;

public interface SyncRequest {

    CompletableFuture<Object> getFuture();
}
