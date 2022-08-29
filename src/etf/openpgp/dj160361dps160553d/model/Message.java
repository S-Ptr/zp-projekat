package etf.openpgp.dj160361dps160553d.model;

import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPPublicKey;

import java.io.File;

public class Message {

    PGPKeyPair senderKey;
    PGPPublicKey receiverKey;
    File inputFile;
    File outputFile;
    int symmetricAlgorithm;
    boolean toAuth;
    boolean toCompress;
    boolean toRadix64;

    public Message() {
    }

    public Message(PGPKeyPair senderKey, PGPPublicKey receiverKey, File inputFile, File outputFile, int symmetricAlgorithm, boolean toAuth, boolean toCompress, boolean toRadix64) {
        this.senderKey = senderKey;
        this.receiverKey = receiverKey;
        this.inputFile = inputFile;
        this.outputFile = outputFile;
        this.symmetricAlgorithm = symmetricAlgorithm;
        this.toAuth = toAuth;
        this.toCompress = toCompress;
        this.toRadix64 = toRadix64;
    }

    public PGPKeyPair getSenderKey() {
        return senderKey;
    }

    public void setSenderKey(PGPKeyPair senderKey) {
        this.senderKey = senderKey;
    }

    public PGPPublicKey getReceiverKey() {
        return receiverKey;
    }

    public void setReceiverKey(PGPPublicKey receiverKey) {
        this.receiverKey = receiverKey;
    }

    public File getInputFile() {
        return inputFile;
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public int getSymmetricAlgorithm() {
        return symmetricAlgorithm;
    }

    public void setSymmetricAlgorithm(int symmetricAlgorithm) {
        this.symmetricAlgorithm = symmetricAlgorithm;
    }

    public boolean isToAuth() {
        return toAuth;
    }

    public void setToAuth(boolean toAuth) {
        this.toAuth = toAuth;
    }

    public boolean isToCompress() {
        return toCompress;
    }

    public void setToCompress(boolean toCompress) {
        this.toCompress = toCompress;
    }

    public boolean isToRadix64() {
        return toRadix64;
    }

    public void setToRadix64(boolean toRadix64) {
        this.toRadix64 = toRadix64;
    }

    @Override
    public String toString() {
        return "Message{" +
                "senderKey=" + senderKey +
                ", receiverKey=" + receiverKey +
                ", inputFile=" + inputFile +
                ", outputFile=" + outputFile +
                ", symmetricAlgorithm=" + symmetricAlgorithm +
                ", toAuth=" + toAuth +
                ", toCompress=" + toCompress +
                ", toRadix64=" + toRadix64 +
                '}';
    }
}
