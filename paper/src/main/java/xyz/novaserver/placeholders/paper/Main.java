package xyz.novaserver.placeholders.paper;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.novaserver.placeholders.actionbar.ActionbarManager;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.Plugin;
import xyz.novaserver.placeholders.common.command.PHCommandExecutor;
import xyz.novaserver.placeholders.common.messaging.PluginPlatform;
import xyz.novaserver.placeholders.common.util.Config;
import xyz.novaserver.placeholders.paper.command.PaperCommand;
import xyz.novaserver.placeholders.paper.listener.ChatListener;
import xyz.novaserver.placeholders.paper.listener.PaperClientListener;
import xyz.novaserver.placeholders.paper.listener.PaperProxyConnection;

import java.io.File;

public class Main extends JavaPlugin implements Plugin {
    private Placeholders placeholders;
    private ActionbarManager actionbarManager;

    @Override
    public void onEnable() {
        placeholders = new Placeholders(this, new PAPIExpansion(this),
                new Config(this, new File(getDataFolder(), "config.yml"), "paper-config.yml"));
        actionbarManager = new ActionbarManager(this);

        if (placeholders.isUsingProxyData()) {
            placeholders.setProxyConnection(new PaperProxyConnection(this));
        }

        getServer().getPluginCommand("novaph").setExecutor(new PaperCommand(new PHCommandExecutor(placeholders)));
        getServer().getPluginManager().registerEvents(new PaperClientListener(this), this);
        if (this.getServer().getPluginManager().isPluginEnabled("TAB")) {
            getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        }

        logInfo("Placeholders finished loading!");
    }

    @Override
    public Placeholders getPlaceholders() {
        return placeholders;
    }

    @Override
    public PluginPlatform getPlatform() {
        return PluginPlatform.SERVER;
    }

    @Override
    public void sendMessage(Object source, Component component) {
        if (source instanceof CommandSender sender) {
            sender.sendMessage(component);
        }
    }

    @Override
    public void logError(String message, Exception e) {
        if (e != null) {
            getSLF4JLogger().error(message, e);
        } else {
            getSLF4JLogger().error(message);
        }
    }

    @Override
    public void logInfo(String message) {
        getSLF4JLogger().info(message);
    }

    @Override
    public void reload() {
        if (actionbarManager != null) {
            actionbarManager.reload();
        }
    }
}
