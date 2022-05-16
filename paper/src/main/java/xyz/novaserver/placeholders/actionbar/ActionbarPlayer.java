package xyz.novaserver.placeholders.actionbar;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.geysermc.floodgate.api.FloodgateApi;

public class ActionbarPlayer {
    private ActionbarConfig currentActionbar;
    private BukkitTask actionbarRefresher;

    private final ActionbarManager manager;
    private final Player player;

    public ActionbarPlayer(ActionbarManager manager, Player player) {
        this.manager = manager;
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setActionbar(ActionbarConfig actionbar) {
        currentActionbar = actionbar;
    }

    public ActionbarConfig getActionbar() {
        return currentActionbar;
    }

    public void cancel() {
        actionbarRefresher.cancel();
    }

    public void schedule() {
        if (actionbarRefresher == null || actionbarRefresher.isCancelled()) {
            // Schedule task for refreshing based on interval fron actionbar config
            boolean floodgatePlayer = isFloodgatePlayer(player);
            actionbarRefresher = Bukkit.getScheduler().runTaskTimerAsynchronously(manager.getPlugin(), () -> {
                // Parse the actionbar message and send it to the player
                player.sendActionBar(!floodgatePlayer
                        ? parseMessage(currentActionbar.message())
                        : parseMessage(currentActionbar.bedrock().message()));
            }, 1, !floodgatePlayer ? currentActionbar.interval() : currentActionbar.bedrock().interval());
        }
    }

    private Component parseMessage(String message) {
        // Set PAPI placeholders on message
        PlaceholderAPI.setPlaceholders(player, message);
        // Parse with minimessage and return
        return MiniMessage.miniMessage().deserialize(message);
    }

    private boolean isFloodgatePlayer(Player player) {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }
}
