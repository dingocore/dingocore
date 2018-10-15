package com.dingocore.hap.common.codec.tlv;

/**
 * Created by bob on 8/27/18.
 */
class IntegerField extends Field<Integer> {

    protected IntegerField(Type type, int value) {
        super(type, value);
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public byte[] getValueAsBytes() {
        return new byte[] { getValue().byteValue() };
    }
}
