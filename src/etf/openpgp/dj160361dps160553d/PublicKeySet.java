package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

public class PublicKeySet {

    private String name;
    private String email;
    private byte[] pass;
    AsymmetricKeyParameter puKey;


    public PublicKeySet(String name, String email, byte[] pass, AsymmetricKeyParameter key) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.puKey = key;
    }

    public AsymmetricKeyParameter getKey(){
        return puKey;
    }

    public AsymmetricKeyParameter getPublic(){
        return puKey;
    }

}
