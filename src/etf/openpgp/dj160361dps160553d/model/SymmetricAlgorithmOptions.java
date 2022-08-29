package etf.openpgp.dj160361dps160553d.model;

public enum SymmetricAlgorithmOptions {

    AES_128(7),
    TRIPLE_DES(2),
    NO_SYMMETRIC_ENCRYPTION(0);

    private final int symmetricAlgorithm;

    SymmetricAlgorithmOptions(int symmetricAlgorithm) {
        this.symmetricAlgorithm = symmetricAlgorithm;
    }

    public int getValue() {
        return this.symmetricAlgorithm;
    }

    public static int myValueOf(String name) {
        for (SymmetricAlgorithmOptions e : values()) {
            if (e.name().equals(name)) {
                return e.getValue();
            }
        }
        return 0;
    }


}