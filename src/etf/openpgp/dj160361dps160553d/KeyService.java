package etf.openpgp.dj160361dps160553d;

import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

enum KeyLength{
    RSA1024,
    RSA2048,
    RSA4096
}


public class KeyService {


    private RSAKeyPairGenerator keygen1024;
    private RSAKeyPairGenerator keygen2048;
    private RSAKeyPairGenerator keygen4096;

    SecureRandom secureRandom;

    public KeyService() {
        keygen1024 = new RSAKeyPairGenerator();
        keygen2048 = new RSAKeyPairGenerator();
        keygen4096 = new RSAKeyPairGenerator();
        secureRandom = new SecureRandom("ZPProjekat2022".getBytes());

        keygen1024.init(new RSAKeyGenerationParameters(new BigInteger("65537"), secureRandom,1024,5));
        keygen2048.init(new RSAKeyGenerationParameters(new BigInteger("65537"),secureRandom,2048,5));
        keygen4096.init(new RSAKeyGenerationParameters(new BigInteger("65537"),secureRandom,4096,5));

    }

    public AsymmetricCipherKeyPair generateKeyPair(KeyLength keyLength) throws IllegalArgumentException{
        AsymmetricCipherKeyPair asymmetricCipherKeyPair;
        switch(keyLength) {
            case RSA1024:
                asymmetricCipherKeyPair = keygen1024.generateKeyPair();
                break;
            case RSA2048:
                asymmetricCipherKeyPair = keygen2048.generateKeyPair();
                break;
            case RSA4096:
                asymmetricCipherKeyPair = keygen4096.generateKeyPair();
                break;
            default:
                throw new IllegalArgumentException("Invalid key length provided. Check the arguments of your method call and try again.");
        }
        return asymmetricCipherKeyPair;
    }

    


}
