package xyz.novaserver.placeholders.common.placeholder.type;

import java.util.UUID;

public interface RelationalType {
    String get(UUID viewer, UUID player);
}
