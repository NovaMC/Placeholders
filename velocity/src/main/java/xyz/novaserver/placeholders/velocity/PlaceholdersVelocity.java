package xyz.novaserver.placeholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Dependency;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import ninja.leaping.configurate.ConfigurationNode;
import org.slf4j.Logger;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.command.PHCommand;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.velocity.command.VelocityCommand;
import xyz.novaserver.placeholders.velocity.listener.VelocityClientListener;

import java.nio.file.Path;

@Plugin(id = "placeholders",
        name = "Placeholders",
        version = "1.1.1-SNAPSHOT", authors = {"Nova"},
        dependencies = {
            @Dependency(id = "tab"),
            @Dependency(id = "floodgate")
        })
public class PlaceholdersVelocity implements PlaceholdersPlugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;
    private Config configuration;

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

        getProxy().getCommandManager().register("novaphv", new VelocityCommand(new PHCommand(this)));
        getProxy().getEventManager().register(this, new VelocityClientListener());

        PlaceholderRegistry.registerPlaceholders(this);
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
