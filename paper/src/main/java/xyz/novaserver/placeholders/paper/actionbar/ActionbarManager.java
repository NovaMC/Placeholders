package xyz.novaserver.placeholders.paper.actionbar;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.paper.Main;

import java.io.File;
import java.util.*;

public class ActionbarManager {
    private final Main plugin;
    private final ActionbarConfig config;

    private final List<ActionbarConfig.Actionbar> actionbarList = new ArrayList<>();
    private final Map<UUID, ActionbarPlayer> actionbarPlayerSet = new HashMap<>();

    private boolean isEnabled;

    public ActionbarManager(Main plugin) {
        this.plugin = plugin;
        this.config = new ActionbarConfig(this, new Config(this,
                new File(plugin.getDataFolder(), "actionbars.yml"), "actionbars.yml"));
        plugin.getServer().getPluginManager().registerEvents(new ActionbarListener(this), plugin);

        config.loadConfig();
        isEnabled = config.get().getNode("options", "enabled").getBoolean(false);
        if (!isEnabled) return;
        config.loadActionbars(actionbarList);
    }

    public void reload() {
        // Cancel player tasks and clear player actionbar set
        actionbarPlayerSet.values().forEach(ActionbarPlayer::cancel);
        actionbarPlayerSet.clear();
        // Load new config values
        config.loadConfig();
        // If not set to enabled don't load any actionbars
        isEnabled = config.get().getNode("options", "enabled").getBoolean(false);
        if (!isEnabled) return;
        // Load actionbars then load players
        config.loadActionbars(actionbarList);
        for (Player player : Bukkit.getOnlinePlayers()) {
            onJoin(player);
        }
    }

    public void onJoin(Player player) {
        if (!isEnabled) return;
        if (actionbarPlayerSet.containsKey(player.getUniqueId())) {
            return;
        }
        ActionbarPlayer actionbarPlayer = new ActionbarPlayer(this, player);
        actionbarPlayerSet.put(player.getUniqueId(), actionbarPlayer);
        loadActionbar(actionbarPlayer);
    }

    public void onQuit(Player player) {
        if (!isEnabled) return;
        actionbarPlayerSet.get(player.getUniqueId()).cancel();
        actionbarPlayerSet.remove(player.getUniqueId());
    }

    private void loadActionbar(ActionbarPlayer player) {
        actionbarList.forEach(player::setActionbar);
        player.schedule();
    }

    public ActionbarPlayer getActionbarPlayer(UUID player) {
        return actionbarPlayerSet.get(player);
    }

    public Main getPlugin() {
        return plugin;
    }
}
