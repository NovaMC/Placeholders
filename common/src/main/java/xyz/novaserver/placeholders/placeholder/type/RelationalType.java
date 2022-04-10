package xyz.novaserver.placeholders.placeholder.type;

import xyz.novaserver.placeholders.common.data.PlayerData;

public interface RelationalType {
    String get(PlayerData viewer, PlayerData player);
}
