package com.dingocore.hap.client;

import java.io.IOException;

import com.dingocore.hap.common.codec.crypto.Chacha;
import org.junit.Test;

/**
 * Created by bob on 8/28/18.
 */
public class ChaChaTest {


    @Test
    public void test() throws IOException {
        String key = "abcdefgh12345678";
        byte[] keyBytes = key.getBytes();
        System.err.println( "keylen: " + keyBytes.length);
        Chacha chacha = new Chacha(keyBytes);

        String plaintext = "hello world";

        byte[] ciphertext = chacha.newEncoder("PV-Msg03").encodeCiphertext(plaintext.getBytes());

        System.err.println( new String( chacha.newDecoder("PV-Msg03").decodeEncryptedData(ciphertext) ) );

    }
}
