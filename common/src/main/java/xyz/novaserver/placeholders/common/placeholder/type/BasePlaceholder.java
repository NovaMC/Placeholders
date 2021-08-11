package xyz.novaserver.placeholders.common.placeholder.type;

public class BasePlaceholder {
    private final String identifier;

    public BasePlaceholder(String identifier) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }
}
