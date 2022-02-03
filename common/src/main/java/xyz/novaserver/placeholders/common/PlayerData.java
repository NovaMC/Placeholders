package xyz.novaserver.placeholders.common;

import org.geysermc.floodgate.util.DeviceOs;

public class PlayerData {
    private Platform platform = Platform.DEFAULT;
    private DeviceOs deviceOs = DeviceOs.UNKNOWN;
    private boolean resourcePackApplied = false;
    private String lastMessage = "";

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
