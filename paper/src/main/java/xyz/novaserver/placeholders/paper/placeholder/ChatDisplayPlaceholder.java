package xyz.novaserver.placeholders.paper.placeholder;

import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class ChatDisplayPlaceholder extends AbstractRelationalPlaceholder {
    public ChatDisplayPlaceholder() {
        super("chat_display");
    }

    @Override
    public String get(UUID viewer, UUID player) {
        PlaceholderPlayer pViewer = PlaceholderPlayer.getPlayerMap().get(viewer);
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(player);
        String message = "";

        if (pViewer != null && pPlayer != null && !pPlayer.getLastMessage().isEmpty()) {
            if (!pViewer.getPlatform().equals(PlaceholderPlayer.Platform.BEDROCK)) {
                message = pPlayer.getMessagePrefix() + pPlayer.getLastMessage();
            } else {
                message = pPlayer.getLastMessage();
            }
        }

        return message;
    }
}
