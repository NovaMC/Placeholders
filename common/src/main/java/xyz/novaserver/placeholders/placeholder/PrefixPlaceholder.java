package xyz.novaserver.placeholders.placeholder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;

public class PrefixPlaceholder extends Placeholder implements PlayerType {
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public PrefixPlaceholder(Placeholders plugin) {
        super(plugin, "prefix");
    }

    @Override
    public String get(PlayerData player) {
        User user = luckPerms.getUserManager().getUser(player.getUuid());

        if (player.isResourcePackApplied()) {
            String prefix = user != null ? user.getCachedData().getMetaData().getPrefix() : null;
            return prefix != null ? prefix : "";
        } else {
            String tag = user != null ? user.getCachedData().getMetaData().getMetaValue("tag") : null;
            return tag != null ? tag : "";
        }
    }
}
