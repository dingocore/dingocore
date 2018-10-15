package com.dingocore.hap.client.codec;

import java.security.SecureRandom;
import java.util.Optional;

import djb.Curve25519;
import com.dingocore.hap.client.auth.AccessoryAuthInfo;
import com.dingocore.hap.client.auth.ClientAuthStorage;
import com.dingocore.hap.common.codec.crypto.Chacha;
import com.dingocore.hap.common.codec.crypto.EdsaSigner;
import com.dingocore.hap.common.codec.crypto.EdsaVerifier;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.TLVError;
import com.dingocore.hap.common.codec.tlv.Type;
import com.dingocore.hap.common.codec.pair.PairVerifyManager;

/**
 * Created by bob on 8/28/18.
 */
public class ClientPairVerifyManager extends PairVerifyManager<ClientAuthStorage> {

    public ClientPairVerifyManager(ClientAuthStorage authStorage) {
        super(authStorage);
    }

    public TLV startRequest(VerifyStartRequest request) {
        this.currentState = 1;

        this.curvePublicKey = new byte[32];
        this.curveSecretKey = new byte[32];

        new SecureRandom().nextBytes(this.curveSecretKey);
        Curve25519.keygen(this.curvePublicKey, null, this.curveSecretKey);

        TLV tlv = new TLV();
        Type.PUBLIC_KEY.set(tlv, this.curvePublicKey);
        Type.STATE.set(tlv, 1);
        return tlv;
    }

    public TLV handle(TLV tlv) throws Exception {
        Optional<Integer> state = Type.STATE.get(tlv);

        if (!state.isPresent()) {
            return error(this.currentState, TLVError.UNKNOWN);
        }

        if (this.currentState == 1 && state.get() == 2) {
            return doVerifyFinishRequest(tlv);
        }
        if (this.currentState == 3 && state.get() == 4) {
            return finish(tlv);
        }

        throw new RuntimeException("incorrect state: " + this.currentState + " vs " + state.get());
    }


    public TLV doVerifyFinishRequest(TLV tlv) throws Exception {
        this.currentState = 3;
        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(tlv);

        if (!publicKey$.isPresent()) {
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        Optional<byte[]> encryptedData$ = Type.ENCRYPTED_DATA.get(tlv);

        if (!encryptedData$.isPresent()) {
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        this.otherCurvePublicKey = publicKey$.get();

        byte[] sharedSecret = getSharedSecret(this.curveSecretKey, this.otherCurvePublicKey);
        byte[] sessionKey = getSessionKey(sharedSecret, PAIR_VERIFY_ENCRYPT_SALT, PAIR_VERIFY_ENCRYPT_INFO);

        byte[] encryptedData = encryptedData$.get();

        Chacha chacha = new Chacha(sessionKey);

        Chacha.Decoder decoder = chacha.newDecoder("PV-Msg02");
        byte[] plaintext = decoder.decodeEncryptedData(encryptedData);

        TLV subtlv = TLV.decodeFrom(plaintext);

        Optional<String> identifier$ = Type.IDENTIFIER.get(subtlv);
        if (!identifier$.isPresent()) {
            return error(this.currentState, TLVError.UNKNOWN);
        }

        Optional<byte[]> signature = Type.SIGNATURE.get(subtlv);
        if (!signature.isPresent()) {
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        AccessoryAuthInfo accessoryAuthInfo = getAuthStorage().get(identifier$.get());
        if (accessoryAuthInfo == null) {
            return error(this.currentState, TLVError.UNKNOWN);
        }

        byte[] accessoryInfo = getAccessoryInfo(this.otherCurvePublicKey, identifier$.get(), this.curvePublicKey);
        EdsaVerifier verifier = new EdsaVerifier(accessoryAuthInfo.getKey());
        if ( ! verifier.verify(accessoryInfo, signature.get()) ) {
            return error(this.currentState, TLVError.AUTHENTICATION);
        }

        byte[] iOSDeviceInfo = getIOSDeviceInfo(this.curvePublicKey, getAuthStorage().getIdentifier(), this.otherCurvePublicKey);
        EdsaSigner signer = new EdsaSigner(getAuthStorage().getLTSK());
        byte[] responseSignature = signer.sign(iOSDeviceInfo);

        TLV responseSubtlv = new TLV();
        Type.IDENTIFIER.set(responseSubtlv, getAuthStorage().getIdentifier());
        Type.SIGNATURE.set(responseSubtlv, responseSignature);


        Chacha.Encoder chachaEncoder = chacha.newEncoder("PV-Msg03");
        byte[] responseCipherText = chachaEncoder.encodeCiphertext(responseSubtlv.encode());

        TLV outboundTlv = new TLV();
        Type.STATE.set(outboundTlv, this.currentState);
        Type.ENCRYPTED_DATA.set(outboundTlv, responseCipherText);

        setKeysForSharedSecret(this.curveSecretKey, this.otherCurvePublicKey);

        //outboundTlv.addAttachment(TLV.HTTP_REQUEST_PATH, "/pair-verify");
        //outboundTlv.addAttachment(TLV.HTTP_METHOD, HttpMethod.POST);
        return outboundTlv;
    }

    public TLV finish(TLV in) {
        // success;
        return null;
    }

    public void reset() {
        super.reset();
        this.curveSecretKey = null;
        this.curvePublicKey = null;
    }

    private byte[] curvePublicKey;

    private byte[] curveSecretKey;

    private byte[] otherCurvePublicKey;
}
