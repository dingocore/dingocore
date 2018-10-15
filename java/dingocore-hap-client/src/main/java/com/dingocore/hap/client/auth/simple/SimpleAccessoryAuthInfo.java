package com.dingocore.hap.client.auth.simple;

import java.util.Base64;

import com.dingocore.hap.client.auth.AccessoryAuthInfo;

/**
 * Created by bob on 8/28/18.
 */
public class SimpleAccessoryAuthInfo implements AccessoryAuthInfo {

    public SimpleAccessoryAuthInfo(String identifier, String pin, byte[] key) {
        this.identifier = identifier;
        this.pin = pin;
        this.key = key;
    }

    public SimpleAccessoryAuthInfo(String identifier, String pin, String base64key) {
        this(identifier, pin, Base64.getDecoder().decode(base64key));
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public String getPin() {
        return this.pin;
    }

    @Override
    public byte[] getKey() {
        return this.key;
    }

    private final String identifier;

    private final String pin;

    private final byte[] key;
}
