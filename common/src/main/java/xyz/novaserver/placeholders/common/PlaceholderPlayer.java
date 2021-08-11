package xyz.novaserver.placeholders.common;

import org.geysermc.floodgate.util.DeviceOs;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceholderPlayer {
    private Platform platform = Platform.DEFAULT;
    private DeviceOs deviceOs = DeviceOs.UNKNOWN;

    private static final Map<UUID, PlaceholderPlayer> playerMap = new HashMap<>();


    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        this.platform = platform;
    }

    public DeviceOs getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(DeviceOs deviceOs) {
        this.deviceOs = deviceOs;
    }


    public static Map<UUID, PlaceholderPlayer> getPlayerMap() {
        return playerMap;
    }

    public static void checkAndCreatePlayer(UUID player) {
        if (!playerMap.containsKey(player)) {
            playerMap.put(player, new PlaceholderPlayer());
        }
    }

    public enum Platform {
        FABRIC,
        FORGE,
        BEDROCK,
        DEFAULT
    }
}
