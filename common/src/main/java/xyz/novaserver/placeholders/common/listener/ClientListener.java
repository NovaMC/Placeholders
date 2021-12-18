package xyz.novaserver.placeholders.common.listener;

import org.geysermc.floodgate.api.FloodgateApi;
import org.geysermc.floodgate.api.player.FloodgatePlayer;
import xyz.novaserver.placeholders.common.PlaceholderPlayer;

import java.util.UUID;

public abstract class ClientListener {

    protected void setOptions(UUID player, String brand) {
        FloodgateApi floodgate = FloodgateApi.getInstance();

        if (brand.contains("geyser") || floodgate.isFloodgatePlayer(player)) {
            FloodgatePlayer fPlayer = floodgate.getPlayer(player);
            if (fPlayer.getDeviceOs() != null) {
                PlaceholderPlayer.getPlayerMap().get(player).setDeviceOs(fPlayer.getDeviceOs());
            }
            PlaceholderPlayer.getPlayerMap().get(player).setPlatform(PlaceholderPlayer.Platform.BEDROCK);
        }
        if (brand.contains("fabric")) {
            PlaceholderPlayer.getPlayerMap().get(player).setPlatform(PlaceholderPlayer.Platform.FABRIC);
        }
        if (brand.matches("(?i)fml|forge")) {
            PlaceholderPlayer.getPlayerMap().get(player).setPlatform(PlaceholderPlayer.Platform.FORGE);
        }
    }
}
