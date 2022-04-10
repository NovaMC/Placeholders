package xyz.novaserver.placeholders.common.util;

import xyz.novaserver.placeholders.common.data.PlayerData;

public final class DataUtils {

    public static PlayerData.Platform getPlatform(String brand) {
        if (brand.contains("fabric")) {
            return PlayerData.Platform.FABRIC;
        } else if (brand.matches("(?i)fml|forge")) {
            return PlayerData.Platform.FORGE;
        } else {
            return PlayerData.Platform.DEFAULT;
        }
    }

}
