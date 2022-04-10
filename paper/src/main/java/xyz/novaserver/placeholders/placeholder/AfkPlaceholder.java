package xyz.novaserver.placeholders.placeholder;

import com.earth2me.essentials.Essentials;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;

public class AfkPlaceholder extends Placeholder implements RelationalType {
    private final Essentials essentials;

    public AfkPlaceholder(Placeholders plugin) {
        super(plugin, "afk");
        this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    @Override
    public String get(PlayerData viewer, PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig();
        if (!essentials.getUser(player.getUuid()).isAfk()) {
            return "";
        } else {
            return viewer.isResourcePackApplied() ? node.getNode("status", "afk", "rp").getString()
                    : node.getNode("status", "afk", "default").getString();
        }
    }
}
