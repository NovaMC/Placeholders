package xyz.novaserver.placeholders.placeholder.seasons;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class SeasonsUtils {
    private static final Class<?>[] PLAYER_TYPE = new Class[]{Player.class};

    /**
     * @param object The object to execute the method on
     * @param methodName A string representing the name of the method to run
     * @param parameters Any parameters to pass to the method
     * @return The result of the method
     */
    private static Object invokeMethod(@NotNull Object object, @NotNull String methodName, Class<?>[] parameterTypes, Object... parameters) {
        try {
            if (parameterTypes == null || parameterTypes.length == 0 || parameters.length == 0) {
                return object.getClass().getMethod(methodName).invoke(object);
            } else {
                return object.getClass().getMethod(methodName, parameterTypes).invoke(object, parameters);
            }
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | NullPointerException e) {
            throw new RuntimeException(e);
        }
    }

    private static @Nullable Object getRealisticSeasons() {
        if (Bukkit.getPluginManager().isPluginEnabled("RealisticSeasons")) {
            return Bukkit.getPluginManager().getPlugin("RealisticSeasons");
        } else {
            return null;
        }
    }

    // Gets the RealisticSeasons TemperatureManager
    private static @Nullable Object getTempManager() {
        final Object realisticSeasons = getRealisticSeasons();
        if (realisticSeasons == null) return null;
        return invokeMethod(realisticSeasons, "getTemperatureManager", null);
    }

    // Gets the RealisticSeasons TempData
    private static @Nullable Object getTempData() {
        final Object tempManager = getTempManager();
        if (tempManager == null) return null;
        return invokeMethod(tempManager, "getTempData", null);
    }

    // Gets the RealisticSeasons TemperatureSettings
    private static @Nullable Object getTempSettings() {
        final Object tempData = getTempData();
        if (tempData == null) return null;
        return invokeMethod(tempData, "getTempSettings", null);
    }

    /**
     * Gets the fahrenheit status of a player
     * @param player The player to check for fahrenheit on
     * @return A boolean containing if the player has fahrenheit enabled or not
     */
    private static boolean hasFahrenheitEnabled(@NotNull Player player) {
        final Object tempManager = getTempManager();
        if (tempManager == null) return false;
        return (boolean) invokeMethod(tempManager, "hasFahrenheitEnabled", PLAYER_TYPE, player);
    }

    /**
     * Gets the fahrenheit status of the plugin
     * @return A boolean containing if the server should convert temperatures or not, defaults to false on error
     */
    private static boolean isConvertToFahrenheit() {
        final Object tempSettings = getTempSettings();
        if (tempSettings == null) return false;
        return (boolean) invokeMethod(tempSettings, "isConvertToFahrenheit", null);
    }

    /**
     * Gets the fahrenheit status of a player based on their own setting and the plugin's
     * @param player The player to get the fahrenheit status of
     * @return A boolean containing if fahrenheit should be enabled or not
     */
    public static boolean getFahrenheitStatus(Player player) {
        return (isConvertToFahrenheit() && !hasFahrenheitEnabled(player))
                || (!isConvertToFahrenheit() && hasFahrenheitEnabled(player));
    }

    /**
     * @param player The player to get the temperature of
     * @return The temperature of the player, or Integer.MIN_VALUE if there was an error
     */
    public static int getTemperature(@NotNull Player player) {
        final Object tempManager = getTempManager();
        if (tempManager == null) return Integer.MIN_VALUE;
        return (int) invokeMethod(tempManager, "getTemperature", PLAYER_TYPE, player);
    }

    /**
     * @param celsius An integer with a temperature in celsius
     * @return A new integer with a temperature in fahrenheit
     */
    public static int convertToFahrenheit(int celsius) {
        return celsius * 9 / 5 + 32;
    }

    /**
     * @param player The player to get the temperature color of
     * @return A TextColor depending on the temperature of the player
     */
    public static TextColor getTemperatureColor(@NotNull Player player)  {
        final Object tempSettings = getTempSettings();
        final int temp = getTemperature(player);
        TextColor color = NamedTextColor.GREEN; // a
        if (tempSettings == null || temp == Integer.MIN_VALUE)
            return color;
        if ((int) invokeMethod(tempSettings, "getColdHungerTemp", null) > temp)
            color = NamedTextColor.AQUA; // b
        if ((int) invokeMethod(tempSettings, "getColdSlownessTemp", null) > temp)
            color = NamedTextColor.BLUE; // 9
        if ((int) invokeMethod(tempSettings, "getColdFreezingTemp", null) > temp)
            color = NamedTextColor.DARK_BLUE; // 1
        if ((int) invokeMethod(tempSettings, "getWarmNoHealingTemp", null) < temp)
            color = NamedTextColor.GOLD; // 6
        if ((int) invokeMethod(tempSettings, "getWarmSlownessTemp", null) < temp)
            color = NamedTextColor.RED; // c
        if ((int) invokeMethod(tempSettings, "getWarmFireTemp", null) < temp)
            color = NamedTextColor.DARK_RED; // 4
        if ((int) invokeMethod(tempSettings, "getBoostMinTemp", null) <= temp
                && (int) invokeMethod(tempSettings, "getBoostMaxTemp", null) >= temp
                && ((List<?>)invokeMethod(tempSettings, "getBoostPotionEffects", null)).size() > 0)
            color = NamedTextColor.LIGHT_PURPLE; // d
        return color;
    }
}
