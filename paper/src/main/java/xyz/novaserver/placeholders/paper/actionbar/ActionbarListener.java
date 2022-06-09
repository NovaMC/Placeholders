package xyz.novaserver.placeholders.paper.actionbar;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class ActionbarListener implements Listener {
    private final ActionbarManager manager;

    public ActionbarListener(ActionbarManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.onJoin(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        manager.onQuit(event.getPlayer());
    }

    @EventHandler
    public void onAirChange(EntityAirChangeEvent event) {
        if (event.getEntityType() != EntityType.PLAYER) return;

        Player player = (Player) event.getEntity();
        ActionbarPlayer actionbarPlayer = manager.getActionbarPlayer(player.getUniqueId());
        // Don't hide the actionbar if not requested
        if (!actionbarPlayer.getCurrentActionbar().hide()) return;

        if (event.getAmount() < player.getMaximumAir()) {
            // Cancel task and send actionbar clear if air amount is below maximum
            // (When air bubbles are on screen)
            if (!actionbarPlayer.isCancelled()) {
                actionbarPlayer.cancel();
                actionbarPlayer.clear();
            }
        } else {
            // If air amount reaches maximum again and task is still cancelled
            // start it again (When air bubbles are gone)
            actionbarPlayer.schedule();
        }
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        ActionbarPlayer actionbarPlayer = manager.getActionbarPlayer(player.getUniqueId());
        // Don't hide the actionbar if not requested
        if (!actionbarPlayer.getCurrentActionbar().hide()) return;

        if (event.getNewGameMode() == GameMode.SPECTATOR) {
            if (!actionbarPlayer.isCancelled()) {
                actionbarPlayer.cancel();
                actionbarPlayer.clear();
            }
        } else {
            actionbarPlayer.schedule();
        }
    }
}
