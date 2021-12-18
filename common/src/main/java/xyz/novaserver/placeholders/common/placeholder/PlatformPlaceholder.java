package xyz.novaserver.placeholders.common.placeholder;

import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;

import java.util.Map;
import java.util.UUID;

public class PlatformPlaceholder extends AbstractRelationalPlaceholder {
    private final PlaceholdersPlugin plugin;

    public PlatformPlaceholder(PlaceholdersPlugin plugin) {
        super("platform", 5000);
        this.plugin = plugin;
    }

    public String get(UUID viewer, UUID player) {
        Map<String, String> placeholderMap = plugin.getPlaceholderMap();
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(player);
        PlaceholderPlayer pViewer = PlaceholderPlayer.getPlayerMap().get(viewer);
        String placeholder = "";

        if (pPlayer == null || pViewer == null || pViewer.getPlatform().equals(PlaceholderPlayer.Platform.BEDROCK)) {
            return placeholder;
        }

        switch (pPlayer.getPlatform()) {
            // Handle player's platform/loader
            case BEDROCK -> {
                // Handle bedrock device type
                switch (pPlayer.getDeviceOs()) {
                    case NX -> placeholder = placeholderMap.get("switch");
                    case XBOX -> placeholder = placeholderMap.get("xbox");
                    case PS4 -> placeholder = placeholderMap.get("playstation");
                    default -> placeholder = placeholderMap.get("bedrock");
                }
            }
            case FABRIC -> placeholder = placeholderMap.get("fabric");
            case FORGE -> placeholder = placeholderMap.get("forge");
            default -> placeholder = placeholderMap.get("vanilla");
        }

        return placeholder;
    }
}
