package com.dingocore.hap.client.auth.simple;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.dingocore.hap.client.auth.AbstractClientAuthStorage;
import com.dingocore.hap.client.auth.AccessoryAuthInfo;

/**
 * Created by bob on 8/28/18.
 */
public class SimpleClientAuthStorage extends AbstractClientAuthStorage {

    public SimpleClientAuthStorage(String identifier) {
        this(identifier, (byte[]) null);

    }
    public SimpleClientAuthStorage(String identifier, String ltskBase64) {
        this(identifier,
             Base64.getDecoder().decode(ltskBase64));
    }

    public SimpleClientAuthStorage(String identifier, byte[] ltsk) {
        super(identifier);
        this.ltsk = ltsk;
    }

    @Override
    public synchronized byte[] getLTSK() {
        if (this.ltsk == null) {
            this.ltsk = generateLTSK();
        }
        return this.ltsk;
    }

    @Override
    public AccessoryAuthInfo get(String identifier) {
        return storage.get(identifier);
    }

    @Override
    public void put(String identifier, String pin, byte[] ltpk) {
        this.storage.put(identifier, createAccessoryAuthInfo(identifier, pin, ltpk));
    }

    protected AccessoryAuthInfo createAccessoryAuthInfo(String identifier, String pin, byte[] ltpk) {
        return new SimpleAccessoryAuthInfo(identifier, pin, ltpk);
    }

    public void put(String identifier, String pin, String ltpkBase64) {
        put(identifier, pin, Base64.getDecoder().decode(ltpkBase64));
    }

    private byte[] ltsk;

    private Map<String, AccessoryAuthInfo> storage = new HashMap<>();
}
