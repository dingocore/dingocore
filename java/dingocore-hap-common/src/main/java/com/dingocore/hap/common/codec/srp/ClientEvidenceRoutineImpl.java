package com.dingocore.hap.common.codec.srp;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.nimbusds.srp6.ClientEvidenceRoutine;
import com.nimbusds.srp6.SRP6ClientEvidenceContext;
import com.nimbusds.srp6.SRP6CryptoParams;

public class ClientEvidenceRoutineImpl implements ClientEvidenceRoutine {

	public ClientEvidenceRoutineImpl() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Calculates M1 according to the following formula:
	 * 
	 * M1 = H(H(N) xor H(g) || H(username) || s || A || B || H(S))
	 */
	@Override
	public BigInteger computeClientEvidence(SRP6CryptoParams cryptoParams,
			SRP6ClientEvidenceContext ctx) {

		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(cryptoParams.H);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not locate requested algorithm", e);
		}
		digest.update(Util.bigIntegerToUnsignedByteArray(cryptoParams.N));
		byte[] hN = digest.digest();

		digest.update(Util.bigIntegerToUnsignedByteArray(cryptoParams.g));
		byte[] hg = digest.digest();

		byte[] hNhg = xor(hN, hg);

		digest.update(ctx.userID.getBytes(StandardCharsets.UTF_8));
		byte[] hu = digest.digest();

		digest.update(Util.bigIntegerToUnsignedByteArray(ctx.S));
		byte[] hS = digest.digest();

		digest.update(hNhg);
		digest.update(hu);
		digest.update(Util.bigIntegerToUnsignedByteArray(ctx.s));
		digest.update(Util.bigIntegerToUnsignedByteArray(ctx.A));
		digest.update(Util.bigIntegerToUnsignedByteArray(ctx.B));
		digest.update(hS);
		BigInteger ret = new BigInteger(1, digest.digest());
		return ret;

		/*
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(cryptoParams.H);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not locate requested algorithm", e);
		}
		digest.update(bigIntegerToUnsignedByteArray(cryptoParams.N));
		byte[] hN = digest.digest();
		
		digest.update(bigIntegerToUnsignedByteArray(cryptoParams.g));
		byte[] hg = digest.digest();
		
		byte[] hNhg = xor(hN, hg);
		
		digest.update(ctx.userID.getBytes(StandardCharsets.UTF_8));
		byte[] hu = digest.digest();
		
		digest.update(bigIntegerToUnsignedByteArray(ctx.S));
		byte[] hS = digest.digest();
		
		digest.update(hNhg);
		digest.update(hu);
		digest.update(bigIntegerToUnsignedByteArray(ctx.s));
		digest.update(bigIntegerToUnsignedByteArray(ctx.A));
		digest.update(bigIntegerToUnsignedByteArray(ctx.B));
		digest.update(hS);
		BigInteger ret = new BigInteger(1, digest.digest());
		return ret;
		*/
	}
	
	private static byte[] xor(byte[] b1, byte[] b2) {
		byte[] result = new byte[b1.length];
		for (int i=0; i<b1.length; i++) {
			result[i] = (byte) (b1[i] ^ b2[i]);
		}
		return result;
	}

}
