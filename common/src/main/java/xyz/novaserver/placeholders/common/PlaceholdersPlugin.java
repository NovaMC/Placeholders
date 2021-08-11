package xyz.novaserver.placeholders.common;

import ninja.leaping.configurate.ConfigurationNode;

import java.util.HashMap;
import java.util.Map;

public interface PlaceholdersPlugin {
    boolean reloadConfiguration();

    ConfigurationNode getConfiguration();

    default Map<String, String> getPlaceholderMap() {
        Map<String, String> placeholderMap = new HashMap<>();
        getConfiguration().getChildrenMap().keySet().forEach(key -> {
            placeholderMap.put((String)key, getConfiguration().getChildrenMap().get(key).getString());
        });
        return placeholderMap;
    }
}
