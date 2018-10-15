package com.dingocore.hap.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.dingocore.hap.common.codec.pair.SessionKeys;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.Test;

/**
 * Created by bob on 9/11/18.
 */
public class SessionKeysTest {

    @Test
    public void test() throws IOException {
        String a2c = "1234567812345678";
        String c2a = "8765432187654321";
        SessionKeys k1 = new SessionKeys(a2c.getBytes(), c2a.getBytes()).forClient();
        SessionKeys k2 = new SessionKeys(a2c.getBytes(), c2a.getBytes()).forServer();

        ByteBuf plaintext = Unpooled.buffer();
        plaintext.writeBytes( "hello world this is my tangy sauce in the big old world of the things with lots of text so that hopefully this will break into multiple frames but who knows for sure, right?".getBytes(StandardCharsets.UTF_8));

        //ByteBuf ciphertext = k1.encrypt(plaintext);

        //ByteBuf result = k2.decrypt(ciphertext);
        //System.err.println( result.toString(StandardCharsets.UTF_8));

    }
}
