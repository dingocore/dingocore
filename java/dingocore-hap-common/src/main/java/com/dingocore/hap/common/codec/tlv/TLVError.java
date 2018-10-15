package com.dingocore.hap.common.codec.tlv;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bob on 8/29/18.
 */
public class TLVError extends Exception {

    private static final Map<Integer,TLVError> INDEX = new HashMap<>();

    public static final TLVError UNKNOWN = new TLVError(0x01, "Unknown");
    public static final TLVError AUTHENTICATION = new TLVError(0x02, "Authentication");
    public static final TLVError BACKOFF = new TLVError(0x03, "Backoff");
    public static final TLVError MAX_PEERS = new TLVError(0x04, "Max Peers");
    public static final TLVError MAX_TRIES = new TLVError(0x05, "Max Tries");
    public static final TLVError UNAVAILABLE = new TLVError(0x06, "Unavailable");
    public static final TLVError BUSY = new TLVError(0x07, "Busy");


    private static void register(TLVError error) {
        INDEX.put(error.getValue(), error);
    }

    public static TLVError lookup(int value){
        return INDEX.get(value);
    }

    private TLVError(int value, String name) {
        this.value = value;
        this.name = name;
        register(this);
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "Error(" + this.value + ") " + this.name;
    }

    private final int value;

    private final String name;
}
