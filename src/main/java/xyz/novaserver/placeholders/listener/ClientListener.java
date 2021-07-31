package xyz.novaserver.placeholders.listener;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.PlayerResourcePackStatusEvent;
import com.velocitypowered.api.proxy.Player;
import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import xyz.novaserver.placeholders.Placeholders;
import xyz.novaserver.placeholders.player.PlaceholderPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class ClientListener {
    private final Placeholders plugin;

    private final Map<UUID, Integer> triesMap = new HashMap<>();
    private final int MAX_TRIES = 8;

    public ClientListener(Placeholders plugin) {
        this.plugin = plugin;
    }

    private void checkAndCreatePlayer(Player player) {
        if (!plugin.getPlayerMap().containsKey(player)) {
            plugin.getPlayerMap().put(player, new PlaceholderPlayer());
        }
    }

    private void setOptions(Player player, String brand) {
        FloodgateApi floodgate = FloodgateApi.getInstance();

        if (brand.contains("geyser") || floodgate.isFloodgatePlayer(player.getUniqueId())) {
            FloodgatePlayer fPlayer = floodgate.getPlayer(player.getUniqueId());
            if (fPlayer.getDeviceOs() != null) {
                plugin.getPlayerMap().get(player).setDeviceOs(fPlayer.getDeviceOs());
            }
            plugin.getPlayerMap().get(player).setUsingBedrock(true);
        }
        if (brand.contains("fabric")) {
            plugin.getPlayerMap().get(player).setUsingFabric(true);
        }
        if (brand.matches("(?i)fml|forge")) {
            plugin.getPlayerMap().get(player).setUsingForge(true);
        }
    }

    private void recursiveCheck(UUID uuid) {
        plugin.getProxy().getScheduler().buildTask(plugin, () -> {
            Optional<Player> opPlayer = plugin.getProxy().getPlayer(uuid);
            if (opPlayer.isPresent()) {
                Player player = opPlayer.get();
                String brand = player.getClientBrand();
                if (brand != null) {
                    setOptions(player, brand.toLowerCase());
                    triesMap.remove(player.getUniqueId());
                }
                else {
                    if (!triesMap.containsKey(player.getUniqueId())) {
                        triesMap.put(player.getUniqueId(), 1);
                    } else {
                        triesMap.put(player.getUniqueId(), triesMap.get(player.getUniqueId()) + 1);
                    }

                    if (triesMap.get(player.getUniqueId()) <= MAX_TRIES) {
                        if (plugin.getProxy().getPlayer(player.getUniqueId()).isPresent()) {
                            recursiveCheck(player.getUniqueId());
                        }
                    }
                }
            }
        }).delay(2, TimeUnit.SECONDS).schedule();
    }

    @Subscribe
    public void onDisconnect(DisconnectEvent event) {
        plugin.getPlayerMap().remove(event.getPlayer());
    }

    @Subscribe
    public void onLogin(PostLoginEvent event) {
        checkAndCreatePlayer(event.getPlayer());
        recursiveCheck(event.getPlayer().getUniqueId());
    }

    @Subscribe
    public void onResourcePackStatus(PlayerResourcePackStatusEvent event) {
        if (event.getStatus().equals(PlayerResourcePackStatusEvent.Status.SUCCESSFUL)) {
            checkAndCreatePlayer(event.getPlayer());
            plugin.getPlayerMap().get(event.getPlayer()).setAcceptedPack(true);
        }
    }
}
