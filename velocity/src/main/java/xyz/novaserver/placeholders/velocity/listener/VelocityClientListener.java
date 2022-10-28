package xyz.novaserver.placeholders.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import org.geysermc.floodgate.api.FloodgateApi;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.common.messaging.DataConstants;
import xyz.novaserver.placeholders.common.util.DataUtils;

import java.util.UUID;

public class VelocityClientListener {
    private final Placeholders placeholders;
    private final FloodgateApi floodgate = FloodgateApi.getInstance();

    public VelocityClientListener(Placeholders placeholders) {
        this.placeholders = placeholders;
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        placeholders.removeData(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!placeholders.hasData(uuid)) {
            placeholders.addData(uuid);
        }

        PlayerData playerData = placeholders.getData(uuid);
        if (floodgate.isFloodgatePlayer(uuid)) {
            playerData.setPlatform(PlayerData.Platform.BEDROCK);
            playerData.setDeviceOs(floodgate.getPlayer(uuid).getDeviceOs());
        }
    }

    @Subscribe
    public void onBrandPacket(PlayerClientBrandEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        // Try to create player data in case this event is fired before PostLoginEvent
        if (!placeholders.hasData(uuid)) {
            placeholders.addData(uuid);
        }

        PlayerData playerData = placeholders.getData(uuid);
        PlayerData.Platform platform = DataUtils.getPlatform(event.getBrand());

        if (floodgate.isFloodgatePlayer(uuid)) {
            platform = PlayerData.Platform.BEDROCK;
            playerData.setDeviceOs(floodgate.getPlayer(uuid).getDeviceOs());
        }
        playerData.setPlatform(platform);

        // Send updated data to backend server
        if (placeholders.isUsingProxyData()) {
            placeholders.getProxyConnection().sendData(uuid, DataConstants.CHANNEL_PLATFORM, playerData.getPlatform().name());
        }
    }
}
