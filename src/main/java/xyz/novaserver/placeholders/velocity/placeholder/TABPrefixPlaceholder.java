package xyz.novaserver.placeholders.velocity.placeholder;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.RelationalPlaceholder;
import xyz.novaserver.placeholders.core.placeholder.PrefixPlaceholder;
import xyz.novaserver.placeholders.velocity.PlaceholdersVelocity;

public class TABPrefixPlaceholder extends RelationalPlaceholder {
    private final PrefixPlaceholder placeholder;

    public TABPrefixPlaceholder(PlaceholdersVelocity plugin) {
        super("%rel_" + PrefixPlaceholder.identifier + "%", 2000);
        this.placeholder = new PrefixPlaceholder(plugin);
    }

    @Override
    public String get(TabPlayer viewer, TabPlayer target) {
        return placeholder.get(viewer.getUniqueId(), target.getUniqueId());
    }
}
