package com.dingocore.hap.common.codec.tlv;

/**
 * Created by bob on 8/27/18.
 */
class UTF8Field extends Field<String> {

    protected UTF8Field(Type type, String value) {
        super(type, value);
    }

    @Override
    public int getLength() {
        return getValueAsBytes().length;
    }

    @Override
    public byte[] getValueAsBytes() {
        return getValue().getBytes();
    }

}
