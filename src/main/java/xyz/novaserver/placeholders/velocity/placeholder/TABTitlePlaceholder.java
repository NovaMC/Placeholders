package xyz.novaserver.placeholders.velocity.placeholder;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.PlayerPlaceholder;
import xyz.novaserver.placeholders.core.placeholder.TitlePlaceholder;
import xyz.novaserver.placeholders.velocity.PlaceholdersVelocity;

public class TABTitlePlaceholder extends PlayerPlaceholder {
    private final TitlePlaceholder placeholder;

    public TABTitlePlaceholder(PlaceholdersVelocity plugin) {
        super("%" + TitlePlaceholder.identifier + "%", 2000);
        this.placeholder = new TitlePlaceholder(plugin);
    }

    @Override
    public String get(TabPlayer player) {
        return placeholder.get(player.getUniqueId());
    }
}
