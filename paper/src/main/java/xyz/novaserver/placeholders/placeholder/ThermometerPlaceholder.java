package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class ThermometerPlaceholder extends Placeholder implements PlayerType {
    // The temperature manager from RealisticSeasons
    private Object tempManager;
    private Method getTemp;

    public ThermometerPlaceholder(Placeholders placeholders) {
        super(placeholders, "thermometer", 2000);

        if (Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")) {
            Object realisticSeasons = Bukkit.getPluginManager().getPlugin("RealisticSeasons");
            if (realisticSeasons == null) {
                return;
            }
            try {
                tempManager = realisticSeasons.getClass().getMethod("getTemperatureManager").invoke(realisticSeasons);
                getTemp = tempManager.getClass().getMethod("getTemperature", Player.class);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
                // RealisticSeasons not loaded
            }
        }
    }

    private Integer getTemperature(UUID uuid) {
        if (tempManager == null || getTemp == null) {
            return null;
        }
        try {
            return (int) getTemp.invoke(tempManager, Bukkit.getPlayer(uuid));
        } catch (IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    @Override
    public String get(PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig().getNode("thermometer");
        final Integer temp = getTemperature(player.getUuid());
        final boolean rpApplied = player.isResourcePackApplied();

        if (temp == null) {
            return "";
        }

        if (temp <= 0) {
            return rpApplied ? node.getNode("cold", "rp").getString() : node.getNode("cold", "default").getString();
        } else if (temp <= 40) {
            return rpApplied ? node.getNode("average", "rp").getString() : node.getNode("average", "default").getString();
        } else {
            return rpApplied ? node.getNode("hot", "rp").getString() : node.getNode("hot", "default").getString();
        }
    }
}
