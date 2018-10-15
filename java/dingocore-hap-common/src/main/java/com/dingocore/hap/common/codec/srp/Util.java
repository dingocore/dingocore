package com.dingocore.hap.common.codec.srp;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Created by bob on 8/29/18.
 */
public class Util {
    private Util() {

    }
    public static byte[] bigIntegerToUnsignedByteArray(BigInteger i) {
        byte[] array = i.toByteArray();
        if (array[0] == 0) {
            array = Arrays.copyOfRange(array, 1, array.length);
        }
        return array;
    }
}
