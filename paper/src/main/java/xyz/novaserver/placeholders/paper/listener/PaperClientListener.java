package xyz.novaserver.placeholders.paper.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
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
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.util.PlayerDataUtils;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class PaperClientListener implements Listener, PluginMessageListener {
    private final PlaceholdersPaper plugin;
    private final FloodgateApi floodgate = FloodgateApi.getInstance();
    private boolean useProxyData = false;

    private final String BRAND_CHANNEL = "minecraft:brand";

    public PaperClientListener(PlaceholdersPaper plugin) {
        this.plugin = plugin;
        if (plugin.getProxyConnection() == null) {
            Bukkit.getMessenger().registerIncomingPluginChannel(plugin, BRAND_CHANNEL, this);
        } else {
            this.useProxyData = true;
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.removePlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.addPlayerData(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        if (useProxyData) return;
        PlayerData playerData = plugin.getPlayerData(event.getPlayer().getUniqueId());

        if (event.getStatus() == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED) {
            playerData.setResourcePackApplied(true);
        } else {
            playerData.setResourcePackApplied(false);
        }
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (useProxyData) return;
        if (!channel.equals(BRAND_CHANNEL)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String brand = in.readUTF();
        UUID uuid = player.getUniqueId();

        // Try to create player data in case this event is fired before PlayerJoinEvent
        plugin.addPlayerData(uuid);

        PlayerData playerData = plugin.getPlayerData(uuid);
        PlayerData.Platform platform = PlayerDataUtils.getPlatform(brand);

        if (floodgate.isFloodgatePlayer(uuid)) {
            platform = PlayerData.Platform.BEDROCK;
            playerData.setDeviceOs(floodgate.getPlayer(uuid).getDeviceOs());
        }

        playerData.setPlatform(platform);
    }
}
