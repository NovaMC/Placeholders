package xyz.novaserver.placeholders.common.data;

import org.geysermc.floodgate.util.DeviceOs;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.messaging.DataConstants;
import xyz.novaserver.placeholders.common.messaging.PluginPlatform;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerData {
    private final UUID uuid;

    private Platform platform = Platform.DEFAULT;
    private DeviceOs deviceOs = DeviceOs.UNKNOWN;
    private boolean resourcePackApplied = false;
    private String lastMessage = "";

    public PlayerData(UUID uuid, Placeholders placeholders) {
        this(uuid);
        if (placeholders.isUsingProxyData() && placeholders.getPlugin().getPlatform() == PluginPlatform.SERVER) {
            // Initial proxy data request
            final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
            executor.schedule(() -> {
                placeholders.getProxyConnection().requestData(uuid, DataConstants.CHANNEL_PLATFORM);
                placeholders.getProxyConnection().requestData(uuid, DataConstants.CHANNEL_DEVICE);
                placeholders.getProxyConnection().requestData(uuid, DataConstants.CHANNEL_RP);
            }, 50, TimeUnit.MILLISECONDS);
        }
    }

    public PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Platform getPlatform() {
        return platform;
    }

    public void setPlatform(Platform platform) {
        if (platform != null) this.platform = platform;
    }

    public DeviceOs getDeviceOs() {
        return deviceOs;
    }

    public void setDeviceOs(DeviceOs deviceOs) {
        if (deviceOs != null) this.deviceOs = deviceOs;
    }

    public boolean isResourcePackApplied() {
        return resourcePackApplied;
    }

    public void setResourcePackApplied(boolean resourcePackApplied) {
        this.resourcePackApplied = resourcePackApplied;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public enum Platform {
        FABRIC,
        FORGE,
        BEDROCK,
        DEFAULT
    }
}
