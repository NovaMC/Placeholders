package xyz.novaserver.placeholders.common.placeholder.type;

public class BasePlaceholder {
    private final String identifier;
    private final int refresh;

    public BasePlaceholder(String identifier, int refresh) {
        this.identifier = identifier;
        this.refresh = refresh;
    }

    public BasePlaceholder (String identifier) {
        this(identifier, 1000);
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getRefresh() {
        return refresh;
    }
}
