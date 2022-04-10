package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;

public class ChatDisplayPlaceholder extends Placeholder implements RelationalType {
    public ChatDisplayPlaceholder(Placeholders plugin) {
        super(plugin, "chat_display");
    }

    @Override
    public String get(PlayerData viewer, PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig().getNode("chat-display");

        if (!player.getLastMessage().isEmpty()) {
            final String prefix = viewer.isResourcePackApplied()
                    ? node.getNode("prefix-rp").getString() : node.getNode("prefix-default").getString();
            return prefix + node.getNode("color").getString() + player.getLastMessage();
        } else {
            return "";
        }
    }
}
