package xyz.novaserver.placeholders.common.placeholder;

import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.PlayerType;

import java.util.UUID;

public class PlatformPlaceholder extends Placeholder implements PlayerType {
    public PlatformPlaceholder(PlaceholdersPlugin plugin) {
        super(plugin,"platform", 5000);
    }

    public String get(UUID player) {
        PlayerData playerData = getPlugin().getPlayerData(player);
        String placeholder = "";

        if (playerData == null) {
            return placeholder;
        }

        switch (playerData.getPlatform()) {
            // Handle player's platform/loader
            case BEDROCK -> {
                // Handle bedrock device type
                switch (playerData.getDeviceOs()) {
                    case NX -> placeholder = getPlugin().getRootValue("switch");
                    case XBOX -> placeholder = getPlugin().getRootValue("xbox");
                    case PS4 -> placeholder = getPlugin().getRootValue("playstation");
                    default -> placeholder = getPlugin().getRootValue("bedrock");
                }
            }
            case FABRIC -> placeholder = getPlugin().getRootValue("fabric");
            case FORGE -> placeholder = getPlugin().getRootValue("forge");
            default -> placeholder = getPlugin().getRootValue("vanilla");
        }

        return placeholder;
    }
}
