package com.dingocore.hap.common.codec.crypto;

import java.io.IOException;

import com.dingocore.hap.common.util.ByteUtil;
import org.bouncycastle.crypto.engines.ChaChaEngine;
import org.bouncycastle.crypto.generators.Poly1305KeyGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.crypto.params.ParametersWithIV;

public class ChachaEncoder {

    private final ChaChaEngine encryptCipher;
    
	public ChachaEncoder(byte[] key, byte[] nonce) throws IOException {
		
        this.encryptCipher = new ChaChaEngine(20);
        
        this.encryptCipher.init(true, new ParametersWithIV(new KeyParameter(key), nonce));
        
        
	}

	public byte[] encodeCiphertext(byte[] plaintext) throws IOException {
		return encodeCiphertext(plaintext, null);
	}
	
	public byte[] encodeCiphertext(byte[] plaintext, byte[] additionalData) throws IOException {
		KeyParameter macKey = initRecordMAC(encryptCipher);
        
		byte[] ciphertext = new byte[plaintext.length];
		encryptCipher.processBytes(plaintext, 0, plaintext.length, ciphertext, 0);
		
		byte[] calculatedMAC = PolyKeyCreator.create(macKey, additionalData, ciphertext);
        
        byte[] ret = new byte[ciphertext.length + 16];
        System.arraycopy(ciphertext, 0, ret, 0, ciphertext.length);
        System.arraycopy(calculatedMAC, 0, ret, ciphertext.length, 16);
        return ret;
    }
	
	private KeyParameter initRecordMAC(ChaChaEngine cipher)
    {
        byte[] firstBlock = new byte[64];
        cipher.processBytes(firstBlock, 0, firstBlock.length, firstBlock, 0);
        System.err.println( "firstBlock: " + ByteUtil.toString(firstBlock));

        // NOTE: The BC implementation puts 'r' after 'k'
        System.arraycopy(firstBlock, 0, firstBlock, 32, 16);
        KeyParameter macKey = new KeyParameter(firstBlock, 16, 32);
        Poly1305KeyGenerator.clamp(macKey.getKey());
        return macKey;
    }
	
}
