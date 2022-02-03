package xyz.novaserver.placeholders.paper;

import org.geysermc.floodgate.util.DeviceOs;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.util.DataConstants;
import xyz.novaserver.placeholders.paper.listener.ProxyDataListener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ProxyPlayerData extends PlayerData {
    private final ProxyDataListener proxyConnection;
    private final UUID uuid;

    private final Map<String, Long> cacheMap = new HashMap<>();

    public ProxyPlayerData(PlaceholdersPaper plugin, @NotNull UUID uuid) {
        this.uuid = uuid;
        this.proxyConnection = plugin.getProxyConnection();

        // Initial data request
        cacheMap.put(DataConstants.CHANNEL_PLATFORM, 0L);
        cacheMap.put(DataConstants.CHANNEL_DEVICE, 0L);
        cacheMap.put(DataConstants.CHANNEL_RP, 0L);
    }

    @Override
    public Platform getPlatform() {
        requestData(DataConstants.CHANNEL_PLATFORM);
        return super.getPlatform();
    }

    @Override
    public DeviceOs getDeviceOs() {
        requestData(DataConstants.CHANNEL_DEVICE);
        return super.getDeviceOs();
    }

    @Override
    public boolean isResourcePackApplied() {
        requestData(DataConstants.CHANNEL_RP);
        return super.isResourcePackApplied();
    }

    private void requestData(String channel) {
        long time = System.currentTimeMillis();
        long cacheTime = cacheMap.getOrDefault(channel, time);
        long CACHE_TIMEOUT = 500;

        if (time - cacheTime > CACHE_TIMEOUT) {
            proxyConnection.requestProxyData(channel, uuid);
            cacheMap.put(channel, System.currentTimeMillis());
        }
    }
}
