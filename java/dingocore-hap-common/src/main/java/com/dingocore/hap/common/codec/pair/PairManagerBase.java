
package com.dingocore.hap.common.codec.pair;

import java.nio.charset.StandardCharsets;

import djb.Curve25519;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.TLVError;
import com.dingocore.hap.common.codec.tlv.Type;
import com.dingocore.hap.common.spi.AuthStorage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.HKDFBytesGenerator;
import org.bouncycastle.crypto.params.HKDFParameters;

/**
 * Created by bob on 8/29/18.
 */
public abstract class PairManagerBase<T extends AuthStorage> {

    protected PairManagerBase(T authStorage) {
        this.authStorage = authStorage;
    }

    public byte[] getSessionKey(byte[] sharedSecret, byte[] salt, byte[] info) {
        HKDFBytesGenerator hkdf = new HKDFBytesGenerator(new SHA512Digest());
        hkdf.init(new HKDFParameters(sharedSecret,
                                     salt,
                                     info));
        byte[] sessionKey = new byte[32];
        hkdf.generateBytes(sessionKey, 0, 32);
        return sessionKey;
    }

    public byte[] getSharedSecret(byte[] myPrivateKey, byte[] otherPublicKey) {
        byte[] sharedSecret = new byte[32];
        Curve25519.curve(sharedSecret, myPrivateKey, otherPublicKey);
        return sharedSecret;
    }

    public byte[] getSharedSecret() {
        return getSharedSecret(this.myPrivateKey, this.otherPublicKey);
    }

    protected void setKeysForSharedSecret(byte[] myPrivateKey, byte[] otherPublicKey) {
        this.myPrivateKey = myPrivateKey;
        this.otherPublicKey = otherPublicKey;
    }

    public SessionKeys getSessionKeys() {
        return new SessionKeys(
                getSessionKey(getSharedSecret(),
                              "Control-Salt".getBytes(StandardCharsets.UTF_8),
                              "Control-Read-Encryption-Key".getBytes(StandardCharsets.UTF_8)),
                getSessionKey(getSharedSecret(),
                              "Control-Salt".getBytes(StandardCharsets.UTF_8),
                              "Control-Write-Encryption-Key".getBytes(StandardCharsets.UTF_8))
        );
    }

    protected TLV error(TLVError error) {
        TLV tlv = new TLV();
        Type.STATE.set(tlv, this.currentState);
        Type.ERROR.set(tlv, error.getValue());
        return tlv;
    }

    protected TLV error(int state, TLVError error) {
        TLV tlv = new TLV();
        Type.STATE.set(tlv, state);
        Type.ERROR.set(tlv, error.getValue());
        return tlv;
    }



    protected byte[] getAccessoryInfo(byte[] accessoryX, String accessoryPairingID, byte[] accessoryLTPK) {
        ByteBuf buf = Unpooled.buffer();
        try {
            buf.writeBytes(accessoryX);
            buf.writeBytes(accessoryPairingID.getBytes(StandardCharsets.UTF_8));
            buf.writeBytes(accessoryLTPK);
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return bytes;
        } finally {
            buf.release();
        }
    }

    protected byte[] getIOSDeviceInfo(byte[] iOSDeviceX, String iOSDevicePairingID, byte[] iOSDeviceLTPK) {
        ByteBuf buf = Unpooled.buffer();
        try {
            buf.writeBytes(iOSDeviceX);
            buf.writeBytes(iOSDevicePairingID.getBytes(StandardCharsets.UTF_8));
            buf.writeBytes(iOSDeviceLTPK);
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            return bytes;
        } finally {
            buf.release();
        }
    }

    public boolean isActive() {
        return this.currentState != 0;
    }

    public void reset() {
        this.currentState = 0;
    }

    public T getAuthStorage() {
        return this.authStorage;
    }

    private final T authStorage;

    protected int currentState;

    private byte[] myPrivateKey;

    private byte[] otherPublicKey;
}
