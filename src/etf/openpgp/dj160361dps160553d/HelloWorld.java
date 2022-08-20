package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPrivateKey;

import javax.crypto.Cipher;
import java.security.KeyPair;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        KeyService ks = new KeyService();
        //PGPKeyPair k2048 = null;
        PGPKeyPair k1024 = null;
        try {
            k1024 = ks.generateKeyPair(KeyLength.RSA1024);
            //k2048 = ks.generateKeyPair(KeyLength.RSA2048);
        } catch (PGPException e) {
            e.getUnderlyingException();
        }
        //System.out.println(Long.toHexString(k2048.getKeyID()));
        System.out.println(Long.toHexString(k1024.getKeyID()));


    }
}
