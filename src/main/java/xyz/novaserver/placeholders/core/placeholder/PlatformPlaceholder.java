package xyz.novaserver.placeholders.core.placeholder;

import xyz.novaserver.placeholders.core.PlaceholderPlayer;
import xyz.novaserver.placeholders.core.PlaceholdersPlugin;

import java.util.UUID;

public class PlatformPlaceholder extends AbstractRelationalPlaceholder {
    public static String identifier = "platform";

    public PlatformPlaceholder(PlaceholdersPlugin plugin) {
        super(identifier, plugin);
    }

    public String get(UUID viewer, UUID player) {
        String placeholder = "";
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(player);

        if (pPlayer == null) {
            return placeholder;
        }

        switch (pPlayer.getPlatform()) {
            // Handle player's platform/loader
            case BEDROCK -> {
                // Handle bedrock device type
                switch (pPlayer.getDeviceOs()) {
                    case NX -> placeholder = getPlaceholderMap().get("switch");
                    case XBOX -> placeholder = getPlaceholderMap().get("xbox");
                    case PS4 -> placeholder = getPlaceholderMap().get("playstation");
                    default -> placeholder = getPlaceholderMap().get("bedrock");
                }
            }
            case FABRIC -> placeholder = getPlaceholderMap().get("fabric");
            case FORGE -> placeholder = getPlaceholderMap().get("forge");
            default -> placeholder = getPlaceholderMap().get("vanilla");
        }

        return placeholder;
    }
}
