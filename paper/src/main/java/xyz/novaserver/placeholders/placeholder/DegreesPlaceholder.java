package xyz.novaserver.placeholders.placeholder;

import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DegreesPlaceholder extends Placeholder implements PlayerType {
    private Object tempSettings;
    private Method isFahrenheit;

    public DegreesPlaceholder(Placeholders placeholders) {
        super(placeholders, "degrees", 2000);

        if (Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")) {
            Object realisticSeasons = Bukkit.getPluginManager().getPlugin("RealisticSeasons");
            if (realisticSeasons == null) {
                return;
            }
            try {
                Object tempManager = realisticSeasons.getClass().getMethod("getTemperatureManager").invoke(realisticSeasons);
                Object tempData = tempManager.getClass().getMethod("getTempData").invoke(tempManager);
                tempSettings = tempData.getClass().getMethod("getTempSettings").invoke(tempData);
                isFahrenheit = tempSettings.getClass().getMethod("isConvertToFahrenheit");
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ignored) {
                // RealisticSeasons not loaded
            }
        }
    }

    private boolean isConvertToFahrenheit() {
        try {
            return (boolean) isFahrenheit.invoke(tempSettings);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }
    }

    @Override
    public String get(PlayerData player) {
        final ConfigurationNode node = getPlaceholders().getConfig().getNode("degrees");
        final boolean rpApplied = player.isResourcePackApplied();

        if (!isConvertToFahrenheit()) {
            return rpApplied ? node.getNode("celsius", "rp").getString() : node.getNode("celsius", "default").getString();
        } else {
            return rpApplied ? node.getNode("fahrenheit", "rp").getString() : node.getNode("fahrenheit", "default").getString();
        }
    }
}
