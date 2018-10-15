package com.dingocore.hap.common.codec.tlv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.dingocore.hap.common.codec.AttachmentKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpMethod;

/**
 * Created by bob on 8/27/18.
 */
public class TLV {

    public static final AttachmentKey<HttpMethod> HTTP_METHOD = new AttachmentKey<>();
    public static final AttachmentKey<String> HTTP_REQUEST_PATH = new AttachmentKey<>();

    public static final AttachmentKey<FullHttpRequest> HTTP_REQUEST = new AttachmentKey<>();
    public static final AttachmentKey<FullHttpResponse> HTTP_RESPONSE = new AttachmentKey<>();


    public TLV() {

    }

    public <T> void addAttachment(AttachmentKey<T> key, T value) {
        this.attachments.put(key, value);
    }

    public <T> T getAttachment(AttachmentKey<T> key) {
        return (T) this.attachments.get(key);
    }

    public <T> Object getAttachment(String key, Class<T> asType) {
        return asType.cast(this.attachments.get(key));
    }

    public TLV addMethod(Method method) {
        addField(new IntegerField(Type.METHOD, method.getValue()));
        return this;
    }

    public Optional<Method> getMethod() {
        Optional<Integer> value = Type.METHOD.get(this);
        return value.map(Method::lookup);
    }

    public TLV addPublicKey(byte[] publicKey) {
        addField(new ByteArrayField(Type.PUBLIC_KEY, publicKey));
        return this;
    }

    public Optional<byte[]> getPublicKey() {
        return Type.PUBLIC_KEY.get(this);
    }

    public TLV addState(int state) {
        addField(new IntegerField(Type.STATE, state));
        return this;
    }

    public Optional<Integer> getState() {
        return Type.STATE.get(this);
    }

    public TLV addEncryptedData(byte[] data) {
        addField(new ByteArrayField(Type.ENCRYPTED_DATA, data));
        return this;
    }

    public Optional<byte[]> getEncryptedData() {
        return Type.ENCRYPTED_DATA.get(this);
    }

    public TLV addField(Field field) {
        this.fields.add(field);
        return this;
    }

    public List<Field> getFields() {
        return this.fields;
    }

    Optional<Field> getField(Type type) {
        return this.fields.stream().filter(e -> e.getType() == type).findFirst();
    }

    <T extends Field> Optional<T> getField(Type type, Class<T> fieldType) {
        Optional<Field> field = getField(type);
        return field.map(fieldType::cast);

    }

     <T,J extends Field<T>> Optional<T> getFieldValue(Type type, Class<J> fieldType) {
        Optional<J> field = getField(type, fieldType);
        return field.map(Field::getValue);

    }

    public void encodeInto(ByteBuf buf) {
        for (Field field : fields) {
            field.encodeInto(buf);
        }
    }

    public byte[] encode() {
        ByteBuf buf = Unpooled.buffer();
        try {
            encodeInto(buf);
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return bytes;
        } finally {
            buf.release();
        }
    }

    public static TLV decodeFrom(byte[] bytes) {
        return decodeFrom( bytes, 0, bytes.length );
    }
    public static TLV decodeFrom(byte[] bytes, int offset, int length) {
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(bytes, offset, length);
        try {
            return decodeFrom(buf);
        } finally {
            buf.release();
        }
    }

    public static TLV decodeFrom(ByteBuf buf) {
        TLV tlv = new TLV();

        ByteBuf tmp = Unpooled.buffer(1024 * 8);
        boolean fragmented = false;
        try {
            while (buf.readableBytes() > 0) {
                byte typeByte = buf.readByte();
                Type type = Type.lookup(typeByte);
                if (type == null) {
                    throw new RuntimeException("Unknown type: " + typeByte);
                }

                short length = buf.readUnsignedByte();

                buf.readBytes(tmp, length);
                if ( length == 255 ) {
                    // possible fragmentation
                    byte nextType = buf.getByte(buf.readerIndex());
                    if ( nextType == typeByte ) {
                        fragmented = true;
                    } else {
                        fragmented = false;
                    }
                } else {
                    fragmented = false;
                }
                if ( ! fragmented ) {
                    byte[] value = new byte[tmp.readableBytes()];
                    tmp.readBytes(value);
                    Field field = type.newField(value);
                    tlv.addField(field);
                    tmp.resetReaderIndex();
                    tmp.resetWriterIndex();
                }
            }
        } finally {
            tmp.release();
        }

        return tlv;
    }

    public String toString() {
        return this.fields.stream().map(e->e.toString()).collect(Collectors.joining());
    }

    private List<Field> fields = new ArrayList<>();

    private Map<AttachmentKey<?>, Object> attachments = new HashMap<>();
}
