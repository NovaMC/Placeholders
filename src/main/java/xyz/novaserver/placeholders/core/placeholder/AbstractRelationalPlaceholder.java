package xyz.novaserver.placeholders.core.placeholder;

import xyz.novaserver.placeholders.core.PlaceholdersPlugin;

import java.util.Map;
import java.util.UUID;

public abstract class AbstractRelationalPlaceholder {
    private final String identifier;
    private final Map<String, String> placeholderMap;

    public AbstractRelationalPlaceholder(String identifier, PlaceholdersPlugin plugin) {
        this.identifier = identifier;
        this.placeholderMap = plugin.getPlaceholderMap();
    }

    public String getIdentifier() {
        return identifier;
    }

    public Map<String, String> getPlaceholderMap() {
        return placeholderMap;
    }

    public abstract String get(UUID viewer, UUID player);
}
