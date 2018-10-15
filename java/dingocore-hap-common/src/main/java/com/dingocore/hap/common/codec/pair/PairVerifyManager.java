package com.dingocore.hap.common.codec.pair;

import java.nio.charset.StandardCharsets;

import com.dingocore.hap.common.spi.AuthStorage;

/**
 * Created by bob on 8/30/18.
 */
public abstract class PairVerifyManager<T extends AuthStorage> extends PairManagerBase<T> {
    protected static final byte[] PAIR_VERIFY_ENCRYPT_SALT = "Pair-Verify-Encrypt-Salt".getBytes(StandardCharsets.UTF_8);
    protected static final byte[] PAIR_VERIFY_ENCRYPT_INFO = "Pair-Verify-Encrypt-Info".getBytes(StandardCharsets.UTF_8);
    protected PairVerifyManager(T authStorage) {
        super(authStorage);
    }
}
