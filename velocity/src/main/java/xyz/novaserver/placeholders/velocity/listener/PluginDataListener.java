package xyz.novaserver.placeholders.velocity.listener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.util.DataConstants;
import xyz.novaserver.placeholders.velocity.PlaceholdersVelocity;

public class PluginDataListener {
    private final PlaceholdersVelocity plugin;
    private final MinecraftChannelIdentifier CHANNEL_ID;

    public PluginDataListener(PlaceholdersVelocity plugin) {
        this.plugin = plugin;
        CHANNEL_ID = MinecraftChannelIdentifier.from(DataConstants.DATA_CHANNEL);
        plugin.getProxy().getChannelRegistrar().register(CHANNEL_ID);
        plugin.getProxy().getEventManager().register(plugin, this);
    }

    public void sendProxyData(ServerConnection connection, String subchannel, Object data) {
        if (data == null) return;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);

        // Should probably find a better solution for this
        if (data instanceof String) {
            out.writeUTF((String) data);
        } else if (data instanceof Boolean) {
            out.writeBoolean((boolean) data);
        } else if (data instanceof Integer) {
            out.writeInt((int) data);
        } else throw new IllegalArgumentException("Unsupported data type " + data.getClass().getSimpleName());

        connection.sendPluginMessage(CHANNEL_ID, out.toByteArray());
    }

    @Subscribe
    public void onPluginMessage(PluginMessageEvent event) {
        if (event.getIdentifier() != CHANNEL_ID) return;

        ByteArrayDataInput in = event.dataAsDataStream();
        String subchannel = in.readUTF();

        if (event.getTarget() instanceof Player player) {
            // Set message as handled
            event.setResult(PluginMessageEvent.ForwardResult.handled());

            if (player.getCurrentServer().isEmpty()) return;
            ServerConnection connection = player.getCurrentServer().get();

            PlayerData playerData = plugin.getPlayerData(player.getUniqueId());

            switch (subchannel) {
                case DataConstants.CHANNEL_PLATFORM -> sendProxyData(connection, subchannel, playerData.getPlatform().name());
                case DataConstants.CHANNEL_DEVICE -> sendProxyData(connection, subchannel, playerData.getDeviceOs().name());
                case DataConstants.CHANNEL_RP -> sendProxyData(connection, subchannel, playerData.isResourcePackApplied());
            }
        }
    }
}
