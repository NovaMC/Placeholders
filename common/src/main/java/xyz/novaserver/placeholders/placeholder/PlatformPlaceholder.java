package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;

public class PlatformPlaceholder extends Placeholder implements PlayerType {
    public PlatformPlaceholder(Placeholders plugin) {
        super(plugin, "platform", 5000);
    }

    @Override
    public String get(PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig();
        String placeholder;

        switch (player.getPlatform()) {
            // Handle player's platform/loader
            case BEDROCK -> {
                // Handle bedrock device type
                switch (player.getDeviceOs()) {
                    case NX -> placeholder = node.getNode("device", "switch").getString();
                    case XBOX -> placeholder = node.getNode("device", "xbox").getString();
                    case PS4 -> placeholder = node.getNode("device", "playstation").getString();
                    default -> placeholder = node.getNode("device", "default").getString();
                }
            }
            case FABRIC -> placeholder = node.getNode("platform", "fabric").getString();
            case FORGE -> placeholder = node.getNode("platform", "forge").getString();
            default -> placeholder = node.getNode("platform", "default").getString();
        }

        return placeholder;
    }
}
