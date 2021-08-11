package xyz.novaserver.placeholders.velocity.placeholder;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.PlayerPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractPlayerPlaceholder;

public class TABPlaceholder extends PlayerPlaceholder {
    private final AbstractPlayerPlaceholder placeholder;

    public TABPlaceholder(AbstractPlayerPlaceholder placeholder, int refresh) {
        super("%" + placeholder.getIdentifier() + "%", refresh);
        this.placeholder = placeholder;
    }

    @Override
    public String get(TabPlayer player) {
        return placeholder.get(player.getUniqueId());
    }
}
