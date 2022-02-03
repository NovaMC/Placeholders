package xyz.novaserver.placeholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import me.neznamy.tab.api.TabAPI;
import net.kyori.adventure.text.Component;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.command.PHCommandExecutor;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.velocity.command.VelocityCommand;
import xyz.novaserver.placeholders.velocity.listener.PluginDataListener;
import xyz.novaserver.placeholders.velocity.listener.VelocityClientListener;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlaceholdersVelocity implements PlaceholdersPlugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    private Config configuration;
    private PlaceholderRegistry registry;
    private PluginDataListener serverConnection;

    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

    @Inject
    public PlaceholdersVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.configuration = new Config(this, new File(dataDirectory.toFile(), "config.yml"), "velocity-config.yml");
        reloadConfiguration();

        this.registry = new PlaceholderRegistry(this);

        getProxy().getCommandManager().register("novaphv", new VelocityCommand(new PHCommandExecutor(this)));
        getProxy().getEventManager().register(this, new VelocityClientListener(this));

        if (getConfiguration().getNode("proxy-data").getBoolean(false)) {
            this.serverConnection = new PluginDataListener(this);
        }

        TabAPI.getInstance().getEventBus().register(registry);
        registry.registerPlaceholders();
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    @Override
    public PlayerData getPlayerData(UUID player) {
        return playerDataMap.getOrDefault(player, new PlayerData());
    }

    @Override
    public void addPlayerData(UUID player) {
        if (!playerDataMap.containsKey(player))
            playerDataMap.put(player, new PlayerData());
    }

    @Override
    public void removePlayerData(UUID player) {
        playerDataMap.remove(player);
    }

    @Override
    public void sendMessage(Object source, Component message) {
        if (source instanceof CommandSource cmdSource) {
            cmdSource.sendMessage(message);
        }
    }

    @Override
    public boolean reloadConfiguration() {
        boolean success = configuration.loadConfig();
        if (!success) {
            logger.error("Failed to load Placeholders config file!");
        }
        return success;
    }

    @Override
    public ConfigurationNode getConfiguration() {
        return configuration.getRoot();
    }
}
