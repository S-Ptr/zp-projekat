package etf.openpgp.dj160361dps160553d.model;

public enum KeyLength {
    RSA1024(1024),
    RSA2048(2048),
    RSA4096(4096);

    private final int keyLength;

    KeyLength(int keyLength) {
        this.keyLength = keyLength;
    }

    public int getValue() {
        return this.keyLength;
    }

}
