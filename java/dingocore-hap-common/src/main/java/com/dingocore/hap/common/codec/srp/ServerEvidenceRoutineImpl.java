package com.dingocore.hap.common.codec.srp;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.nimbusds.srp6.SRP6CryptoParams;
import com.nimbusds.srp6.SRP6ServerEvidenceContext;
import com.nimbusds.srp6.ServerEvidenceRoutine;

import static com.dingocore.hap.common.codec.srp.Util.bigIntegerToUnsignedByteArray;

public class ServerEvidenceRoutineImpl implements ServerEvidenceRoutine {

	@Override
	public BigInteger computeServerEvidence(SRP6CryptoParams cryptoParams,
			SRP6ServerEvidenceContext ctx) {
		
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance(cryptoParams.H);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Could not locate requested algorithm", e);
		}
		
		byte[] hS = digest.digest(bigIntegerToUnsignedByteArray(ctx.S));
		
		digest.update(bigIntegerToUnsignedByteArray(ctx.A));
		digest.update(bigIntegerToUnsignedByteArray(ctx.M1));
		digest.update(hS);
		
		return new BigInteger(1, digest.digest());


	}


}
