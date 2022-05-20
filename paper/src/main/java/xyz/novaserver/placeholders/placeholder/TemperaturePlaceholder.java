package xyz.novaserver.placeholders.placeholder;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.paper.util.NumberUtils;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class TemperaturePlaceholder extends Placeholder implements PlayerType {
    // The temperature manager from RealisticSeasons
    private Object tempManager;
    private Method getTemp;
    private Object tempSettings;
    private Method isFahrenheit;
    private Method convertFahrenheit;

    public TemperaturePlaceholder(Placeholders placeholders) {
        super(placeholders, "hud_temp", 2000);

        if (Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")) {
            Object realisticSeasons = Bukkit.getPluginManager().getPlugin("RealisticSeasons");
            if (realisticSeasons == null) {
                return;
            }
            Plugin plugin = Bukkit.getPluginManager().getPlugin("NovaPlaceholders");
            if (plugin != null) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    try {
                        tempManager = realisticSeasons.getClass().getMethod("getTemperatureManager").invoke(realisticSeasons);
                        getTemp = tempManager.getClass().getMethod("getTemperature", Player.class);
                        Object tempData = tempManager.getClass().getMethod("getTempData").invoke(tempManager);
                        tempSettings = tempData.getClass().getMethod("getTempSettings").invoke(tempData);
                        isFahrenheit = tempSettings.getClass().getMethod("isConvertToFahrenheit");
                        convertFahrenheit = Class.forName("me.casperge.realisticseasons.utils.JavaUtils").getMethod("convertToFahrenheit", int.class);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException ignored) {
                        // RealisticSeasons not loaded
                    }
                }, 40);
            }
        }
    }

    private int getTemperature(UUID uuid) {
        if (tempManager == null || getTemp == null) {
            return Integer.MIN_VALUE;
        }
        try {
            return (int) getTemp.invoke(tempManager, Bukkit.getPlayer(uuid));
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            return Integer.MIN_VALUE;
        }
    }

    private int convertToFahrenheit(int tempC) {
        if (convertFahrenheit == null) {
            return Integer.MIN_VALUE;
        }
        try {
            return (int) convertFahrenheit.invoke(null, tempC);
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            return Integer.MIN_VALUE;
        }
    }

    private boolean isConvertToFahrenheit() {
        if (tempSettings == null || isFahrenheit == null) {
            return false;
        }
        try {
            return (boolean) isFahrenheit.invoke(tempSettings);
        } catch (IllegalAccessException | InvocationTargetException | NullPointerException e) {
            return false;
        }
    }

    private TextColor getTemperatureColor(UUID uuid)  {
        final int temp = getTemperature(uuid);
        TextColor color = NamedTextColor.GREEN; // a
        try {
            if ((int) tempSettings.getClass().getMethod("getColdHungerTemp").invoke(tempSettings) > temp)
                color = NamedTextColor.AQUA; // b
            if ((int) tempSettings.getClass().getMethod("getColdSlownessTemp").invoke(tempSettings) > temp)
                color = NamedTextColor.BLUE; // 9
            if ((int) tempSettings.getClass().getMethod("getColdFreezingTemp").invoke(tempSettings) > temp)
                color = NamedTextColor.DARK_BLUE; // 1
            if ((int) tempSettings.getClass().getMethod("getWarmNoHealingTemp").invoke(tempSettings) < temp)
                color = NamedTextColor.GOLD; // 6
            if ((int) tempSettings.getClass().getMethod("getWarmSlownessTemp").invoke(tempSettings) < temp)
                color = NamedTextColor.RED; // c
            if ((int) tempSettings.getClass().getMethod("getWarmFireTemp").invoke(tempSettings) < temp)
                color = NamedTextColor.DARK_RED; // 4
            if ((int) tempSettings.getClass().getMethod("getBoostMinTemp").invoke(tempSettings) <= temp
                    && (int) tempSettings.getClass().getMethod("getBoostMaxTemp").invoke(tempSettings) >= temp)
                color = NamedTextColor.LIGHT_PURPLE; // d
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ignored) {
            // Realistic seasons not loaded, should never get to this point
        }
        return color;
    }

    @Override
    public String get(PlayerData player) {
        final int tempC = getTemperature(player.getUuid());
        // Return if temperature is invalid
        if (tempC == Integer.MIN_VALUE) {
            return "";
        }

        final ConfigurationNode node = getPlaceholders().getConfig().getNode("temperature");
        final boolean rpApplied = player.isResourcePackApplied();
        // Convert temperature to string
        String tempString = String.valueOf(isConvertToFahrenheit() ? convertToFahrenheit(tempC) : tempC);

        // Added spaces to keep thermometer in place
        Component padding = Component.empty();
        switch (tempString.length()) {
            case 0 -> padding = Component.text("   ");
            case 1 -> padding = Component.text("  ");
            case 2 -> padding = Component.space();
        }

        // Add temp numbers and sign
        Component temperature;
        if (rpApplied) {
            // Convert numbers to resource pack hud numbers
            final String replaceChars = getPlaceholders().getConfig().getNode("hud-numbers").getString();
            final String sign = !isConvertToFahrenheit()
                    ? node.getNode("celsius", "rp").getString()
                    : node.getNode("fahrenheit", "rp").getString();
            temperature = Component.text(NumberUtils.convertNumbers(tempString, replaceChars) + sign);
        } else {
            final String sign = !isConvertToFahrenheit()
                    ? node.getNode("celsius", "default").getString()
                    : node.getNode("fahrenheit", "default").getString();
            temperature = Component.text(tempString + sign);
        }
        // Color the temperature text
        final TextColor tempColor = getTemperatureColor(player.getUuid());
        temperature = temperature.color(tempColor);

        Component thermometer;
        // Add thermometer icon depending on temp and resourcepack status
        if (tempC <= node.getNode("cold", "temp").getInt()) {
            if (rpApplied) {
                thermometer = Component.text(node.getNode("cold", "rp").getString("")).color(NamedTextColor.WHITE);
            } else {
                thermometer = Component.text(node.getNode("cold", "default").getString("")).color(tempColor);
            }
        } else if (tempC <= node.getNode("average", "temp").getInt()) {
            if (rpApplied) {
                thermometer = Component.text(node.getNode("average", "rp").getString("")).color(NamedTextColor.WHITE);
            } else {
                thermometer = Component.text(node.getNode("average", "default").getString("")).color(tempColor);
            }
        } else {
            if (rpApplied) {
                thermometer = Component.text(node.getNode("hot", "rp").getString("")).color(NamedTextColor.WHITE);
            } else {
                thermometer = Component.text(node.getNode("hot", "default").getString("")).color(tempColor);
            }
        }

        final Component text = padding.append(temperature).append(Component.space()).append(thermometer);
        return LegacyComponentSerializer.legacySection().serialize(text);
    }
}
