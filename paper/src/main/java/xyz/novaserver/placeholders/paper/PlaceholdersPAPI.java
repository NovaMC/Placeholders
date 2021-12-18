package xyz.novaserver.placeholders.paper;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.PrefixPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.TitlePlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractPlayerPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.BasePlaceholder;
import xyz.novaserver.placeholders.paper.placeholder.AfkPlaceholder;
import xyz.novaserver.placeholders.paper.placeholder.ChatDisplayPlaceholder;
import xyz.novaserver.placeholders.paper.placeholder.VoicePlaceholder;

import java.util.HashMap;
import java.util.Map;

public class PlaceholdersPAPI extends PlaceholderExpansion implements Relational {
    private final PlaceholdersPaper plugin;
    private final Map<String, BasePlaceholder> placeholderMap = new HashMap<>();

    public PlaceholdersPAPI(PlaceholdersPaper plugin) {
        this.plugin = plugin;

        // Player placeholders
        addPlaceholder(new TitlePlaceholder());
        addPlaceholder(new PlatformPlaceholder(plugin));

        // Relational placeholders
        addPlaceholder(new AfkPlaceholder(plugin));
        addPlaceholder(new VoicePlaceholder(plugin));
        addPlaceholder(new PrefixPlaceholder());

        if (plugin.getServer().getPluginManager().isPluginEnabled("TAB")) {
            addPlaceholder(new ChatDisplayPlaceholder());
        }
    }

    private void addPlaceholder(BasePlaceholder placeholder) {
        placeholderMap.put(placeholder.getIdentifier(), placeholder);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nova";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Nova";
    }

    @Override
    public @NotNull String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null) {
            return null;
        }

        // Check if placeholder exists & is player placeholder then get value
        String lower = params.toLowerCase();
        if (placeholderMap.containsKey(lower) && placeholderMap.get(lower) instanceof AbstractPlayerPlaceholder) {
            return ((AbstractPlayerPlaceholder) placeholderMap.get(lower)).get(player.getUniqueId());
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player viewer, Player player, String identifier) {
        if (player == null || viewer == null) {
            return null;
        }

        // Check if placeholder exists & is relational placeholder then get value
        String lower = identifier.toLowerCase();
        if (placeholderMap.containsKey(lower) && placeholderMap.get(lower) instanceof AbstractRelationalPlaceholder) {
            return ((AbstractRelationalPlaceholder) placeholderMap.get(lower)).get(viewer.getUniqueId(), player.getUniqueId());
        }

        return null;
    }
}
