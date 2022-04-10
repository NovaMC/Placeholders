package xyz.novaserver.placeholders.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.common.messaging.DataConstants;
import xyz.novaserver.placeholders.common.messaging.ProxyConnection;
import xyz.novaserver.placeholders.velocity.Main;

import java.util.Optional;
import java.util.UUID;

public class VelocityProxyConnection extends ProxyConnection {
    private final Main plugin;

    public VelocityProxyConnection(Main plugin) {
        super(plugin);
        this.plugin = plugin;
        MinecraftChannelIdentifier DATA_CHANNEL = MinecraftChannelIdentifier.from(DataConstants.DATA_CHANNEL);
        plugin.getProxy().getChannelRegistrar().register(DATA_CHANNEL);
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    @Override
    public void sendPluginMessage(UUID uuid, String channel, byte[] data) {
        Optional<Player> player = plugin.getProxy().getPlayer(uuid);
        if (player.isPresent()) {
            Optional<ServerConnection> connection = player.get().getCurrentServer();
            connection.ifPresent(serverConnection -> serverConnection.sendPluginMessage(MinecraftChannelIdentifier.from(channel), data));
        }
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (!event.getIdentifier().getId().equals(DataConstants.DATA_CHANNEL)
                || !(event.getTarget() instanceof Player player)) return;

        UUID uuid = player.getUniqueId();
        ByteArrayDataInput in = event.dataAsDataStream();
        String subchannel = in.readUTF();

        PlayerData playerData = plugin.getPlaceholders().getData(uuid);
        switch (subchannel) {
            case DataConstants.CHANNEL_PLATFORM -> sendData(uuid, subchannel, playerData.getPlatform().name());
            case DataConstants.CHANNEL_DEVICE -> sendData(uuid, subchannel, playerData.getDeviceOs().name());
            case DataConstants.CHANNEL_RP -> sendData(uuid, subchannel, playerData.isResourcePackApplied());
        }

        // Set message as handled
        event.setResult(PluginMessageEvent.ForwardResult.handled());
    }
}
