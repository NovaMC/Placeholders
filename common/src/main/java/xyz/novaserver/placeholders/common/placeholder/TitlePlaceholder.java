package xyz.novaserver.placeholders.common.placeholder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.PlayerData;
import xyz.novaserver.placeholders.common.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.common.placeholder.type.PlayerType;

import java.util.UUID;

public class TitlePlaceholder extends Placeholder implements PlayerType {
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public TitlePlaceholder(PlaceholdersPlugin plugin) {
        super(plugin, "title", 3000);
    }

    @Override
    public String get(UUID player) {
        User user = luckPerms.getUserManager().getUser(player);
        PlayerData playerData = getPlugin().getPlayerData(player);

        if (playerData != null && playerData.isResourcePackApplied()) {
            String prefix = user.getCachedData().getMetaData().getPrefix();
            return prefix != null ? prefix : "";
        } else {
            String tag = user.getCachedData().getMetaData().getMetaValue("tag");
            return tag != null ? tag : "";
        }
    }
}
