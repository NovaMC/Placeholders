package xyz.novaserver.placeholders;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.messages.LegacyChannelIdentifier;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;
import com.velocitypowered.api.scheduler.ScheduledTask;
import me.neznamy.tab.api.TABAPI;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.PlayerPlaceholder;
import me.neznamy.tab.shared.placeholders.RelationalPlaceholder;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import xyz.novaserver.placeholders.command.PlaceholdersCommand;
import xyz.novaserver.placeholders.listener.ClientListener;
import xyz.novaserver.placeholders.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.player.PlaceholderPlayer;
import xyz.novaserver.placeholders.util.Config;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Plugin(id = "placeholders",
        name = "Placeholders",
        version = "0.1.0", authors = {"Lui798"},
        dependencies = {
            @Dependency(id = "tab"),
            @Dependency(id = "floodgate")
        })
public class Placeholders {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    private Config configuration;

    private final Map<Player, PlaceholderPlayer> playerMap = new HashMap<>();
    private ScheduledTask registerTask;

    @Inject
    public Placeholders(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.configuration = new Config(this);
        reloadConfig();

        getProxy().getCommandManager().register("placeholders", new PlaceholdersCommand(this));
        getProxy().getEventManager().register(this, new ClientListener(this));

        this.registerTask = getProxy().getScheduler().buildTask(this, () -> {
            try {
                TABAPI.registerRelationalPlaceholder(new PlatformPlaceholder(this));
                logger.info("Registered platform placeholder successfully!");
                registerTask.cancel();
                logger.info("Canceled task successfully!");
            } catch (NullPointerException ignored) {
                logger.error("Encountered and error while registering placeholders, please wait...");
            }
        }).delay(2, TimeUnit.SECONDS).repeat(2, TimeUnit.SECONDS).schedule();
    }

    public boolean reloadConfig() {
        boolean success = configuration.loadConfig(dataDirectory);
        if (!success) {
            logger.error("Failed to load Placeholders config file!");
        }
        return success;
    }
    public ConfigurationNode getConfig() {
        return configuration.getRoot();
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    public Map<Player, PlaceholderPlayer> getPlayerMap() {
        return playerMap;
    }
}
