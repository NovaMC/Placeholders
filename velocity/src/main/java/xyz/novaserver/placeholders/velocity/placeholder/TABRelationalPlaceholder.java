package xyz.novaserver.placeholders.velocity.placeholder;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.RelationalPlaceholder;
import xyz.novaserver.placeholders.common.placeholder.type.AbstractRelationalPlaceholder;

public class TABRelationalPlaceholder extends RelationalPlaceholder {
    private final AbstractRelationalPlaceholder placeholder;

    public TABRelationalPlaceholder(AbstractRelationalPlaceholder placeholder, int refresh) {
        super("%rel_" + placeholder.getIdentifier() + "%", refresh);
        this.placeholder = placeholder;
    }

    @Override
    public String get(TabPlayer viewer, TabPlayer target) {
        return placeholder.get(viewer.getUniqueId(), target.getUniqueId());
    }
}
