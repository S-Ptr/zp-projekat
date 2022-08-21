package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPrivateKey;

import javax.crypto.Cipher;
import java.io.IOException;
import java.security.KeyPair;

public class HelloWorld {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        PrivateKeySet privateKeys;
        try {
            privateKeys = new PrivateKeySet();
        } catch (PGPException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        KeyService ks = new KeyService();
        try {
            privateKeys.addPrivateKey(ks.generateKeyPair(KeyLength.RSA2048), "stefan", "stefan@stefex.com", "nijestefan");
            privateKeys.exportPairToFile("stefan@stefex.com");
            PGPKeyPair pgpKeyPair = privateKeys.getKeyPair("stefan@stefex.com", "nijestefan");
            if(pgpKeyPair != null) {System.out.println("gg");}

        } catch (PGPException e) {
            e.getUnderlyingException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
