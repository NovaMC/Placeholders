package xyz.novaserver.placeholders.paper.placeholder;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.RelationalType;

import java.util.UUID;

public class AfkPlaceholder extends Placeholder implements RelationalType {
    private final Essentials essentials;

    public AfkPlaceholder(PlaceholdersPlugin plugin) {
        super(plugin, "afk");
        this.essentials = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
    }

    @Override
    public String get(UUID viewer, UUID player) {
        User user = essentials.getUser(player);
        PlayerData playerData = getPlugin().getPlayerData(viewer);

        if (!user.isAfk()) {
            return "";
        } else {
            if (playerData != null && playerData.isResourcePackApplied()) {
                return getPlugin().getRootValue("afk-rp");
            } else {
                return getPlugin().getRootValue("afk-vanilla");
            }
        }
    }
}
