package xyz.novaserver.placeholders.velocity.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import xyz.novaserver.placeholders.common.listener.ClientListener;
import xyz.novaserver.placeholders.velocity.PlaceholdersVelocity;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class VelocityClientListener extends ClientListener {
    private final PlaceholdersVelocity plugin;

    public VelocityClientListener(PlaceholdersVelocity plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void recursiveCheck(UUID uuid) {
        plugin.getProxy().getScheduler().buildTask(plugin, () -> {
            Optional<Player> opPlayer = plugin.getProxy().getPlayer(uuid);
            if (opPlayer.isPresent()) {
                String brand = opPlayer.get().getClientBrand();
                if (brand != null) {
                    setOptions(uuid, brand.toLowerCase());
                    getTriesMap().remove(uuid);
                }
                else {
                    if (!getTriesMap().containsKey(uuid)) {
                        getTriesMap().put(uuid, 1);
                    } else {
                        getTriesMap().put(uuid, getTriesMap().get(uuid) + 1);
                    }

                    if (getTriesMap().get(uuid) <= MAX_TRIES) {
                        recursiveCheck(uuid);
                    }
                }
            }
        }).delay(DELAY, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        PlaceholderPlayer.getPlayerMap().remove(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        PlaceholderPlayer.checkAndCreatePlayer(player);
        recursiveCheck(player);
    }
}
