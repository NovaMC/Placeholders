package xyz.novaserver.placeholders.paper;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.expansion.Relational;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import xyz.novaserver.placeholders.core.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.core.placeholder.PrefixPlaceholder;
import xyz.novaserver.placeholders.core.placeholder.TitlePlaceholder;
import xyz.novaserver.placeholders.paper.placeholder.AfkPlaceholder;

public class PlaceholdersPAPI extends PlaceholderExpansion implements Relational {
    private final PlaceholdersPaper plugin;

    public PlaceholdersPAPI(PlaceholdersPaper plugin) {
        this.plugin = plugin;
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
        return "1.1.0";
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

        if (params.equalsIgnoreCase("title")) {
            return new TitlePlaceholder(plugin).get(player.getUniqueId());
        }

        return null;
    }

    @Override
    public String onPlaceholderRequest(Player viewer, Player player, String identifier) {
        if (player == null || viewer == null) {
            return null;
        }

        if (identifier.equalsIgnoreCase("platform")) {
            return new PlatformPlaceholder(plugin).get(viewer.getUniqueId(), player.getUniqueId());
        }
        else if (identifier.equalsIgnoreCase("prefix")) {
            return new PrefixPlaceholder(plugin).get(viewer.getUniqueId(), player.getUniqueId());
        }
        else if (identifier.equalsIgnoreCase("afk")) {
            return new AfkPlaceholder(plugin).get(viewer.getUniqueId(), player.getUniqueId());
        }

        return null;
    }
}
