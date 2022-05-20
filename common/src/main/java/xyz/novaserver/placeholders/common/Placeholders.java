package xyz.novaserver.placeholders.common;

import ninja.leaping.configurate.ConfigurationNode;
import org.reflections.Reflections;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.common.messaging.ProxyConnection;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class Placeholders {
    private final Plugin plugin;
    private final Config configuration;
    private ProxyConnection proxyConnection;

    private final Map<UUID, PlayerData> playerData = new HashMap<>();
    private final boolean usingProxyData;

    public Placeholders(Plugin plugin, Expansion expansion, Config config) {
        this.plugin = plugin;
        this.configuration = config;

        reloadConfig();
        usingProxyData = getConfig().getNode("use-proxy-data").getBoolean(false);

        // Load placeholders
        final List<Placeholder> placeholderList = new ArrayList<>();
        try {
            Reflections reflections = new Reflections("xyz.novaserver.placeholders.placeholder");
            Set<Class<? extends Placeholder>> classes = reflections.getSubTypesOf(Placeholder.class);
            for (Class<? extends Placeholder> clazz : classes) {
                try {
                    placeholderList.add(clazz.getDeclaredConstructor(Placeholders.class).newInstance(this));
                } catch (NoClassDefFoundError | InvocationTargetException e) {
                    plugin.logInfo("Not loading " + clazz.getSimpleName() + " because of missing dependencies!");
                }
            }
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            plugin.logError("A reflection error was encountered while loading placeholders!", e);
        }
        // Register placeholders
        expansion.register(this, placeholderList);
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public boolean isUsingProxyData() {
        return usingProxyData;
    }

    public PlayerData getData(UUID player) {
        return playerData.getOrDefault(player, new PlayerData(player));
    }

    public boolean hasData(UUID player) {
        return playerData.containsKey(player);
    }

    public void addData(UUID player) {
        playerData.put(player, new PlayerData(player, this));
    }

    public void removeData(UUID player) {
        playerData.remove(player);
    }

    public ProxyConnection getProxyConnection() {
        return proxyConnection;
    }

    public void setProxyConnection(ProxyConnection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public ConfigurationNode getConfig() {
        return configuration.getRoot();
    }

    public boolean reloadConfig() {
        boolean success = configuration.loadConfig();
        if (!success) {
            plugin.logError("Failed to load Placeholders config file!", null);
        }

        return success;
    }

    public boolean reload() {
        // Reload platform-specific features
        plugin.reload();
        return reloadConfig();
    }
}
