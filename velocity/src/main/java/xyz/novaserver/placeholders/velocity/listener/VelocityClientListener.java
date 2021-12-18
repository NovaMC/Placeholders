package xyz.novaserver.placeholders.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerClientBrandEvent;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.listener.ClientListener;

public class VelocityClientListener extends ClientListener {

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        PlaceholderPlayer.getPlayerMap().remove(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        PlaceholderPlayer.checkAndCreatePlayer(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onBrandPacket(PlayerClientBrandEvent event) {
        setOptions(event.getPlayer().getUniqueId(), event.getBrand());
    }
}
