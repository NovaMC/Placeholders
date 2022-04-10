package xyz.novaserver.placeholders.paper.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.geysermc.floodgate.api.FloodgateApi;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.common.util.DataUtils;
import xyz.novaserver.placeholders.paper.Main;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class PaperClientListener implements Listener, PluginMessageListener {
    private final Placeholders placeholders;
    private final FloodgateApi floodgate = FloodgateApi.getInstance();

    private final String BRAND_CHANNEL = "minecraft:brand";

    public PaperClientListener(Main plugin) {
        this.placeholders = plugin.getPlaceholders();
        if (!placeholders.isUsingProxyData()) {
            Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BRAND_CHANNEL, this);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        placeholders.removeData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!placeholders.hasData(uuid)) {
            placeholders.addData(uuid);
        }
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        if (placeholders.isUsingProxyData()) return;
        UUID uuid = event.getPlayer().getUniqueId();

        // Try to create player data in case this event is fired before PlayerJoinEvent
        if (!placeholders.hasData(uuid)) {
            placeholders.addData(uuid);
        }

        PlayerData playerData = placeholders.getData(uuid);
        playerData.setResourcePackApplied(event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (placeholders.isUsingProxyData()) return;
        if (!channel.equals(BRAND_CHANNEL)) return;

        String brand = new String(message, StandardCharsets.UTF_8);
        UUID uuid = player.getUniqueId();

        // Try to create player data in case this event is fired before PlayerJoinEvent
        if (!placeholders.hasData(uuid)) {
            placeholders.addData(uuid);
        }

        PlayerData playerData = placeholders.getData(uuid);
        PlayerData.Platform platform = DataUtils.getPlatform(brand);

        if (floodgate.isFloodgatePlayer(uuid)) {
            platform = PlayerData.Platform.BEDROCK;
            playerData.setDeviceOs(floodgate.getPlayer(uuid).getDeviceOs());
        }
        playerData.setPlatform(platform);
    }
}
