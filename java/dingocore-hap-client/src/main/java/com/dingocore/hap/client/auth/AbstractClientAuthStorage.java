package com.dingocore.hap.client.auth;

import java.security.SecureRandom;

import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;

/**
 * Created by bob on 8/28/18.
 */
public abstract class AbstractClientAuthStorage implements ClientAuthStorage {

    public AbstractClientAuthStorage(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    protected byte[] generateLTSK() {
        byte[] k = null;
        EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
        byte[] seed = new byte[spec.getCurve().getField().getb() / 8];
        new SecureRandom().nextBytes(seed);
        k = seed;
        return k;
    }

    private String identifier;
}

