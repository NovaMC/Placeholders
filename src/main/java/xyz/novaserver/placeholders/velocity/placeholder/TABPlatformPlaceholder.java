package xyz.novaserver.placeholders.velocity.placeholder;

import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.RelationalPlaceholder;
import xyz.novaserver.placeholders.core.placeholder.PlatformPlaceholder;
import xyz.novaserver.placeholders.velocity.PlaceholdersVelocity;

public class TABPlatformPlaceholder extends RelationalPlaceholder {
    private final PlatformPlaceholder placeholder;

    public TABPlatformPlaceholder(PlaceholdersVelocity plugin) {
        super("%rel_" + PlatformPlaceholder.identifier + "%", 2000);
        this.placeholder = new PlatformPlaceholder(plugin);
    }

    @Override
    public String get(TabPlayer viewer, TabPlayer target) {
        return placeholder.get(viewer.getUniqueId(), target.getUniqueId());
    }
}
