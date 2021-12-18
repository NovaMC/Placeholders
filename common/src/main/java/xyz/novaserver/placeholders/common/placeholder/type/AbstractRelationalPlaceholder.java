package xyz.novaserver.placeholders.common.placeholder.type;

import java.util.UUID;

public abstract class AbstractRelationalPlaceholder extends BasePlaceholder {
    public AbstractRelationalPlaceholder(String identifier, int refresh) {
        super(identifier, refresh);
    }

    public AbstractRelationalPlaceholder(String identifier) {
        super(identifier);
    }

    public abstract String get(UUID viewer, UUID player);
}
