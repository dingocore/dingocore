package com.dingocore.hap.common.codec.pair;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import com.nimbusds.srp6.SRP6Session;
import com.dingocore.hap.common.codec.tlv.TLV;
import com.dingocore.hap.common.codec.srp.Util;
import com.dingocore.hap.common.spi.AuthStorage;

/**
 * Created by bob on 8/29/18.
 */
public abstract class PairSetupManager<T extends AuthStorage> extends PairManagerBase<T> {

    protected final static BigInteger N_3072 = new BigInteger("5809605995369958062791915965639201402176612226902900533702900882779736177890990861472094774477339581147373410185646378328043729800750470098210924487866935059164371588168047540943981644516632755067501626434556398193186628990071248660819361205119793693985433297036118232914410171876807536457391277857011849897410207519105333355801121109356897459426271845471397952675959440793493071628394122780510124618488232602464649876850458861245784240929258426287699705312584509625419513463605155428017165714465363094021609290561084025893662561222573202082865797821865270991145082200656978177192827024538990239969175546190770645685893438011714430426409338676314743571154537142031573004276428701433036381801705308659830751190352946025482059931306571004727362479688415574702596946457770284148435989129632853918392117997472632693078113129886487399347796982772784615865232621289656944284216824611318709764535152507354116344703769998514148343807");
    protected final static BigInteger G = BigInteger.valueOf(5);
    protected final static String IDENTIFIER = "Pair-Setup";
    protected final static byte[] IDENTIFIER_BYTES = IDENTIFIER.getBytes(StandardCharsets.UTF_8);

    protected static final byte[] PAIR_SETUP_CONTROLLER_SALT = "Pair-Setup-Controller-Sign-Salt".getBytes(StandardCharsets.UTF_8);
    protected static final byte[] PAIR_SETUP_CONTROLLER_INFO = "Pair-Setup-Controller-Sign-Info".getBytes(StandardCharsets.UTF_8);

    protected static final byte[] PAIR_SETUP_ACCESSORY_SALT = "Pair-Setup-Accessory-Sign-Salt".getBytes(StandardCharsets.UTF_8);
    protected static final byte[] PAIR_SETUP_ACCESSORY_INFO = "Pair-Setup-Accessory-Sign-Info".getBytes(StandardCharsets.UTF_8);

    protected static final byte[] PAIR_SETUP_ENCRYPT_SALT = "Pair-Setup-Encrypt-Salt".getBytes(StandardCharsets.UTF_8);
    protected static final byte[] PAIR_SETUP_ENCRYPT_INFO = "Pair-Setup-Encrypt-Info".getBytes(StandardCharsets.UTF_8);

    protected PairSetupManager(T authStorage) {
        super(authStorage);
    }

    protected abstract SRP6Session getSRPSession();

    protected byte[] getSRPSessionKey() {
        MessageDigest digest = getSRPSession().getCryptoParams().getMessageDigestInstance();
        BigInteger S = getSRPSession().getSessionKey();
        byte[] sBytes = Util.bigIntegerToUnsignedByteArray(S);
        byte[] key = digest.digest(sBytes);
        return key;
    }

    public abstract TLV handle(TLV message) throws Exception;
}
