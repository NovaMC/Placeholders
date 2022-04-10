package xyz.novaserver.placeholders.paper;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.common.Expansion;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;
import xyz.novaserver.placeholders.placeholder.type.ServerType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PAPIExpansion extends PlaceholderExpansion implements Relational, Expansion {
    private final Main plugin;
    private Placeholders placeholders;

    private final Map<String, Placeholder> placeholderMap = new HashMap<>();

    public PAPIExpansion(Main plugin) {
        this.plugin = plugin;
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
        if (player == null) return null;

        // Check if placeholder exists & is player placeholder then get value
        String lower = params.toLowerCase();
        if (placeholderMap.containsKey(lower)) {
            if (placeholderMap.get(lower) instanceof PlayerType placeholder) {
                return placeholder.get(placeholders.getData(player.getUniqueId()));
            }
            else if (placeholderMap.get(lower) instanceof ServerType placeholder) {
                return placeholder.get();
            }
        }
        return null;
    }

    @Override
    public String onPlaceholderRequest(Player viewer, Player player, String identifier) {
        if (player == null || viewer == null) return null;

        // Check if placeholder exists & is relational placeholder then get value
        String lower = "rel_" + identifier.toLowerCase();
        if (placeholderMap.containsKey(lower) && placeholderMap.get(lower) instanceof RelationalType placeholder) {
            return placeholder.get(placeholders.getData(viewer.getUniqueId()), placeholders.getData(player.getUniqueId()));
        }
        return null;
    }

    @Override
    public void register(Placeholders placeholders, List<Placeholder> placeholderList) {
        for (Placeholder placeholder : placeholderList) {
            if (placeholder instanceof RelationalType) {
                placeholderMap.put("rel_" + placeholder.getIdentifier(), placeholder);
            } else {
                placeholderMap.put(placeholder.getIdentifier(), placeholder);
            }
        }
        this.placeholders = placeholders;
        this.register();
    }
}
