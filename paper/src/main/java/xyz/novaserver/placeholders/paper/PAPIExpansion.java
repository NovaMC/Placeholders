package xyz.novaserver.placeholders.paper;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.PrefixPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.TitlePlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.PlayerType;
import xyz.novaserver.placeholders.common.placeholder.type.RelationalType;
import xyz.novaserver.placeholders.common.placeholder.type.ServerType;
import xyz.novaserver.placeholders.paper.placeholder.AfkPlaceholder;
import xyz.novaserver.placeholders.paper.placeholder.ChatDisplayPlaceholder;
import xyz.novaserver.placeholders.paper.placeholder.VoicePlaceholder;

import java.util.HashMap;
import java.util.Map;

public class PAPIExpansion extends PlaceholderExpansion implements Relational {
    private final PlaceholdersPaper plugin;
    private final Map<String, Placeholder> placeholderMap = new HashMap<>();

    public PAPIExpansion(PlaceholdersPaper plugin) {
        this.plugin = plugin;

        // Player placeholders
        addPlaceholder(new TitlePlaceholder(plugin));
        addPlaceholder(new PlatformPlaceholder(plugin));

        // Relational placeholders
        addPlaceholder(new AfkPlaceholder(plugin));
        addPlaceholder(new VoicePlaceholder(plugin));
        addPlaceholder(new PrefixPlaceholder(plugin));

        if (plugin.getServer().getPluginManager().isPluginEnabled("TAB")) {
            addPlaceholder(new ChatDisplayPlaceholder(plugin));
        }
    }

    private void addPlaceholder(Placeholder placeholder) {
        placeholderMap.put(placeholder.getIdentifier(), placeholder);
    }

    @Override
    public @NotNull String getIdentifier() {
        return "nova";
    }

    @Override
    public @NotNull String getAuthor() {
        return String.join(", ", plugin.getDescription().getAuthors());
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
        // Check if placeholder exists & is player placeholder then get value
        String lower = params.toLowerCase();
        if (placeholderMap.containsKey(lower)) {
            if (player != null && placeholderMap.get(lower) instanceof PlayerType placeholder) {
                return placeholder.get(player.getUniqueId());
            }
            else if (placeholderMap.get(lower) instanceof ServerType placeholder) {
                return placeholder.get();
            }
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
        if (placeholderMap.containsKey(lower) && placeholderMap.get(lower) instanceof RelationalType placeholder) {
            return placeholder.get(viewer.getUniqueId(), player.getUniqueId());
        }

        return null;
    }
}
