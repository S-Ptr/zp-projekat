package etf.openpgp.dj160361dps160553d.model;

public enum Panels {
    MAIN(1),
    GENERATE_KEY_PAIR(1),
    DELETE_KEY_PAIR(2),
    VIEW_PUBLIC_KEY_RING(3),
    VIEW_PRIVATE_KEY_RING(4);
    private final int screen;

    Panels(int screen) {
        this.screen = screen;
    }

    public int getValue() {
        return this.screen;
    }

}