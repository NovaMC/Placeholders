package xyz.novaserver.placeholders.common;

import net.kyori.adventure.text.Component;
import xyz.novaserver.placeholders.common.messaging.PluginPlatform;

public interface Plugin {
    Placeholders getPlaceholders();

    PluginPlatform getPlatform();

    void sendMessage(Object source, Component component);

    void logError(String message);

    void logInfo(String message);
}
