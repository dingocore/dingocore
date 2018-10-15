package com.dingocore.hap.common.codec.pair;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import com.dingocore.hap.common.codec.crypto.Chacha;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import org.bouncycastle.util.Pack;

/**
 * Created by bob on 8/30/18.
 */
public class SessionKeys {
    public enum Role {
        ACCESSORY,
        CONTROLLER
    }

    public SessionKeys(byte[] accessoryToControllerKey,
                       byte[] controllerToAccessoryKey) {
        this.accessoryToControllerKey = accessoryToControllerKey;
        this.controllerToAccessoryKey = controllerToAccessoryKey;
    }

    byte[] getAccessoryToControllerKey() {
        return this.accessoryToControllerKey;
    }

    byte[] getControllerToAccessoryKey() {
        return this.controllerToAccessoryKey;
    }

    public SessionKeys forClient() {
        this.role = Role.CONTROLLER;
        return this;
    }

    public SessionKeys forServer() {
        this.role = Role.ACCESSORY;
        return this;
    }

    private byte[] getEncryptionKey() {
        if (this.role == Role.CONTROLLER) {
            return this.controllerToAccessoryKey;
        }
        if (this.role == Role.ACCESSORY) {
            return this.accessoryToControllerKey;
        }
        return null;
    }

    public List<ByteBuf> doEncrypt() throws IOException {
        List<ByteBuf> chunks = new ArrayList<>();

        while (this.encryptAccumulator.readableBytes() > 0) {
            int len = Math.min(this.encryptAccumulator.readableBytes(), 1024);
            ByteBuf chunk = this.encryptAccumulator.alloc().buffer(len);
            this.encryptAccumulator.readBytes(chunk, len);
            chunks.add( encryptChunk( chunk ) );
        }
        return chunks;
    }

    public void encrypt(ByteBuf chunk) {
        this.encryptAccumulator.writeBytes(chunk);
    }

    public ByteBuf encryptChunk(ByteBuf response) throws IOException {

        int offset = 0;

        ByteBuf result = response.alloc().buffer();

        while (response.readableBytes() > 0) {
            short len = (short) Math.min(response.readableBytes() - offset, 0x400);
            ByteBuf lenBytes = response.alloc().buffer(2);
            lenBytes.writeShortLE(len);
            result.writeBytes(lenBytes.duplicate());
            byte[] nonce = Pack.longToLittleEndian(outboundCounter++);

            result.writeBytes(new Chacha(getEncryptionKey()).newEncoder(nonce).encodeCiphertext(response.readBytes(len), lenBytes));
        }

        return result;
    }

    private byte[] getDecryptionKey() {
        if (this.role == Role.CONTROLLER) {
            return this.accessoryToControllerKey;
        }
        if (this.role == Role.ACCESSORY) {
            return this.controllerToAccessoryKey;
        }
        return null;
    }

    public byte[] decrypt(byte[] msg) {
        byte[] mac = new byte[16];
        byte[] ciphertext = new byte[msg.length - 16];
        System.arraycopy(msg, 0, ciphertext, 0, msg.length - 16);
        System.arraycopy(msg, msg.length - 16, mac, 0, 16);
        byte[] additionalData = ByteBuffer.allocate(2).order(ByteOrder.LITTLE_ENDIAN)
                .putShort((short) (msg.length - 16)).array();
        try {
            byte[] nonce = Pack.longToLittleEndian(inboundCounter++);
            return new Chacha(getDecryptionKey()).newDecoder(nonce).decodeCiphertext(mac, additionalData, ciphertext);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ByteBuf> decrypt(ByteBuf msg) {
        List<ByteBuf> out = new ArrayList<>();
        decrypt(msg, msg.alloc(), out);
        return out;
    }

    public void decrypt(ByteBuf msg, ByteBufAllocator alloc, List<ByteBuf> out) {
        if (msg.readableBytes() == 0) {
            return;
        }

        if (this.decryptAccumulator.readableBytes() == 0) {
            this.len = msg.readShortLE();
        }

        int remainingExpected = (this.len + 16) - this.decryptAccumulator.readableBytes();

        if (remainingExpected > 0) {
            if (msg.readableBytes() >= remainingExpected) {
                msg.readBytes(this.decryptAccumulator, remainingExpected);
            } else {
                msg.readBytes(this.decryptAccumulator, msg.readableBytes());
            }
        }

        remainingExpected = (this.len + 16) - this.decryptAccumulator.readableBytes();

        if (remainingExpected > 0) {
            return;
        }

        ByteBuf decryptedChunk = decryptChunk(this.decryptAccumulator, this.len, alloc);
        this.decryptAccumulator.clear();
        this.len = 0;
        out.add(decryptedChunk);
        if (msg.readableBytes() > 0) {
            decrypt(msg, alloc, out);
        }
    }

    protected ByteBuf decryptChunk(ByteBuf msg, int len, ByteBufAllocator alloc) {
        if (msg.readableBytes() == 0) {
            return msg;
        }

        int msgLen = msg.readableBytes();

        ByteBuf ciphertext = alloc.buffer(msgLen - 16);
        ByteBuf mac = alloc.buffer(16);
        ByteBuf additionalData = alloc.buffer(2);

        try {
            msg.readBytes(ciphertext, msgLen - 16);
            msg.readBytes(mac, 16);
            additionalData.writeShortLE(len);
            try {
                byte[] nonce = Pack.longToLittleEndian(inboundCounter++);
                ByteBuf result = alloc.buffer();
                byte[] plaintext = new Chacha(getDecryptionKey()).newDecoder(nonce).decodeCiphertext(mac, additionalData, ciphertext);
                result.writeBytes(plaintext);
                return result;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            mac.release();
            ciphertext.release();
            additionalData.release();
        }
    }

    private ByteBuf encryptAccumulator = Unpooled.buffer(0xFFFF);

    private ByteBuf decryptAccumulator = Unpooled.buffer(0xFFFF);

    private int len = 0;

    private final byte[] controllerToAccessoryKey;

    private final byte[] accessoryToControllerKey;

    private int inboundCounter = 0;

    private int outboundCounter = 0;

    private Role role;
}
