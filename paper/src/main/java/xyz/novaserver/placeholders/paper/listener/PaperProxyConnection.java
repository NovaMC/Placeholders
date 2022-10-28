package xyz.novaserver.placeholders.paper.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.common.messaging.DataConstants;
import xyz.novaserver.placeholders.common.messaging.ProxyConnection;
import xyz.novaserver.placeholders.paper.Main;

import java.util.UUID;

public class PaperProxyConnection extends ProxyConnection implements PluginMessageListener {
    private final Main plugin;

    public PaperProxyConnection(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, DataConstants.DATA_CHANNEL, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, DataConstants.DATA_CHANNEL);
    }

    @Override
    public void sendPluginMessage(UUID uuid, String channel, byte[] data) {
        Player player = plugin.getServer().getPlayer(uuid);
        if (player != null) player.sendPluginMessage(plugin, channel, data);
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, byte @NotNull [] message) {
        if (!channel.equals(DataConstants.DATA_CHANNEL)) return;
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        PlayerData playerData = plugin.getPlaceholders().getData(player.getUniqueId());
        if (DataConstants.CHANNEL_PLATFORM.equals(subchannel)) {
            playerData.setPlatform(PlayerData.Platform.valueOf(in.readUTF()));
        }
    }
}
