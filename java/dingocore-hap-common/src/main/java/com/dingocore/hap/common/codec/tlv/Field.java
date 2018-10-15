package com.dingocore.hap.common.codec.tlv;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Created by bob on 8/27/18.
 */
public abstract class Field<T> {

    public Field(Type type, T value) {
        this.type = type;
        this.value = value;
    }

    public Type getType() {
        return this.type;
    }

    public abstract int getLength();

    public abstract byte[] getValueAsBytes();

    T getValue() {
        return value;
    }

    public void encodeInto(ByteBuf buf) {
        ByteBuf tmp = Unpooled.buffer();
        try {
            tmp.writeBytes(getValueAsBytes());

            int len = tmp.readableBytes();

            while (len > 0) {
                buf.writeByte(this.type.getType());
                int fragmentSize = tmp.readableBytes() > 255 ? 255 : tmp.readableBytes();
                buf.writeByte(fragmentSize);
                buf.writeBytes(tmp, fragmentSize);
                len = len - fragmentSize;
            }
        } finally {
            tmp.release();
        }
    }

    public String toString() {
        return "[" + this.type + "; length=" + getLength() + "; value=" + getValue() + "]";

    }

    private final Type type;
    private final T value;
}
