package xyz.novaserver.placeholders.actionbar;

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

    public ActionbarManager(Main plugin) {
        this.plugin = plugin;
        this.config = new ActionbarConfig(this, new Config(this,
                new File(plugin.getDataFolder(), "actionbars.yml"), "actionbars.yml"));

        config.loadConfig();
        if (!config.get().getNode("options", "enabled").getBoolean(false)) {
            return;
        }
        config.loadActionbars(actionbarList);
        plugin.getServer().getPluginManager().registerEvents(new ActionbarListener(this), plugin);
    }

    public void reload() {
        config.loadConfig();
        config.loadActionbars(actionbarList);
        actionbarPlayerSet.values().forEach(ActionbarPlayer::cancel);
        actionbarPlayerSet.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onJoin(player);
        }
    }

    public void onJoin(Player player) {
        if (actionbarPlayerSet.containsKey(player.getUniqueId())) {
            return;
        }
        ActionbarPlayer actionbarPlayer = new ActionbarPlayer(this, player);
        actionbarPlayerSet.put(player.getUniqueId(), actionbarPlayer);
        loadActionbar(actionbarPlayer);
    }

    public void onQuit(Player player) {
        actionbarPlayerSet.get(player.getUniqueId()).cancel();
        actionbarPlayerSet.remove(player.getUniqueId());
    }

    private void loadActionbar(ActionbarPlayer player) {
        actionbarList.forEach(player::setActionbar);
        player.schedule();
    }

    public Main getPlugin() {
        return plugin;
    }
}
