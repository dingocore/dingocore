package com.dingocore.hap.common.codec.tlv;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bob on 8/28/18.
 */
public class MethodRegistry {
    private static Map<Integer,Method> INDEX = new HashMap<>();

    static void register(Method method) {
        INDEX.put(method.getValue(), method);
    }

    static Method lookup(int type) {
        return INDEX.get(type);
    }

}
