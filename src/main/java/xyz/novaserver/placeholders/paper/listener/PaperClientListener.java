package xyz.novaserver.placeholders.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.novaserver.placeholders.core.PlaceholderPlayer;
import xyz.novaserver.placeholders.core.listener.ClientListener;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class PaperClientListener extends ClientListener implements Listener {
    private final PlaceholdersPaper plugin;

    public PaperClientListener(PlaceholdersPaper plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void recursiveCheck(UUID uuid) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                String brand = player.getClientBrandName();
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
        }, DELAY * 20L);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        PlaceholderPlayer.getPlayerMap().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        UUID player = event.getPlayer().getUniqueId();
        PlaceholderPlayer.checkAndCreatePlayer(player);
        recursiveCheck(player);
    }
}
