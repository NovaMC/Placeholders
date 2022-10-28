package xyz.novaserver.placeholders.common.util;

import xyz.novaserver.placeholders.common.data.PlayerData;

public final class DataUtils {

    public static PlayerData.Platform getPlatform(String brand) {
        switch (brand) {
            case "quilt" -> {
                return PlayerData.Platform.QUILT;
            } case "fabric" -> {
                return PlayerData.Platform.FABRIC;
            } case "forge" -> {
                return PlayerData.Platform.FORGE;
            } case "Geyser" -> {
                return PlayerData.Platform.BEDROCK;
            } default -> {
                return PlayerData.Platform.DEFAULT;
            }
        }
    }

}
