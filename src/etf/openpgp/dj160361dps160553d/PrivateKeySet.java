package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.operator.PBESecretKeyDecryptor;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.PGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

import javax.print.attribute.HashAttributeSet;
import java.io.Console;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class PrivateKeySet {

    private PGPSecretKeyRingCollection secretKeys;
    private PGPSecretKey key;
    private PGPDigestCalculatorProvider hashCalculator;

    public PrivateKeySet(PGPKeyPair keyPair, String name, String email, String password){
        try {
            this.secretKeys= new PGPSecretKeyRingCollection(new ArrayList<PGPSecretKeyRing>());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
        this.hashCalculator = new BcPGPDigestCalculatorProvider();
        /*try {
            PGPSecretKey key = new PGPSecretKey(keyPair.getPrivateKey(), keyPair.getPublicKey(), hashCalculator.get(HashAlgorithmTags.SHA1), true, new BcPBESecretKeyEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_128, hashCalculator.get(HashAlgorithmTags.SHA1)).build(password.toCharArray()));
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }*/

        try {
            PGPSecretKey key2 = new PGPSecretKey(PGPSignature.POSITIVE_CERTIFICATION, keyPair, name + " <" + email + ">",
                    hashCalculator.get(HashAlgorithmTags.SHA1),
                    null, null,
                    new BcPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                    new BcPBESecretKeyEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_128, hashCalculator.get(HashAlgorithmTags.SHA1)).build(password.toCharArray()));
            this.key = key2;
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public PGPPrivateKey retrieveSecretKey(String pass) throws InvalidPasswordException {
            try {
                PBESecretKeyDecryptor decryptor = new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(pass.toCharArray());
                PGPPrivateKey privateKey = key.extractPrivateKey(decryptor);
                return privateKey;
            } catch (Throwable t) {
                throw new InvalidPasswordException();
            }
    }

    public void addPrivateKey (PGPKeyPair keyPair, String name, String email, String passphrase){
        try {
            PGPSecretKey key = new PGPSecretKey(PGPSignature.POSITIVE_CERTIFICATION, keyPair, name + " <" + email + ">",
                    hashCalculator.get(HashAlgorithmTags.SHA1),
                    null, null,
                    new BcPGPContentSignerBuilder(keyPair.getPublicKey().getAlgorithm(), HashAlgorithmTags.SHA1),
                    new BcPBESecretKeyEncryptorBuilder(SymmetricKeyAlgorithmTags.AES_128, hashCalculator.get(HashAlgorithmTags.SHA1)).build(passphrase.toCharArray()));
            List<PGPSecretKey> keyRing = new ArrayList<>();
            keyRing.add(key);
            this.secretKeys = PGPSecretKeyRingCollection.addSecretKeyRing(this.secretKeys, new PGPSecretKeyRing(keyRing));
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrivateKey(long keyID) {
        try {
            this.secretKeys = PGPSecretKeyRingCollection.removeSecretKeyRing(this.secretKeys, this.secretKeys.getSecretKeyRing(keyID));
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
    }

    public void removePrivateKey(String user){
        Iterator<PGPSecretKeyRing> detectedUsers = null;
        try {
            detectedUsers = this.secretKeys.getKeyRings(user, true);//boolean means whether partial matches are allowed or not
            while(detectedUsers.hasNext()){
                this.secretKeys = PGPSecretKeyRingCollection.removeSecretKeyRing(this.secretKeys, detectedUsers.next());
            }
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }


    }

    public PGPKeyPair getKeyPair(String user, String passphrase) {
        try {
            Iterator<PGPSecretKeyRing> matchingSecretKeys = this.secretKeys.getKeyRings(user, true);
            while(matchingSecretKeys.hasNext()){
                PGPSecretKeyRing secretKeyRing = matchingSecretKeys.next();
                PGPSecretKey secretKey = secretKeyRing.getSecretKey();
                return secretKey.extractKeyPair(new BcPBESecretKeyDecryptorBuilder(hashCalculator).build(passphrase.toCharArray()));
            }
        } catch (PGPException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
