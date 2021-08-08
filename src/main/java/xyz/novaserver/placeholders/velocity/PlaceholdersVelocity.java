package xyz.novaserver.placeholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.scheduler.ScheduledTask;
import me.neznamy.tab.api.TABAPI;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import xyz.novaserver.placeholders.core.PlaceholdersPlugin;
import xyz.novaserver.placeholders.velocity.command.VelocityPHCommand;
import xyz.novaserver.placeholders.velocity.listener.VelocityClientListener;
import xyz.novaserver.placeholders.velocity.placeholder.TABPlatformPlaceholder;
import xyz.novaserver.placeholders.core.util.Config;
import xyz.novaserver.placeholders.velocity.placeholder.TABPrefixPlaceholder;
import xyz.novaserver.placeholders.velocity.placeholder.TABTitlePlaceholder;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

@Plugin(id = "placeholders",
        name = "Placeholders",
        version = "1.1.0", authors = {"Nova"},
        dependencies = {
            @Dependency(id = "tab"),
            @Dependency(id = "floodgate")
        })
public class PlaceholdersVelocity implements PlaceholdersPlugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    private Config configuration;

    private ScheduledTask registerTask;

    @Inject
    public PlaceholdersVelocity(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        this.configuration = new Config(this);
        reloadConfiguration();

        getProxy().getCommandManager().register("novaphv", new VelocityPHCommand(this));
        getProxy().getEventManager().register(this, new VelocityClientListener(this));

        this.registerTask = getProxy().getScheduler().buildTask(this, () -> {
            try {
                TABAPI.registerRelationalPlaceholder(new TABPlatformPlaceholder(this));
                logger.info("Registered platform placeholder successfully!");
                TABAPI.registerRelationalPlaceholder(new TABPrefixPlaceholder(this));
                logger.info("Registered prefix placeholder successfully!");
                TABAPI.registerPlayerPlaceholder(new TABTitlePlaceholder(this));
                logger.info("Registered title placeholder successfully!");
                registerTask.cancel();
                logger.info("Canceled task successfully!");
            } catch (NullPointerException ignored) {
                logger.error("Encountered and error while registering placeholders, please wait...");
            }
        }).delay(2, TimeUnit.SECONDS).repeat(2, TimeUnit.SECONDS).schedule();
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    @Override
    public boolean reloadConfiguration() {
        boolean success = configuration.loadConfig(dataDirectory);
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
