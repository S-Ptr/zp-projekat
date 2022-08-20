package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPKeyPair;
import org.bouncycastle.util.encoders.Base64;

import java.math.BigInteger;
import java.security.*;
import java.util.Date;

enum KeyLength{
    RSA1024,
    RSA2048,
    RSA4096
}


public class KeyService {


    private KeyPairGenerator keygen1024;
    private KeyPairGenerator keygen2048;
    private KeyPairGenerator keygen4096;

    private PGPKeyRingGenerator keyRingGenerator;

    SecureRandom secureRandom;

    public KeyService() {

        KeyPairGenerator kpg;
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
        KeyPair keyPair = keygen2048.generateKeyPair();
        PublicKey pub = keyPair.getPublic();
        PrivateKey priv = keyPair.getPrivate();
        String privateKey = new String(Base64.encode(priv.getEncoded()));
        String publicKey = new String(Base64.encode(pub.getEncoded()));
        System.out.println(privateKey);
        System.out.println(publicKey);

        keyPair = keygen4096.generateKeyPair();
        pub = keyPair.getPublic();
        priv = keyPair.getPrivate();
        privateKey = new String(Base64.encode(priv.getEncoded()));
        publicKey = new String(Base64.encode(pub.getEncoded()));
        System.out.println(privateKey);
        System.out.println(publicKey);


    }

    public PGPKeyPair generateKeyPair(KeyLength keyLength) throws IllegalArgumentException, PGPException {
        PGPKeyPair asymmetricCipherKeyPair;

        switch(keyLength) {
            case RSA1024:
                asymmetricCipherKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keygen1024.generateKeyPair(), new Date());
                break;
            case RSA2048:
                asymmetricCipherKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keygen2048.generateKeyPair(), new Date());
                break;
            case RSA4096:
                asymmetricCipherKeyPair = new JcaPGPKeyPair(PGPPublicKey.RSA_GENERAL, keygen4096.generateKeyPair(), new Date());
                break;
            default:
                throw new IllegalArgumentException("Invalid key length provided. Check the arguments of your method call and try again.");
        }
        return asymmetricCipherKeyPair;
    }





    


}
