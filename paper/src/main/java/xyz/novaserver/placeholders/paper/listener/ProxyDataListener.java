package xyz.novaserver.placeholders.paper.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.geysermc.floodgate.util.DeviceOs;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.util.DataConstants;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class ProxyDataListener implements PluginMessageListener {
    private final PlaceholdersPaper plugin;

    public ProxyDataListener(PlaceholdersPaper plugin) {
        this.plugin = plugin;
        Bukkit.getMessenger().registerIncomingPluginChannel(plugin, DataConstants.DATA_CHANNEL, this);
        Bukkit.getMessenger().registerOutgoingPluginChannel(plugin, DataConstants.DATA_CHANNEL);
    }

    public void requestProxyData(String subchannel, UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeUTF(subchannel);
            player.sendPluginMessage(plugin, DataConstants.DATA_CHANNEL, out.toByteArray());
        }
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals(DataConstants.DATA_CHANNEL)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();

        PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

        switch (subchannel) {
            case DataConstants.CHANNEL_PLATFORM -> playerData.setPlatform(PlayerData.Platform.valueOf(in.readUTF()));
            case DataConstants.CHANNEL_DEVICE -> playerData.setDeviceOs(DeviceOs.valueOf(in.readUTF()));
            case DataConstants.CHANNEL_RP -> playerData.setResourcePackApplied(in.readBoolean());
        }
    }
}
