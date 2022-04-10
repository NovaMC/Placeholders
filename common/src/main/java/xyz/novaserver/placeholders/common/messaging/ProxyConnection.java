package xyz.novaserver.placeholders.common.messaging;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import xyz.novaserver.placeholders.common.Plugin;

import java.util.UUID;

public abstract class ProxyConnection {
    private final Plugin plugin;

    public ProxyConnection(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    // Send a request for updated data to the proxy
    public void requestData(UUID uuid, String subchannel) {
        if (plugin.getPlatform() == PluginPlatform.PROXY)
            throw new RuntimeException("Data can't be requested from the proxy!");

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        sendPluginMessage(uuid, DataConstants.DATA_CHANNEL, out.toByteArray());
    }

    // Sends updated data to a backend server
    public void sendData(UUID uuid, String subchannel, Object data) {
        if (plugin.getPlatform() == PluginPlatform.SERVER)
            throw new RuntimeException("Data can't be sent from a backend server!");

        if (data == null) return;
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(subchannel);
        writeObject(out, data);
        sendPluginMessage(uuid, DataConstants.DATA_CHANNEL, out.toByteArray());
    }

    private void writeObject(ByteArrayDataOutput out, Object data) {
        // Should probably find a better solution for this
        if (data instanceof String) {
            out.writeUTF((String) data);
        } else if (data instanceof Boolean) {
            out.writeBoolean((boolean) data);
        } else if (data instanceof Integer) {
            out.writeInt((int) data);
        } else if (data instanceof byte[])  {
            out.write((byte[]) data);
        } else throw new IllegalArgumentException("Unsupported data type " + data.getClass().getSimpleName());
    }

    public abstract void sendPluginMessage(UUID uuid, String channel, byte[] data);
}
