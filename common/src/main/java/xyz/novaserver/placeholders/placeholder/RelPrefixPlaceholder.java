package xyz.novaserver.placeholders.placeholder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;

public class RelPrefixPlaceholder extends Placeholder implements RelationalType {
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public RelPrefixPlaceholder(Placeholders placeholders) {
        super(placeholders, "prefix");
    }

    @Override
    public String get(PlayerData viewer, PlayerData player) {
        User user = luckPerms.getUserManager().getUser(player.getUuid());

        if (!viewer.isBedrock()) {
            String prefix = user != null ? user.getCachedData().getMetaData().getPrefix() : null;
            return prefix != null ? prefix : "";
        } else {
            String tag = user != null ? user.getCachedData().getMetaData().getMetaValue("tag") : null;
            return tag != null ? tag : "";
        }
    }
}
