package xyz.novaserver.placeholders.actionbar;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.paper.Main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ActionbarManager {
    private final Main plugin;
    private final Config config;

    private final List<ActionbarConfig> actionBars = new ArrayList<>();
    private final Set<ActionbarPlayer> actionbarPlayerSet = new HashSet<>();

    public ActionbarManager(Main plugin, Config config) {
        this.plugin = plugin;
        this.config = config;

        reloadConfig();
    }

    public void reload() {
        reloadConfig();
        actionbarPlayerSet.forEach(ActionbarPlayer::cancel);
        actionbarPlayerSet.clear();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onJoin(player);
        }
    }

    private void reloadConfig() {
        boolean success = config.loadConfig();
        if (!success) {
            plugin.logError("Failed to load Actionbars config file!", null);
        }

        actionBars.clear();
        config.getRoot().getChildrenList().forEach(node -> {
            try {
                actionBars.add(config.getRoot().getValue(TypeToken.of(ActionbarConfig.class)));
            } catch (ObjectMappingException e) {
                plugin.logError("Mapping exception occurred while trying to deserialize config!", e);
            }
        });
    }

    public void onJoin(Player player) {
        for (ActionbarPlayer actionbarPlayer : actionbarPlayerSet) {
            if (actionbarPlayer.getPlayer() == player) {
                return;
            }
        }
        ActionbarPlayer actionbarPlayer = new ActionbarPlayer(this, player);
        actionbarPlayerSet.add(actionbarPlayer);
        loadActionbar(actionbarPlayer);
    }

    private void loadActionbar(ActionbarPlayer player) {
        actionBars.forEach(player::setActionbar);
        player.schedule();
    }

    public Main getPlugin() {
        return plugin;
    }
}
