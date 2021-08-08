package xyz.novaserver.placeholders.paper;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.novaserver.placeholders.core.PlaceholdersPlugin;
import xyz.novaserver.placeholders.core.util.Config;
import xyz.novaserver.placeholders.paper.command.PaperPHCommand;
import xyz.novaserver.placeholders.paper.listener.PaperClientListener;

public class PlaceholdersPaper extends JavaPlugin implements PlaceholdersPlugin {
    private Config configuration;

    @Override
    public void onEnable() {
        this.configuration = new Config(this);
        reloadConfiguration();

        getServer().getPluginCommand("novaph").setExecutor(new PaperPHCommand(this));
        getServer().getPluginManager().registerEvents(new PaperClientListener(this), this);

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
