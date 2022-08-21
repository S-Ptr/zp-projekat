package etf.openpgp.dj160361dps160553d.service;

import etf.openpgp.dj160361dps160553d.model.KeyLength;
import etf.openpgp.dj160361dps160553d.model.User;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
//import org.bouncycastle.util.encoders.Base64;

import java.security.*;
import java.util.Date;


public class KeyService {

    private static KeyPairGenerator keygen1024;
    private static KeyPairGenerator keygen2048;
    private static KeyPairGenerator keygen4096;


    public KeyService() {

        try {
            keygen1024 = KeyPairGenerator.getInstance("RSA");
            keygen2048 = KeyPairGenerator.getInstance("RSA");
            keygen4096 = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keygen1024.initialize(1024);
        keygen2048.initialize(2048);
        keygen4096.initialize(4096);
//        KeyPair keyPair = keygen2048.generateKeyPair();
//        PublicKey pub = keyPair.getPublic();
//        PrivateKey priv = keyPair.getPrivate();
//        String privateKey = new String(Base64.encode(priv.getEncoded()));
//        String publicKey = new String(Base64.encode(pub.getEncoded()));
//        System.out.println(privateKey);
//        System.out.println(publicKey);
//
//        keyPair = keygen4096.generateKeyPair();
//        pub = keyPair.getPublic();
//        priv = keyPair.getPrivate();
//        privateKey = new String(Base64.encode(priv.getEncoded()));
//        publicKey = new String(Base64.encode(pub.getEncoded()));
//        System.out.println(privateKey);
//        System.out.println(publicKey);

    }

    public static PGPKeyPair generateKeyPair(User user) throws IllegalArgumentException, PGPException {
        PGPKeyPair asymmetricCipherKeyPair;

        switch (user.getRsaAlgorithm()) {
            case RSA1024 -> {
                asymmetricCipherKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keygen1024.generateKeyPair(), new Date());
                return asymmetricCipherKeyPair;
            }
            case RSA2048 -> {
                asymmetricCipherKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keygen2048.generateKeyPair(), new Date());
                return asymmetricCipherKeyPair;
            }
            case RSA4096 -> {
                asymmetricCipherKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keygen4096.generateKeyPair(), new Date());
                return asymmetricCipherKeyPair;
            }
            default ->
                    throw new IllegalArgumentException("Invalid key length provided. Check the arguments of your method call and try again.");
        }
    }





    


}
