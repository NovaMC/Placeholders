package xyz.novaserver.placeholders.placeholder.type;

import xyz.novaserver.placeholders.common.Placeholders;

public class Placeholder {
    private final Placeholders placeholders;
    private final String identifier;
    private final int refresh;

    protected Placeholder(Placeholders placeholders, String identifier, int refresh) {
        this.placeholders = placeholders;
        this.identifier = identifier;
        this.refresh = refresh;
    }

    protected Placeholder(Placeholders placeholders, String identifier) {
        this(placeholders, identifier, 1000);
    }

    public Placeholders getPlaceholders() {
        return placeholders;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getRefresh() {
        return refresh;
    }
}
