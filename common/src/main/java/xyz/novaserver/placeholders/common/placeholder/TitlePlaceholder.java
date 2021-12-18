package xyz.novaserver.placeholders.common.placeholder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractPlayerPlaceholder;

import java.util.UUID;

public class TitlePlaceholder extends AbstractPlayerPlaceholder {
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public TitlePlaceholder() {
        super("title", 3000);
    }

    @Override
    public String get(UUID player) {
        User user = luckPerms.getUserManager().getUser(player);
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(player);

        if (pPlayer != null && !pPlayer.getPlatform().equals(PlaceholderPlayer.Platform.BEDROCK)) {
            String prefix = user.getCachedData().getMetaData().getPrefix();
            return prefix != null ? prefix : "";
        }
        else {
            String tag = user.getCachedData().getMetaData().getMetaValue("tag");
            return tag != null ? tag : "";
        }
    }
}
