package xyz.novaserver.placeholders.common.placeholder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.RelationalType;

import java.util.UUID;

public class PrefixPlaceholder extends Placeholder implements RelationalType {
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public PrefixPlaceholder(PlaceholdersPlugin plugin) {
        super(plugin, "prefix", 3000);
    }

    @Override
    public String get(UUID viewer, UUID player) {
        User user = luckPerms.getUserManager().getUser(player);
        PlayerData playerData = getPlugin().getPlayerData(viewer);

        if (playerData != null && playerData.isResourcePackApplied()) {
            String prefix = user.getCachedData().getMetaData().getPrefix();
            return prefix != null ? prefix : "";
        } else {
            String tag = user.getCachedData().getMetaData().getMetaValue("tag");
            return tag != null ? tag : "";
        }
    }
}
