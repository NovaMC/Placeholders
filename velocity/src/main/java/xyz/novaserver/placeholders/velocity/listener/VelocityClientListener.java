package xyz.novaserver.placeholders.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import com.velocitypowered.api.event.player.PlayerResourcePackStatusEvent;
import org.geysermc.floodgate.api.FloodgateApi;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.util.PlayerDataUtils;

import java.util.UUID;

public class VelocityClientListener {
    private final PlaceholdersPlugin plugin;
    private final FloodgateApi floodgate = FloodgateApi.getInstance();

    public VelocityClientListener(PlaceholdersPlugin plugin) {
        this.plugin = plugin;
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        plugin.removePlayerData(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        plugin.addPlayerData(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onBrandPacket(PlayerClientBrandEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();

        // Try to create player data in case this event is fired before PostLoginEvent
        plugin.addPlayerData(uuid);

        PlayerData playerData = plugin.getPlayerData(uuid);
        PlayerData.Platform platform = PlayerDataUtils.getPlatform(event.getBrand());

        if (floodgate.isFloodgatePlayer(uuid)) {
            platform = PlayerData.Platform.BEDROCK;
            playerData.setDeviceOs(floodgate.getPlayer(uuid).getDeviceOs());
        }

        playerData.setPlatform(platform);
    }

    @Subscribe
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        // Try to create player data in case this event is fired before PostLoginEvent
        plugin.addPlayerData(event.getPlayer().getUniqueId());

        PlayerData playerData = plugin.getPlayerData(event.getPlayer().getUniqueId());

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFUL) {
            playerData.setResourcePackApplied(true);
        } else {
            playerData.setResourcePackApplied(false);
        }
    }
}
