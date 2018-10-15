package com.dingocore.hap.server.auth.simple;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.dingocore.hap.server.auth.ServerAuthStorage;
import net.i2p.crypto.eddsa.spec.EdDSANamedCurveTable;
import net.i2p.crypto.eddsa.spec.EdDSAParameterSpec;

/**
 * Created by bob on 8/29/18.
 */
public class SimpleServerAuthStorage implements ServerAuthStorage {

    public SimpleServerAuthStorage(String pin, String pairingID) {
        this.pin = pin;
        this.pairingID = pairingID;
    }

    @Override
    public String getPairingID() {
        return this.pairingID;
    }

    @Override
    public String getPIN() {
        return this.pin;
    }

    @Override
    public synchronized byte[] getLTSK() {
        if (this.ltsk == null) {
            EdDSAParameterSpec spec = EdDSANamedCurveTable.getByName("ed25519-sha-512");
            byte[] seed = new byte[spec.getCurve().getField().getb() / 8];
            new SecureRandom().nextBytes(seed);
            this.ltsk = seed;
        }
        return this.ltsk;
    }

    public void setLTSK(byte[] ltsk) {
        this.ltsk = ltsk;
    }

    public void setLTSK(String ltskBase64) {
        setLTSK(Base64.getDecoder().decode(ltskBase64));
    }

    @Override
    public void addPairing(String identifier, byte[] ltpk) {
        this.pairings.put(identifier, ltpk);
    }

    public void addPairing(String identifier, String ltpkBase64) {
        addPairing(identifier, Base64.getDecoder().decode(ltpkBase64));
    }

    public byte[] getPairedLTPK(String identifier) {
        return this.pairings.get(identifier);
    }

    private final String pairingID;

    private final String pin;

    private byte[] ltsk;

    private Map<String,byte[]> pairings = new HashMap<>();
}
