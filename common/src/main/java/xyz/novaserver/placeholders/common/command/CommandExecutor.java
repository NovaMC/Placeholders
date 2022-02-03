package xyz.novaserver.placeholders.common.command;

import xyz.novaserver.placeholders.common.PlaceholdersPlugin;

import java.util.List;

public abstract class CommandExecutor {
    private final PlaceholdersPlugin plugin;

    public CommandExecutor(PlaceholdersPlugin plugin) {
        this.plugin = plugin;
    }

    public PlaceholdersPlugin getPlugin() {
        return plugin;
    }

    public abstract void execute(String[] args, Object source);

    public abstract List<String> suggest(String[] args);

    public abstract String getPermission();
}
