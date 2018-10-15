package com.dingocore.hap.server.auth;

import com.dingocore.hap.common.spi.AuthStorage;

/**
 * Created by bob on 8/29/18.
 */
public interface ServerAuthStorage extends AuthStorage {

    String getPairingID();
    String getPIN();

    void addPairing(String identifier, byte[] ltpk);
    byte[] getPairedLTPK(String identifier);
}
