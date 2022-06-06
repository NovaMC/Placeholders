package xyz.novaserver.placeholders.paper.chat;

import ninja.leaping.configurate.ConfigurationNode;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.paper.Main;

import java.io.File;

public class ChatManager {
    private final Main plugin;
    private final Config config;

    private FormatListener listener;
    private boolean isEnabled;

    private final FancyRenderer fancyRenderer = new FancyRenderer(this);

    public ChatManager(Main plugin) {
        this.plugin = plugin;
        this.config = new Config(plugin, new File(plugin.getDataFolder(), "chat.yml"), "chat.yml");
        reload();
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public ConfigurationNode getConfig() {
        return config.getRoot();
    }

    public Main getPlugin() {
        return plugin;
    }

    public FancyRenderer getFancyRenderer() {
        return fancyRenderer;
    }

    public void reload() {
        boolean success = config.loadConfig();
        if (!success) {
            plugin.logError("Failed to load Chat config file!", null);
        }

        isEnabled = config.getRoot().getNode("enabled").getBoolean(false);
        if (isEnabled && listener == null) {
            listener = new FormatListener(this);
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}
