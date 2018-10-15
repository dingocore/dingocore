package com.dingocore.hap.common.codec.tlv;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Created by bob on 8/27/18.
 */
public class Type<V, F extends Field<V>> {
    private static Map<Integer, Type> INDEX = new HashMap<>();

    public static final Type<Integer, IntegerField> METHOD = new Type<>("METHOD", 0x00, Integer.class, IntegerField.class);

    public static final Type<String, UTF8Field> IDENTIFIER = new Type<>("IDENTIFIER", 0x01, String.class, UTF8Field.class);

    public static final Type<byte[], ByteArrayField> SALT = new Type<>("SALT", 0x02, byte[].class, ByteArrayField.class);

    public static final Type<byte[], ByteArrayField> PUBLIC_KEY = new Type<>("PUBLIC_KEY", 0x03, byte[].class, ByteArrayField.class);

    public static final Type<byte[], ByteArrayField> PROOF = new Type<>("PROOF", 0x04, byte[].class, ByteArrayField.class);

    public static final Type<byte[], ByteArrayField> ENCRYPTED_DATA = new Type<>("ENCRYPTED_DATA", 0x05, byte[].class, ByteArrayField.class);

    public static final Type<Integer, IntegerField> STATE = new Type<>("STATE", 0x06, Integer.class, IntegerField.class);

    public static final Type<Integer, IntegerField> ERROR = new Type<>("ERROR", 0x07, Integer.class, IntegerField.class);

    public static final Type<Integer, IntegerField> RETRY_DELAY = new Type<>("RETRY_DELAY", 0x08, Integer.class, IntegerField.class);

    public static final Type<byte[], ByteArrayField> CERTIFICATE = new Type<>("CERTIFICATE", 0x09, byte[].class, ByteArrayField.class);

    public static final Type<byte[], ByteArrayField> SIGNATURE = new Type<>("SIGNATURE", 0x0A, byte[].class, ByteArrayField.class);

    public static final Type<Integer, IntegerField> PERMISSIONS = new Type<>("PERMISSIONS", 0x0B, Integer.class, IntegerField.class);

    public static final Type<byte[], ByteArrayField> FRAGMENT_DATA = new Type<>("FRAGMENT_DATA", 0x0C, byte[].class, ByteArrayField.class);

    public static final Type<Void, EmptyField> FRAGMENT_LAST = new Type<>("FRAGMENT_LAST", 0x0D, Void.class, EmptyField.class);


    static void register(Type type) {
        INDEX.put(type.getType(), type);
    }

    static Type lookup(int type) {
        return INDEX.get(type);
    }

    private Type(String name, int tag, Class<V> dataType, Class<F> fieldType) {
        this.name = name;
        this.type = tag;
        this.dataType = dataType;
        this.fieldType = fieldType;
        register(this);
    }

    public int getType() {
        return this.type;
    }

    public Class<F> getFieldType() {
        return this.fieldType;
    }

    public Class<V> getDataType() {
        return this.dataType;
    }

    public Optional<V> get(TLV tlv) {
        return tlv.getFieldValue(this, this.fieldType);
    }

    public TLV set(TLV tlv, V value) {
        if (this.fieldType == IntegerField.class) {
            tlv.addField(new IntegerField(this, (Integer) value));
            return tlv;
        }
        if (this.fieldType == UTF8Field.class) {
            tlv.addField(new UTF8Field(this, (String) value));
            return tlv;
        }
        if (this.fieldType == ByteArrayField.class) {
            tlv.addField(new ByteArrayField(this, (byte[]) value));
            return tlv;
        }
        if (this.fieldType == EmptyField.class) {
            tlv.addField(new EmptyField(this));
            return tlv;
        }
        throw new RuntimeException("Unknown field type: " + this.fieldType);
    }

    protected Field newField(byte[] value) {
        if (this.fieldType == IntegerField.class) {
            return new IntegerField(this, value[0]);
        }
        if (this.fieldType == UTF8Field.class) {
            return new UTF8Field(this, new String(value, Charset.forName("UTF8")));
        }
        if (this.fieldType == ByteArrayField.class) {
            return new ByteArrayField(this, value);
        }
        if (this.fieldType == EmptyField.class) {
            return new EmptyField(this);
        }
        throw new RuntimeException("Unknown field type: " + this.fieldType);
    }

    public String toString() {
        return this.name;
    }

    private final int type;

    private final Class<V> dataType;

    private final Class<F> fieldType;

    private final String name;
}
