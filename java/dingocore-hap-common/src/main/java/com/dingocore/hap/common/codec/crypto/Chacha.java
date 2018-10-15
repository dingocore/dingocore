package com.dingocore.hap.common.codec.crypto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;
import org.bouncycastle.crypto.tls.AlertDescription;
import org.bouncycastle.crypto.tls.TlsFatalAlert;
import org.bouncycastle.util.Arrays;

/**
 * Created by bob on 8/28/18.
 */
public class Chacha {

    public Chacha(byte[] key) {
        this.key = key;
    }

    public Decoder newDecoder(String nonce) {
        return new Decoder(nonce);
    }

    public Decoder newDecoder(byte[] nonce) {
        return new Decoder(nonce);
    }

    public Encoder newEncoder(String nonce) {
        return new Encoder(nonce);
    }

    public Encoder newEncoder(byte[] nonce) {
        return new Encoder(nonce);
    }


    private final byte[] key;

    public class Decoder extends CodecBase {
        Decoder(String nonce) {
            super(nonce);
            this.cipher.init(false, new ParametersWithIV(new KeyParameter(key), getNonce()));
        }

        Decoder(byte[] nonce) {
            super(nonce);
            this.cipher.init(false, new ParametersWithIV(new KeyParameter(key), getNonce()));
        }

        public byte[] decodeCiphertext(ByteBuf receivedMAC, ByteBuf additionalData, ByteBuf ciphertext) throws IOException {
            byte[] receivedMAC$ = new byte[receivedMAC.readableBytes()];
            receivedMAC.readBytes(receivedMAC$);
            byte[] additionalData$ = new byte[additionalData.readableBytes()];
            additionalData.readBytes(additionalData$);
            byte[] ciphertext$ = new byte[ciphertext.readableBytes()];
            ciphertext.readBytes(ciphertext$);
            return decodeCiphertext(receivedMAC$, additionalData$, ciphertext$);
        }

        public byte[] decodeCiphertext(byte[] receivedMAC, byte[] additionalData, byte[] ciphertext) throws IOException {

            try {
                KeyParameter macKey = initRecordMAC();

                byte[] calculatedMAC = PolyKeyCreator.create(macKey, additionalData, ciphertext);

                if (!Arrays.constantTimeAreEqual(calculatedMAC, receivedMAC)) {
                    throw new TlsFatalAlert(AlertDescription.bad_record_mac);
                }

                byte[] output = new byte[ciphertext.length];
                this.cipher.processBytes(ciphertext, 0, ciphertext.length, output, 0);
                return output;
            } finally {
                reset();
            }
        }

        public byte[] decodeCiphertext(byte[] receivedMAC, byte[] ciphertext) throws IOException {
            return decodeCiphertext(receivedMAC, null, ciphertext);
        }

        public byte[] decodeEncryptedData(byte[] data) throws IOException {

            byte[] material = new byte[data.length - 16];
            byte[] authTag = new byte[16];

            System.arraycopy(data, 0, material, 0, material.length);
            System.arraycopy(data, material.length, authTag, 0, authTag.length);

            return decodeCiphertext(authTag, material);
        }
    }

    public class Encoder extends CodecBase {
        Encoder(String nonce) {
            super(nonce);
            this.cipher.init(true, new ParametersWithIV(new KeyParameter(key), getNonce()));
        }

        Encoder(byte[] nonce) {
            super(nonce);
            this.cipher.init(true, new ParametersWithIV(new KeyParameter(key), getNonce()));
        }

        public byte[] encodeCiphertext(byte[] plaintext) throws IOException {
            return encodeCiphertext(plaintext, null);
        }

        public ByteBuf encodeCiphertext(ByteBuf plaintext, ByteBuf additionalData) throws IOException {
            byte[] plaintext$ = new byte[plaintext.readableBytes()];
            plaintext.readBytes(plaintext$);

            byte[] additionalData$ = new byte[additionalData.readableBytes()];
            additionalData.readBytes(additionalData$);

            byte[] resultBytes = encodeCiphertext(plaintext$, additionalData$);

            ByteBuf result = plaintext.alloc().buffer(resultBytes.length);
            result.writeBytes(resultBytes);
            return result;
        }

        public byte[] encodeCiphertext(byte[] plaintext, byte[] additionalData) throws IOException {

            try {
                KeyParameter macKey = initRecordMAC();

                byte[] ciphertext = new byte[plaintext.length];
                this.cipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);

                byte[] calculatedMAC = PolyKeyCreator.create(macKey, additionalData, ciphertext);

                byte[] ret = new byte[ciphertext.length + 16];
                System.arraycopy(ciphertext, 0, ret, 0, ciphertext.length);
                System.arraycopy(calculatedMAC, 0, ret, ciphertext.length, 16);
                return ret;
            } finally {
                reset();
            }
        }
    }

    private class CodecBase {
        protected CodecBase(String nonce) {
            this.cipher = new ChaChaEngine(20);
            this.nonce = nonce.getBytes(StandardCharsets.UTF_8);
        }

        protected CodecBase(byte[] nonce) {
            this.cipher = new ChaChaEngine(20);
            this.nonce = nonce;
        }

        protected byte[] getNonce() {
            return this.nonce;
        }

        protected void reset() {
            this.cipher.reset();
        }

        protected KeyParameter initRecordMAC() {
            byte[] firstBlock = new byte[64];
            this.cipher.processBytes(firstBlock, 0, firstBlock.length, firstBlock, 0);

            KeyParameter macKey = new KeyParameter(firstBlock, 0, 32);
            Poly1305KeyGenerator.clamp(macKey.getKey());
            return macKey;
        }

        protected final ChaChaEngine cipher;

        private final byte[] nonce;
    }
}
