package com.dingocore.hap.common.codec.tlv;

/**
 * Created by bob on 8/27/18.
 */
public enum Method {
    RESERVED(0),
    PAIR_SETUP(1),
    PAIR_VERIFY(2),
    ADD_PAIRING(3),
    REMOVE_PAIRING(4),
    LIST_PAIRINGS(5);

    private static void register(Method method) {
        MethodRegistry.register(method);
    }

    public static Method lookup(int method) {
        return MethodRegistry.lookup(method);
    }

    Method(int value) {
        this.value = value;
        register(this);
    }

    public int getValue() {
        return this.value;
    }

    private final int value;
}
