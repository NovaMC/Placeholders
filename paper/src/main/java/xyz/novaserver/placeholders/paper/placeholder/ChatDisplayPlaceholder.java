package xyz.novaserver.placeholders.paper.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.RelationalType;

import java.util.UUID;

public class ChatDisplayPlaceholder extends Placeholder implements RelationalType {
    public ChatDisplayPlaceholder(PlaceholdersPlugin plugin) {
        super(plugin, "chat_display");
    }

    @Override
    public String get(UUID viewer, UUID player) {
        final ConfigurationNode node = getPlugin().getConfiguration().getNode("chat-display");

        PlayerData viewerData = getPlugin().getPlayerData(viewer);
        PlayerData playerData = getPlugin().getPlayerData(player);
        String message = "";

        if (viewerData != null && playerData != null && !playerData.getLastMessage().isEmpty()) {
            if (viewerData.isResourcePackApplied()) {
                message = node.getNode("prefix-rp").getString("");
            } else {
                message = node.getNode("prefix-vanilla").getString("");
            }
            message += node.getNode("color").getString("") + playerData.getLastMessage();
        }

        return message;
    }
}
