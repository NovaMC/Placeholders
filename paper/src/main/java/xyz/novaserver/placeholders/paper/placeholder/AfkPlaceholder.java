package xyz.novaserver.placeholders.paper.placeholder;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;
import xyz.novaserver.placeholders.common.PlaceholdersPlugin;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;
import xyz.novaserver.placeholders.paper.PlaceholdersPaper;

import java.util.UUID;

public class AfkPlaceholder extends AbstractRelationalPlaceholder {
    private final PlaceholdersPlugin plugin;
    private final Essentials essentials;

    public AfkPlaceholder(PlaceholdersPaper plugin) {
        super("afk");
        this.plugin = plugin;
        this.essentials = (Essentials) plugin.getServer().getPluginManager().getPlugin("Essentials");
    }

    @Override
    public String get(UUID viewer, UUID player) {
        User user = essentials.getUser(player);
        PlaceholderPlayer pPlayer = PlaceholderPlayer.getPlayerMap().get(viewer);

        if (!user.isAfk()) {
            return "";
        }
        else {
            if (pPlayer != null && !pPlayer.getPlatform().equals(PlaceholderPlayer.Platform.BEDROCK)) {
                return plugin.getPlaceholderMap().get("afk-java");
            } else {
                return plugin.getPlaceholderMap().get("afk-bedrock");
            }
        }
    }
}
