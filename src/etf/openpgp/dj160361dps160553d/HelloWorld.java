package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import javax.crypto.Cipher;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        KeyService ks = new KeyService();
        AsymmetricCipherKeyPair k1024 = ks.generateKeyPair(KeyLength.RSA1024);
        AsymmetricCipherKeyPair k2048 = ks.generateKeyPair(KeyLength.RSA2048);
        

    }
}
