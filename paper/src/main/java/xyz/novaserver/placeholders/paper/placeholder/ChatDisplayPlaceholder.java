package xyz.novaserver.placeholders.paper.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class ChatDisplayPlaceholder extends AbstractRelationalPlaceholder {
    private final PlaceholdersPlugin plugin;

    public ChatDisplayPlaceholder(PlaceholdersPaper plugin) {
        super("chat_display");
        this.plugin = plugin;
    }

    @Override
    public String get(UUID viewer, UUID player) {
        final ConfigurationNode node = plugin.getConfiguration().getNode("chat-display");

        PlaceholderPlayer pViewer = PlaceholderPlayer.getPlayerMap().get(viewer);
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(player);
        String message = "";

        if (pViewer != null && pPlayer != null && !pPlayer.getLastMessage().isEmpty()) {
            if (!pViewer.getPlatform().equals(PlaceholderPlayer.Platform.BEDROCK)) {
                message = node.getNode("prefix").getString("");
            } else {
                message = node.getNode("prefix-bedrock").getString("");
            }
            message += node.getNode("color").getString("") + pPlayer.getLastMessage();
        }

        return message;
    }
}