package xyz.novaserver.placeholders.core.placeholder;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import xyz.novaserver.placeholders.core.PlaceholderPlayer;
import xyz.novaserver.placeholders.core.PlaceholdersPlugin;

import java.util.UUID;

public class PrefixPlaceholder extends AbstractRelationalPlaceholder {
    public static String identifier = "prefix";
    private final LuckPerms luckPerms = LuckPermsProvider.get();

    public PrefixPlaceholder(PlaceholdersPlugin plugin) {
        super(identifier, plugin);
    }

    @Override
    public String get(UUID viewer, UUID player) {
        User user = luckPerms.getUserManager().getUser(player);
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(viewer);

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
