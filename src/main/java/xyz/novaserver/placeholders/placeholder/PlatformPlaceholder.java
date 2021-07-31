package xyz.novaserver.placeholders.placeholder;

import com.velocitypowered.api.proxy.Player;
import me.neznamy.tab.api.TabPlayer;
import me.neznamy.tab.shared.placeholders.RelationalPlaceholder;
import org.geysermc.floodgate.util.DeviceOs;
import xyz.novaserver.placeholders.Placeholders;
import xyz.novaserver.placeholders.player.PlaceholderPlayer;

import java.util.Map;

public class PlatformPlaceholder extends RelationalPlaceholder {
    private final Placeholders plugin;

    public PlatformPlaceholder(Placeholders plugin) {
        super("%rel_platform%", 2000);
        this.plugin = plugin;
    }

    //TODO: use switch statement and enum for device/loader type
    @Override
    public String get(TabPlayer viewer, TabPlayer target) {
        String placeholder = "";
        if (viewer.getPlayer() instanceof Player vPlayer && target.getPlayer() instanceof Player tPlayer) {
            Map<Player, PlaceholderPlayer> playerMap = plugin.getPlayerMap();

            if (!playerMap.containsKey(vPlayer)
                    || !playerMap.containsKey(tPlayer)) return placeholder;
            if (plugin.getConfig().getNode("needs-rp").getBoolean()
                    && !playerMap.get(vPlayer).isAcceptedPack()) return placeholder;

            if (playerMap.get(tPlayer).isUsingBedrock()) {
                if (playerMap.get(tPlayer).getDeviceOs().equals(DeviceOs.NX)) {
                    placeholder = plugin.getConfig().getNode("switch").getString();
                } else if (playerMap.get(tPlayer).getDeviceOs().equals(DeviceOs.XBOX)) {
                    placeholder = plugin.getConfig().getNode("xbox").getString();
                } else if (playerMap.get(tPlayer).getDeviceOs().equals(DeviceOs.PS4)) {
                    placeholder = plugin.getConfig().getNode("playstation").getString();
                } else {
                    placeholder = plugin.getConfig().getNode("bedrock").getString();
                }
            }
            else if (playerMap.get(tPlayer).isUsingFabric()) {
                placeholder = plugin.getConfig().getNode("fabric").getString();
            }
            else if (playerMap.get(tPlayer).isUsingForge()) {
                placeholder = plugin.getConfig().getNode("forge").getString();
            }
            else {
                placeholder = plugin.getConfig().getNode("vanilla").getString();
            }
        }
        return placeholder;
    }
}
