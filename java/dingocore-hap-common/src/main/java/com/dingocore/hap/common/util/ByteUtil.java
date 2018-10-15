package com.dingocore.hap.common.util;

import io.netty.buffer.ByteBuf;

/**
 * Created by bob on 9/11/18.
 */
public class ByteUtil {

    public static String toString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        String s = "";

        for (int i = 0; i < bytes.length; ++i) {
            s += "<" + bytes[i] + ">";
        }
        return s;
    }

    public static String toString(ByteBuf buf) {
        if (buf == null) {
            return "";
        }
        String s = "";
        for (int i = 0; i < buf.readableBytes(); ++i) {
            s += "<" + buf.getByte(buf.readerIndex() + i) + ">";
        }
        return s;

    }
}
