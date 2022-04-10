package xyz.novaserver.placeholders.velocity;

import me.neznamy.tab.api.TabAPI;
import me.neznamy.tab.api.event.Subscribe;
import me.neznamy.tab.api.event.plugin.TabLoadEvent;
import me.neznamy.tab.api.placeholder.PlaceholderManager;
import xyz.novaserver.placeholders.common.Expansion;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;
import xyz.novaserver.placeholders.placeholder.type.RelationalType;
import xyz.novaserver.placeholders.placeholder.type.ServerType;

import java.util.List;

public class TABExpansion implements Expansion {
    private boolean registered = false;

    // Store placeholders because TAB requires them to be re-registered after reload
    private Placeholders placeholders;
    private List<Placeholder> placeholderList;

    public TABExpansion() {
        TabAPI.getInstance().getEventBus().register(this);
    }

    @Subscribe
    public void onTabLoad(TabLoadEvent event) {
        // Re-register placeholders when TAB reloads
        if (registered) {
            registered = false;
            register(placeholders, placeholderList);
        }
    }

    @Override
    public void register(Placeholders placeholders, List<Placeholder> placeholderList) {
        if (registered) return;
        PlaceholderManager tabManager = TabAPI.getInstance().getPlaceholderManager();

        // Store placeholders because TAB requires them to be re-registered after reload
        if (this.placeholders == null) this.placeholders = placeholders;
        if (this.placeholderList == null) this.placeholderList = placeholderList;

        for (Placeholder placeholder : placeholderList) {
            if (placeholder instanceof RelationalType p) {
                tabManager.registerRelationalPlaceholder("%rel_" + placeholder.getIdentifier() + "%", placeholder.getRefresh(),
                        (viewer, target) -> p.get(placeholders.getData(viewer.getUniqueId()), placeholders.getData(target.getUniqueId())));
            }
            else if (placeholder instanceof PlayerType p) {
                tabManager.registerPlayerPlaceholder("%" + placeholder.getIdentifier() + "%", placeholder.getRefresh(),
                        (player) -> p.get(placeholders.getData(player.getUniqueId())));
            }
            else if (placeholder instanceof ServerType p) {
                tabManager.registerServerPlaceholder("%" + placeholder.getIdentifier() + "%", placeholder.getRefresh(), p::get);
            }
        }
        registered = true;
    }
}
