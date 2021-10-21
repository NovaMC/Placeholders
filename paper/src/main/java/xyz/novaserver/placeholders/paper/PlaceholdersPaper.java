package xyz.novaserver.placeholders.paper;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.command.PHCommand;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.paper.command.PaperCommand;
import xyz.novaserver.placeholders.paper.listener.ChatListener;
import xyz.novaserver.placeholders.paper.listener.PaperClientListener;

public class PlaceholdersPaper extends JavaPlugin implements PlaceholdersPlugin {
    private Config configuration;

    @Override
    public void onEnable() {
        this.configuration = new Config(this);
        reloadConfiguration();

        getServer().getPluginCommand("novaph").setExecutor(new PaperCommand(new PHCommand(this)));
        getServer().getPluginManager().registerEvents(new PaperClientListener(this), this);
        if (this.getServer().getPluginManager().isPluginEnabled("TAB")) {
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }

        new PlaceholdersPAPI(this).register();
    }

    @Override
    public boolean reloadConfiguration() {
        boolean success = configuration.loadConfig(getDataFolder().toPath());
        if (!success) {
            getLogger().severe("Failed to load Placeholders config file!");
        }
        return success;
    }

    @Override
    public ConfigurationNode getConfiguration() {
        return configuration.getRoot();
    }
}
