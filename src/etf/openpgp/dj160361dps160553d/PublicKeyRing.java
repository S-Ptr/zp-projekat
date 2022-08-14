package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.openpgp.PGPKeyRing;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPPublicKeyRing;

import java.util.LinkedList;
import java.util.List;

public class PublicKeyRing {
    private PGPPublicKeyRing publicKeyRing;



    public PublicKeyRing(){
        publicKeyRing = new PGPPublicKeyRing(new LinkedList<PGPPublicKey>());
    }
}
