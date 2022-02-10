package xyz.novaserver.placeholders.common.util;

import xyz.novaserver.placeholders.common.PlayerData;

public class PlayerDataUtils {
    public static PlayerData.Platform getPlatform(String brand) {
        PlayerData.Platform platform;
        if (brand.contains("fabric")) {
            platform = PlayerData.Platform.FABRIC;
        } else if (brand.matches("(?i)fml|forge")) {
            platform = PlayerData.Platform.FORGE;
        } else {
            platform = PlayerData.Platform.DEFAULT;
        }
        return platform;
    }
}
