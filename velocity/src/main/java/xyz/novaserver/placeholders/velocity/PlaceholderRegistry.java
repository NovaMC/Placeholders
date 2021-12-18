package xyz.novaserver.placeholders.velocity;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.placeholder.PlaceholderManager;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.PrefixPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.TitlePlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractPlayerPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.BasePlaceholder;

import java.util.HashMap;
import java.util.Map;

public class PlaceholderRegistry {
    private static final Map<String, BasePlaceholder> placeholderMap = new HashMap<>();

    private static void addPlaceholder(BasePlaceholder placeholder) {
        placeholderMap.put(placeholder.getIdentifier(), placeholder);
    }

    private static void setupPlaceholders(PlaceholdersPlugin plugin) {
        // Player placeholders
        addPlaceholder(new TitlePlaceholder());

        // Relational placeholders
        addPlaceholder(new PlatformPlaceholder(plugin));
        addPlaceholder(new PrefixPlaceholder());
    }

    protected static void registerPlaceholders(PlaceholdersPlugin plugin) {
        PlaceholderManager tabManager = TabAPI.getInstance().getPlaceholderManager();

        setupPlaceholders(plugin);

        placeholderMap.values().forEach(p -> {
            if (p instanceof AbstractRelationalPlaceholder) {
                tabManager.registerRelationalPlaceholder("%rel_" + p.getIdentifier() + "%", p.getRefresh(),
                        (viewer, target) -> ((AbstractRelationalPlaceholder) p).get(viewer.getUniqueId(), target.getUniqueId()));
            }
            else if (p instanceof AbstractPlayerPlaceholder) {
                tabManager.registerPlayerPlaceholder("%" + p.getIdentifier() + "%", p.getRefresh(),
                        (player) -> ((AbstractPlayerPlaceholder) p).get(player.getUniqueId()));
            }
        });
    }
}
