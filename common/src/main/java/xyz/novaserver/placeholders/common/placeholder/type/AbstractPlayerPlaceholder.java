package xyz.novaserver.placeholders.common.placeholder.type;

import java.util.UUID;

public abstract class AbstractPlayerPlaceholder extends BasePlaceholder {
    public AbstractPlayerPlaceholder(String identifier, int refresh) {
        super(identifier, refresh);
    }

    public AbstractPlayerPlaceholder(String identifier) {
        super(identifier);
    }

    public abstract String get(UUID player);
}
