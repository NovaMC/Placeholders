package xyz.novaserver.placeholders.core.placeholder;

import xyz.novaserver.placeholders.core.PlaceholdersPlugin;

import java.util.UUID;

public abstract class AbstractPlaceholder {
    private final String identifier;

    public AbstractPlaceholder(String identifier, PlaceholdersPlugin plugin) {
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public abstract String get(UUID player);
}
