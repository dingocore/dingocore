package com.dingocore.hap.common.codec.tlv;

/**
 * Created by bob on 8/27/18.
 */
public class EmptyField extends Field<Void> {

    protected EmptyField(Type type) {
        super(type, null);
    }

    @Override
    public int getLength() {
        return 0;
    }

    @Override
    public byte[] getValueAsBytes() {
        return new byte[0];
    }
}
