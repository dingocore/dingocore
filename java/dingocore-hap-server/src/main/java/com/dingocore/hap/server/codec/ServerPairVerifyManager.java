package com.dingocore.hap.server.codec;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.util.Optional;

import djb.Curve25519;
import com.dingocore.hap.common.codec.crypto.Chacha;
import com.dingocore.hap.common.codec.crypto.EdsaSigner;
import com.dingocore.hap.common.codec.crypto.EdsaVerifier;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.TLVError;
import com.dingocore.hap.common.codec.tlv.Type;
import com.dingocore.hap.common.codec.pair.PairVerifyManager;
import com.dingocore.hap.server.auth.ServerAuthStorage;

/**
 * Created by bob on 9/10/18.
 */
public class ServerPairVerifyManager extends PairVerifyManager<ServerAuthStorage> {

    public ServerPairVerifyManager(ServerAuthStorage authStorage) {
        super(authStorage);
    }

    public TLV handle(TLV in) {
        Optional<Integer> state$ = Type.STATE.get(in);
        if (!state$.isPresent()) {
            return error(TLVError.UNKNOWN);
        }

        int state = state$.get();

        if ( this.currentState == 0 && state == 1 ) {
            return doVerifyStartResponse(in);
        } else if ( this.currentState == 2 && state == 3) {
            return doVerifyFinishResponse(in);
        }

        return error(TLVError.UNKNOWN);
    }

    private TLV doVerifyStartResponse(TLV in) {
        this.currentState = 2;

        this.curvePublicKey = new byte[32];
        this.curveSecretKey = new byte[32];

        new SecureRandom().nextBytes(this.curveSecretKey);
        Curve25519.keygen(this.curvePublicKey, null, this.curveSecretKey);

        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(in);

        if (!publicKey$.isPresent()) {
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        this.otherCurvePublicKey = publicKey$.get();

        byte[] sharedSecret = getSharedSecret(this.curveSecretKey, this.otherCurvePublicKey);

        byte[] accessoryInfo = getAccessoryInfo(this.curvePublicKey, getAuthStorage().getPairingID(), publicKey$.get());

        EdsaSigner signer = new EdsaSigner(getAuthStorage().getLTSK());
        byte[] signature = new byte[0];
        try {
            signature = signer.sign(accessoryInfo);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return error(TLVError.UNKNOWN);
        }

        TLV subTlv = new TLV();
        Type.IDENTIFIER.set(subTlv, getAuthStorage().getPairingID());
        Type.SIGNATURE.set(subTlv, signature);

        this.sessionKey = getSessionKey(sharedSecret, PAIR_VERIFY_ENCRYPT_SALT, PAIR_VERIFY_ENCRYPT_INFO);

        Chacha chacha = new Chacha(this.sessionKey);
        Chacha.Encoder encoder = chacha.newEncoder("PV-Msg02");
        byte[] encryptedData = new byte[0];
        try {
            encryptedData = encoder.encodeCiphertext(subTlv.encode());
        } catch (IOException e) {
            return error(TLVError.UNKNOWN);
        }

        TLV out = new TLV();
        Type.STATE.set(out, 2);
        Type.PUBLIC_KEY.set(out, this.curvePublicKey);
        Type.ENCRYPTED_DATA.set(out, encryptedData);
        return out;
    }

    private TLV doVerifyFinishResponse(TLV in) {
        this.currentState = 4;
        Optional<byte[]> encryptedData$ = Type.ENCRYPTED_DATA.get(in);
        if ( ! encryptedData$.isPresent() ) {
            return error(TLVError.AUTHENTICATION);
        }
        Chacha chacha = new Chacha(this.sessionKey);
        Chacha.Decoder decoder = chacha.newDecoder("PV-Msg03");
        TLV subTlv = null;
        try {
            subTlv = TLV.decodeFrom(decoder.decodeEncryptedData(encryptedData$.get()));
        } catch (IOException e) {
            return error(TLVError.AUTHENTICATION);
        }

        Optional<String> identifier$ = Type.IDENTIFIER.get(subTlv);
        if ( ! identifier$.isPresent() ) {
            return error(TLVError.AUTHENTICATION);
        }
        Optional<byte[]> signature$ = Type.SIGNATURE.get(subTlv);
        if ( ! signature$.isPresent() ) {
            return error(TLVError.AUTHENTICATION);
        }

        byte[] ltpk = getAuthStorage().getPairedLTPK(identifier$.get());

        byte[] iosDeviceInfo = getIOSDeviceInfo(this.otherCurvePublicKey, identifier$.get(), this.curvePublicKey);

        EdsaVerifier verifier = new EdsaVerifier(ltpk);
        try {
            if ( ! verifier.verify(iosDeviceInfo, signature$.get()) ) {
                return error(TLVError.AUTHENTICATION);
            }
        } catch (Exception e) {
            return error(TLVError.AUTHENTICATION);
        }

        TLV out = new TLV();
        Type.STATE.set(out, 4);

        setKeysForSharedSecret(this.curveSecretKey, this.otherCurvePublicKey);

        return out;
    }

    public void reset() {
        super.reset();
        this.currentState = 0;
        this.curvePublicKey = null;
        this.curveSecretKey = null;
    }

    private byte[] curvePublicKey;
    private byte[] curveSecretKey;
    private byte[] sessionKey;

    private byte[] otherCurvePublicKey;
}
