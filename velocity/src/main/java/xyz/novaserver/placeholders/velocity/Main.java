package xyz.novaserver.placeholders.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.Component;
import org.slf4j.Logger;
import xyz.novaserver.placeholders.common.DummyExpansion;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.Plugin;
import xyz.novaserver.placeholders.common.command.PHCommandExecutor;
import xyz.novaserver.placeholders.common.messaging.PluginPlatform;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.velocity.command.VelocityCommand;
import xyz.novaserver.placeholders.velocity.listener.VelocityClientListener;
import xyz.novaserver.placeholders.velocity.listener.VelocityProxyConnection;

import java.io.File;
import java.nio.file.Path;

public class Main implements Plugin {
    private final ProxyServer proxy;
    private final Logger logger;
    private final Path dataDirectory;

    private Placeholders placeholders;

    @Inject
    public Main(ProxyServer server, Logger logger, @DataDirectory Path dataDirectory) {
        this.proxy = server;
        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        placeholders = new Placeholders(this,
                proxy.getPluginManager().isLoaded("tab") ? new TABExpansion() : new DummyExpansion(),
                new Config(this, new File(dataDirectory.toFile(), "config.yml"), "velocity-config.yml"));

        if (placeholders.isUsingProxyData()) {
            placeholders.setProxyConnection(new VelocityProxyConnection(this));
        }

        this.getProxy().getCommandManager().register("novaphv", new VelocityCommand(new PHCommandExecutor(placeholders)));
        this.getProxy().getEventManager().register(this, new VelocityClientListener(placeholders));

        logInfo("Placeholders finished loading!");
    }

    public ProxyServer getProxy() {
        return proxy;
    }

    @Override
    public Placeholders getPlaceholders() {
        return placeholders;
    }

    @Override
    public PluginPlatform getPlatform() {
        return PluginPlatform.PROXY;
    }

    @Override
    public void sendMessage(Object source, Component component) {
        if (source instanceof CommandSource source1) {
            source1.sendMessage(component);
        }
    }

    @Override
    public void logError(String message, Exception e) {
        if (e != null) {
            logger.error(message, e);
        } else {
            logger.error(message);
        }
    }

    @Override
    public void logInfo(String message) {
        logger.info(message);
    }

    @Override
    public void reload() {}
}
