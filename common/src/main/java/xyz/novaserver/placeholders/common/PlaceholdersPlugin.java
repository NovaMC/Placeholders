package xyz.novaserver.placeholders.common;

import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;

import java.util.UUID;

public interface PlaceholdersPlugin {
    PlayerData getPlayerData(UUID player);

    void addPlayerData(UUID player);

    void removePlayerData(UUID player);

    void sendMessage(Object source, Component message);

    boolean reloadConfiguration();

    ConfigurationNode getConfiguration();

    default String getRootValue(String key) {
        return getConfiguration().getNode(key).getString();
    }
}
