package xyz.novaserver.placeholders.common.placeholder.type;

import xyz.novaserver.placeholders.common.PlaceholdersPlugin;

public class Placeholder {
    private final PlaceholdersPlugin plugin;
    private final String identifier;
    private final int refresh;

    public Placeholder(PlaceholdersPlugin plugin, String identifier, int refresh) {
        this.plugin = plugin;
        this.identifier = identifier;
        this.refresh = refresh;
    }

    public Placeholder(PlaceholdersPlugin plugin, String identifier) {
        this(plugin, identifier, 1000);
    }

    public PlaceholdersPlugin getPlugin() {
        return plugin;
    }

    public String getIdentifier() {
        return identifier;
    }

    public int getRefresh() {
        return refresh;
    }
}
