package xyz.novaserver.placeholders.placeholder.seasons;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

public class SeasonsUtils {
    private static @Nullable Object getRealisticSeasons() {
        if (Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")) {
            return Bukkit.getPluginManager().getPlugin("RealisticSeasons");
        } else {
            return null;
        }
    }

    private static @Nullable Object getTempManager() {
        final Object realisticSeasons = getRealisticSeasons();
        if (realisticSeasons == null) return null;
        try {
            return realisticSeasons.getClass().getMethod("getTemperatureManager").invoke(realisticSeasons);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            return null;
        }
    }

    private static @Nullable Object getTempData() {
        final Object tempManager = getTempManager();
        if (tempManager == null) return null;
        try {
            return tempManager.getClass().getMethod("getTempData").invoke(tempManager);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            return null;
        }
    }

    private static @Nullable Object getTempSettings() {
        final Object tempData = getTempData();
        if (tempData == null) return null;
        try {
            return tempData.getClass().getMethod("getTempSettings").invoke(tempData);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            return null;
        }
    }

    /**
     * @param uuid The Unique ID of a player to get the temperature of
     * @return The temperature of the player, or Integer.MIN_VALUE if there was an error
     */
    public static int getTemperature(@NotNull UUID uuid) {
        final Object tempManager = getTempManager();
        if (tempManager == null) return Integer.MIN_VALUE;
        try {
            return (int) tempManager.getClass().getMethod("getTemperature", Player.class)
                    .invoke(tempManager, Bukkit.getPlayer(uuid));
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            return Integer.MIN_VALUE;
        }
    }

    /**
     * @return A boolean containing if the server should convert temperatures or not, defaults to false on error
     */
    public static boolean isConvertToFahrenheit() {
        final Object tempSettings = getTempSettings();
        if (tempSettings == null) return false;
        try {
            return (boolean) tempSettings.getClass().getMethod("isConvertToFahrenheit").invoke(tempSettings);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            return false;
        }
    }

    /**
     * @param uuid The Unique ID of a player to get the temperature color of
     * @return A TextColor depending on the temperature of the player
     */
    public static TextColor getTemperatureColor(@NotNull UUID uuid)  {
        final Object tempSettings = getTempSettings();
        final int temp = getTemperature(uuid);
        TextColor color = NamedTextColor.GREEN; // a
        if (tempSettings == null || temp == Integer.MIN_VALUE)
            return color;
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

    /**
     * @param celsius An integer with a temperature in celsius
     * @return A new integer with a temperature in fahrenheit
     */
    public static int celsiusToFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }
}
