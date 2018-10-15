package com.dingocore.hap.client.codec;

import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Optional;

import com.nimbusds.srp6.SRP6ClientCredentials;
import com.nimbusds.srp6.SRP6ClientSession;
import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6Exception;
import com.nimbusds.srp6.SRP6Session;
import com.nimbusds.srp6.XRoutineWithUserIdentity;
import com.dingocore.hap.client.PinSupplier;
import com.dingocore.hap.client.auth.ClientAuthStorage;
import com.dingocore.hap.common.codec.crypto.Chacha;
import com.dingocore.hap.common.codec.crypto.EdsaSigner;
import com.dingocore.hap.common.codec.crypto.EdsaVerifier;
import com.dingocore.hap.common.codec.pair.PairSetupManager;
import com.dingocore.hap.common.codec.tlv.Method;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.tlv.TLVError;
import com.dingocore.hap.common.codec.tlv.Type;
import com.dingocore.hap.common.codec.srp.ClientEvidenceRoutineImpl;
import com.dingocore.hap.common.codec.srp.ServerEvidenceRoutineImpl;
import com.dingocore.hap.common.codec.srp.Util;

/**
 * Created by bob on 8/29/18.
 */
public class ClientPairSetupManager extends PairSetupManager<ClientAuthStorage> {

    public ClientPairSetupManager(ClientAuthStorage authStorage) {
        super(authStorage);
    }

    public void reset() {
        super.reset();
        this.currentState = 0;
        this.session = null;
        this.pinSupplier = null;
    }

    public TLV startRequest(SRPStartRequest request) {
        this.pinSupplier = request.getPinSupplier();
        this.currentState = 1;
        TLV out = new TLV();
        Type.STATE.set(out, 1);
        Type.METHOD.set(out, Method.PAIR_SETUP.getValue());
        return out;
    }

    @Override
    public TLV handle(TLV in) {
        Optional<Integer> state$ = Type.STATE.get(in);
        if (!state$.isPresent()) {
            return error(TLVError.UNKNOWN);
        }

        int state = state$.get();

        if (this.currentState == 1 && state == 2) {
            return doSRPVerifyRequest(in);
        }
        if (this.currentState == 3 && state == 4) {
            return doExchangeRequest(in);
        }
        if (this.currentState == 5 && state == 6) {
            return doVerification(in);
        }

        return Type.STATE.set(error(TLVError.UNKNOWN), state$.get() + 1);
    }


    private TLV doSRPVerifyRequest(TLV in) {
        this.currentState = 3;
        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(in);
        if (!publicKey$.isPresent()) {
            throw new RuntimeException("no public key");
        }
        Optional<byte[]> salt$ = Type.SALT.get(in);
        if (!salt$.isPresent()) {
            throw new RuntimeException("no salt");
        }

        SRP6CryptoParams config = new SRP6CryptoParams(N_3072, G, "SHA-512");
        this.session = new SRP6ClientSession(0);
        this.session.setXRoutine(new XRoutineWithUserIdentity());
        this.session.setClientEvidenceRoutine(new ClientEvidenceRoutineImpl());
        this.session.setServerEvidenceRoutine(new ServerEvidenceRoutineImpl());

        //SRP6VerifierGenerator verifierGenerator = new SRP6VerifierGenerator(config);

        this.session.step1(IDENTIFIER, this.pinSupplier.get());

        SRP6ClientCredentials creds = null;
        try {
            creds = this.session.step2(config,
                                       new BigInteger(1, salt$.get()),
                                       new BigInteger(1, publicKey$.get()));
        } catch (SRP6Exception e) {
            return error(TLVError.AUTHENTICATION);
        }

        TLV out = new TLV();

        Type.STATE.set(out, 3);
        Type.PUBLIC_KEY.set(out, Util.bigIntegerToUnsignedByteArray(creds.A));
        Type.PROOF.set(out, Util.bigIntegerToUnsignedByteArray(creds.M1));

        return out;
    }

    private TLV doExchangeRequest(TLV in) {
        this.currentState = 5;

        Optional<byte[]> proof$ = Type.PROOF.get(in);
        if (!proof$.isPresent()) {
            return Type.STATE.set(error(TLVError.AUTHENTICATION), 5);
        }

        try {
            this.session.step3(new BigInteger(1, proof$.get()));
        } catch (SRP6Exception e) {
            return error(TLVError.AUTHENTICATION);
        }

        // response

        EdsaSigner signer = new EdsaSigner(getAuthStorage().getLTSK());
        byte[] iOSDeviceX = getSessionKey(getSRPSessionKey(),
                                          PAIR_SETUP_CONTROLLER_SALT,
                                          PAIR_SETUP_CONTROLLER_INFO);

        byte[] iOSDeviceInfo = getIOSDeviceInfo(iOSDeviceX, getAuthStorage().getIdentifier(), signer.getPublicKey());

        byte[] signature = new byte[0];
        try {
            signature = signer.sign(iOSDeviceInfo);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            return error(TLVError.AUTHENTICATION);
        }

        TLV subTlv = new TLV();
        Type.IDENTIFIER.set(subTlv, getAuthStorage().getIdentifier());
        Type.PUBLIC_KEY.set(subTlv, signer.getPublicKey());
        Type.SIGNATURE.set(subTlv, signature);

        byte[] chachaKey = getSessionKey(getSRPSessionKey(),
                                         PAIR_SETUP_ENCRYPT_SALT, PAIR_SETUP_ENCRYPT_INFO);

        Chacha chacha = new Chacha(chachaKey);

        Chacha.Encoder encoder = chacha.newEncoder("PS-Msg05");
        byte[] encryptedData = null;
        try {
            encryptedData = encoder.encodeCiphertext(subTlv.encode());
        } catch (IOException e) {
            return error(TLVError.AUTHENTICATION);
        }

        TLV out = new TLV();
        Type.STATE.set(out, 5);
        Type.ENCRYPTED_DATA.set(out, encryptedData);
        return out;
    }

    private TLV doVerification(TLV in) {
        Optional<byte[]> encrypedData$ = Type.ENCRYPTED_DATA.get(in);
        if (!encrypedData$.isPresent()) {
            return Type.STATE.set(error(TLVError.AUTHENTICATION), 6);
        }

        Chacha chacha = new Chacha(getSessionKey(getSRPSessionKey(),
                                                 PAIR_SETUP_ENCRYPT_SALT,
                                                 PAIR_SETUP_ENCRYPT_INFO));

        Chacha.Decoder decoder = chacha.newDecoder("PS-Msg06");
        TLV subTlv = null;
        try {
            subTlv = TLV.decodeFrom(decoder.decodeEncryptedData(encrypedData$.get()));
        } catch (IOException e) {
            return error(TLVError.AUTHENTICATION);
        }

        Optional<String> identifer$ = Type.IDENTIFIER.get(subTlv);
        Optional<byte[]> publicKey$ = Type.PUBLIC_KEY.get(subTlv);
        Optional<byte[]> signature$ = Type.SIGNATURE.get(subTlv);

        byte[] accessoryX = getSessionKey(getSRPSessionKey(),
                                          PAIR_SETUP_ACCESSORY_SALT,
                                          PAIR_SETUP_ACCESSORY_INFO);

        byte[] accessoryInfo = getAccessoryInfo(accessoryX, identifer$.get(), publicKey$.get());

        EdsaVerifier verifier = new EdsaVerifier(publicKey$.get());
        try {
            if (!verifier.verify(accessoryInfo, signature$.get())) {
                error(7, TLVError.AUTHENTICATION);
            }
        } catch (Exception e) {
            return error(TLVError.AUTHENTICATION);
        }

        try {
            getAuthStorage().put(identifer$.get(), this.pinSupplier.get(), publicKey$.get());
        } catch (IOException e) {
            return error(TLVError.UNKNOWN);
        }

        return null;
    }

    @Override
    protected SRP6Session getSRPSession() {
        return this.session;
    }

    private SRP6ClientSession session;

    private PinSupplier pinSupplier;
}
