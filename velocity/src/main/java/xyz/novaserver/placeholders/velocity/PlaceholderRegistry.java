package xyz.novaserver.placeholders.velocity;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.event.Subscribe;
import me.neznamy.tab.api.event.plugin.TabLoadEvent;
import me.neznamy.tab.api.placeholder.PlaceholderManager;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.PrefixPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.TitlePlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.PlayerType;
import xyz.novaserver.placeholders.common.placeholder.type.RelationalType;
import xyz.novaserver.placeholders.common.placeholder.type.ServerType;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderRegistry {
    private final PlaceholdersPlugin plugin;
    private final List<Placeholder> placeholders = new ArrayList<>();

    private boolean registered = false;

    public PlaceholderRegistry(PlaceholdersPlugin plugin) {
        this.plugin = plugin;

        // Player placeholders
        addPlaceholder(new TitlePlaceholder(plugin));
        addPlaceholder(new PlatformPlaceholder(plugin));

        // Relational placeholders
        addPlaceholder(new PrefixPlaceholder(plugin));
    }

    private void addPlaceholder(Placeholder placeholder) {
        placeholders.add(placeholder);
    }

    protected void registerPlaceholders() {
        if (registered) return;
        PlaceholderManager tabManager = TabAPI.getInstance().getPlaceholderManager();

        for (Placeholder placeholder : placeholders) {
            if (placeholder instanceof RelationalType p) {
                tabManager.registerRelationalPlaceholder("%rel_" + placeholder.getIdentifier() + "%", placeholder.getRefresh(),
                        (viewer, target) -> p.get(viewer.getUniqueId(), target.getUniqueId()));
            }
            else if (placeholder instanceof PlayerType p) {
                tabManager.registerPlayerPlaceholder("%" + placeholder.getIdentifier() + "%", placeholder.getRefresh(),
                        (player) -> p.get(player.getUniqueId()));
            }
            else if (placeholder instanceof ServerType p) {
                tabManager.registerServerPlaceholder("%" + placeholder.getIdentifier() + "%", placeholder.getRefresh(), p::get);
            }
        }
        registered = true;
    }

    @Subscribe
    public void onTabLoad(TabLoadEvent event) {
        // Re-register placeholders when TAB reloads
        if (registered) {
            registered = false;
            registerPlaceholders();
        }
    }
}
