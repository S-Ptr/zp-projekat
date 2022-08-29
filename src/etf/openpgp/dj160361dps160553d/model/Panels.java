package etf.openpgp.dj160361dps160553d.model;

public enum Panels {
    MAIN(1),
    GENERATE_KEY_PAIR(1),
    DELETE_KEY_PAIR(2),
    VIEW_PUBLIC_KEY_RING(3),
    VIEW_PRIVATE_KEY_RING(4),
    EXPORT_PUBLIC_KEY(5),
    EXPORT_PRIVATE_KEY(6),
    SEND_MESSAGE(7),

    RECEIVE_MESSAGE(8);
    private final int screen;

    Panels(int screen) {
        this.screen = screen;
    }

    public int getValue() {
        return this.screen;
    }

}
