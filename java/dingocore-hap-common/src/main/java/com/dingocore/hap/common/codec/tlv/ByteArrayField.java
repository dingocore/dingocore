package com.dingocore.hap.common.codec.tlv;

/**
 * Created by bob on 8/27/18.
 */
class ByteArrayField extends Field<byte[]> {

    protected ByteArrayField(Type type, byte[] value) {
        super(type, value);
    }

    @Override
    public int getLength() {
        return getValue().length;
    }

    @Override
    public byte[] getValueAsBytes() {
        return getValue();
    }
}
