package xyz.novaserver.placeholders.actionbar;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ActionbarListener implements Listener {
    private final ActionbarManager manager;

    public ActionbarListener(ActionbarManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        manager.onJoin(event.getPlayer());
    }
}
