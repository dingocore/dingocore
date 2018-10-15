package com.dingocore.hap.client;

import com.dingocore.hap.client.auth.ClientAuthStorage;

public class ActiveClient {

    public ActiveClient(ClientAuthStorage authStorage) {
        this.authStorage = authStorage;
        this.client = new SimpleClient(authStorage);
    }

    private final ClientAuthStorage authStorage;

    private final SimpleClient client;
}
