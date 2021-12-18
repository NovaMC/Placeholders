package xyz.novaserver.placeholders.paper.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.listener.ClientListener;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PaperClientListener extends ClientListener implements Listener {
    private final PlaceholdersPaper plugin;

    private final Map<UUID, Integer> triesMap = new HashMap<>();
    private static final int MAX_TRIES = 8;
    private static final int DELAY = 2;

    public PaperClientListener(PlaceholdersPaper plugin) {
        this.plugin = plugin;
    }

    private void recursiveCheck(UUID uuid) {
        plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
            Player player = plugin.getServer().getPlayer(uuid);
            if (player != null && player.isOnline()) {
                String brand = player.getClientBrandName();
                if (brand != null) {
                    setOptions(uuid, brand.toLowerCase());
                    triesMap.remove(uuid);
                }
                else {
                    if (!triesMap.containsKey(uuid)) {
                        triesMap.put(uuid, 1);
                    } else {
                        triesMap.put(uuid, triesMap.get(uuid) + 1);
                    }

                    if (triesMap.get(uuid) <= MAX_TRIES) {
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
