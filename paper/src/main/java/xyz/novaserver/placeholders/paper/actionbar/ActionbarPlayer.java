package xyz.novaserver.placeholders.paper.actionbar;

import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.geysermc.floodgate.api.FloodgateApi;

public class ActionbarPlayer {
    private ActionbarConfig.Actionbar currentActionbar;
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

    public void setActionbar(ActionbarConfig.Actionbar actionbar) {
        currentActionbar = actionbar;
    }

    public void cancel() {
        actionbarRefresher.cancel();
    }

    public void schedule() {
        if (actionbarRefresher == null || actionbarRefresher.isCancelled()) {
            long interval = currentActionbar.interval();
            String message = currentActionbar.message();

            // Use bedrock-specific data if specified
            if (currentActionbar.bedrock() != null && isFloodgatePlayer(player)) {
                interval = currentActionbar.bedrock().interval();
                message = currentActionbar.bedrock().message();
            }

            // Schedule task for refreshing based on interval from actionbar config
            final String finalMessage = message;
            actionbarRefresher = Bukkit.getScheduler().runTaskTimerAsynchronously(manager.getPlugin(), () -> {
                // Parse the actionbar message and send it to the player
                player.sendActionBar(parseMessage(finalMessage));
            }, 1, interval);
        }
    }

    private Component parseMessage(String message) {
        // Set PAPI placeholders on message
        message = PlaceholderAPI.setPlaceholders(player, message);
        // TODO: RGB support along with legacy ampersand support
        message = message.replaceAll("§", "&");
        // Parse with minimessage and return
        return LegacyComponentSerializer.legacyAmpersand().deserialize(message);
    }

    private boolean isFloodgatePlayer(Player player) {
        return FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId());
    }
}
