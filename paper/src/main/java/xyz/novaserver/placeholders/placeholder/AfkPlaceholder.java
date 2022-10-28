package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class AfkPlaceholder extends Placeholder implements RelationalType {
    private final Object essentials;

    public AfkPlaceholder(Placeholders plugin) {
        super(plugin, "afk");
        this.essentials = Bukkit.getPluginManager().getPlugin("Essentials");
    }

    private boolean isAfk(UUID uuid) {
        try {
            Object user = essentials.getClass().getMethod("getUser", UUID.class).invoke(essentials, uuid);
            return (boolean) user.getClass().getMethod("isAfk").invoke(user);
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException e) {
            // Essentials not loaded
            return false;
        }
    }

    @Override
    public String get(PlayerData viewer, PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig();
        if (!isAfk(player.getUuid())) {
            return "";
        } else {
            return !viewer.isBedrock() ? node.getNode("status", "afk", "java").getString()
                    : node.getNode("status", "afk", "bedrock").getString();
        }
    }
}
