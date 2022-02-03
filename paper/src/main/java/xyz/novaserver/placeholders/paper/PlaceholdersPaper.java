package xyz.novaserver.placeholders.paper;

import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.command.PHCommandExecutor;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.paper.command.PaperCommand;
import xyz.novaserver.placeholders.paper.listener.ChatListener;
import xyz.novaserver.placeholders.paper.listener.PaperClientListener;
import xyz.novaserver.placeholders.paper.listener.ProxyDataListener;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceholdersPaper extends JavaPlugin implements PlaceholdersPlugin {
    private Config configuration;
    private PAPIExpansion papi;
    private ProxyDataListener proxyConnection;

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    @Override
    public void onEnable() {
        this.configuration = new Config(this, new File(getDataFolder(), "config.yml"), "paper-config.yml");
        reloadConfiguration();

        this.papi = new PAPIExpansion(this);

        if (getConfiguration().getNode("proxy-data").getBoolean(false)) {
            this.proxyConnection = new ProxyDataListener(this);
        }

        getServer().getPluginCommand("novaph").setExecutor(new PaperCommand(new PHCommandExecutor(this)));
        getServer().getPluginManager().registerEvents(new PaperClientListener(this), this);
        if (this.getServer().getPluginManager().isPluginEnabled("TAB")) {
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }

        this.papi.register();
    }

    public ProxyDataListener getProxyConnection() {
        return proxyConnection;
    }

    @Override
    public PlayerData getPlayerData(UUID player) {
        return playerDataMap.getOrDefault(player, new PlayerData());
    }

    @Override
    public void addPlayerData(UUID player) {
        if (!playerDataMap.containsKey(player))
            playerDataMap.put(player, proxyConnection == null ? new PlayerData() : new ProxyPlayerData(this, player));
    }

    @Override
    public void removePlayerData(UUID player) {
        playerDataMap.remove(player);
    }

    @Override
    public void sendMessage(Object source, Component message) {
        if (source instanceof CommandSender sender) {
            sender.sendMessage(message);
        }
    }

    @Override
    public boolean reloadConfiguration() {
        boolean success = configuration.loadConfig();
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
