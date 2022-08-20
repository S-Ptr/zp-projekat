package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;

import java.io.IOException;

public class PublicKeySet {

    private String name;
    private String email;
    private byte[] pass;
    PGPPublicKey puKey;

    private long keyID;


    public PublicKeySet(String name, String email, byte[] pass, PGPPublicKey key) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.puKey = key;
        this.keyID = puKey.getKeyID();
    }

    public PGPPublicKey getKey(){
        return puKey;
    }
}
