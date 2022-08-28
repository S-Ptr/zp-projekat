package etf.openpgp.dj160361dps160553d.model;

import lombok.*;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;

import java.io.File;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    PGPKeyPair senderKey;
    PGPPublicKey receiverKey;
    File inputFile;
    File outputFile;
    int symmetricAlgorithm;
    boolean toAuth;
    boolean toCompress;
    boolean toRadix64;

}
