package xyz.novaserver.placeholders.common.util;

import xyz.novaserver.placeholders.common.PlayerData;

public class PlayerDataUtils {
    public static PlayerData.Platform getPlatform(String brand) {
        PlayerData.Platform platform;
        switch (brand) {
            case "fabric" -> platform = PlayerData.Platform.FABRIC;
            case "forge", "fml" -> platform = PlayerData.Platform.FORGE;
            default -> platform = PlayerData.Platform.DEFAULT;
        }
        return platform;
    }
}
