package xyz.novaserver.placeholders.placeholder.seasons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import xyz.novaserver.placeholders.common.Placeholders;
import xyz.novaserver.placeholders.common.data.PlayerData;
import xyz.novaserver.placeholders.placeholder.type.Placeholder;
import xyz.novaserver.placeholders.placeholder.type.PlayerType;
import xyz.novaserver.placeholders.paper.util.ActionbarUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HudTempPlaceholder extends Placeholder implements PlayerType {
    // Cache data to save cpu time when the temperature doesn't change
    private final Map<UUID, TemperatureData> tempCache = new HashMap<>();

    public HudTempPlaceholder(Placeholders placeholders) {
        super(placeholders, "hud_temp", 2000);
    }

    @Override
    public String get(PlayerData data) {
        final UUID uuid = data.getUuid();
        final Player player = Bukkit.getPlayer(uuid);
        // Return if player could not be retrieved
        if (player == null) return "";

        // If temperature is invalid have it display 0 degrees
        int tempC = SeasonsUtils.getTemperature(player);
        if (tempC == Integer.MIN_VALUE) tempC = 0;

        final boolean bedrock = data.isBedrock();

        // Handle cache data
        if (!tempCache.containsKey(uuid)) {
            tempCache.put(uuid, new TemperatureData(tempC, bedrock));
        } else {
            if (tempCache.get(uuid).equalsData(tempC, bedrock)) {
                return tempCache.get(uuid).getDisplay();
            } else {
                tempCache.get(uuid).setTemperature(tempC);
                tempCache.get(uuid).setBedrock(bedrock);
            }
        }

        final ConfigurationNode config = getPlaceholders().getConfig();
        final boolean isFahrenheit = SeasonsUtils.getFahrenheitStatus(player);
        // Convert temperature to fahrenheit if specified and convert to string
        final String tempString = String.valueOf(isFahrenheit ? SeasonsUtils.convertToFahrenheit(tempC) : tempC);

        // Added spaces to keep thermometer in place
        Component padding = getPadding(config, bedrock, tempString);

        // Add temp numbers and celsius/fahrenheit sign
        // Color the temperature text
        final TextColor tempColor = SeasonsUtils.getTemperatureColor(player);
        Component temperature = getTemperature(config, bedrock, isFahrenheit, tempString).color(tempColor);

        // The gap between the temperature and thermometer
        Component gap = !bedrock
                ? Component.text(config.getNode("hud", "space-char").getString(" ")) : Component.space();

        // Add thermometer icon depending on temp and resource pack status
        Component thermometer = getThermometer(config, bedrock, tempC, tempColor);

        final Component text = padding.append(temperature).append(gap).append(thermometer);
        final String result = LegacyComponentSerializer.legacySection().serialize(text);
        tempCache.get(uuid).setDisplay(result);

        return result;
    }

    private Component getPadding(ConfigurationNode config, boolean bedrock, String tempString) {
        if (!bedrock) {
            // Get chars used to right align to hotbar on java
            final String alignChars = ActionbarUtils.getAlignChars(tempString.length(),
                    4, config.getNode("hud", "align-char").getString(""));
            return Component.text(ActionbarUtils.getSpaces(34)).append(Component.text(alignChars));
        } else {
            return Component.text(ActionbarUtils.getSpaces(28));
        }
    }

    private Component getTemperature(ConfigurationNode config, boolean bedrock, boolean isFahrenheit, String tempString) {
        final ConfigurationNode tempNode = config.getNode("temperature");
        if (!bedrock) {
            // Convert numbers to resource pack hud numbers
            final String replaceChars = config.getNode("hud", "numbers").getString("");
            final Component sign = Component.text(!isFahrenheit
                    ? tempNode.getNode("celsius", "java").getString("")
                    : tempNode.getNode("fahrenheit", "java").getString(""));
            return ActionbarUtils.convertNumbers(Component.text(tempString), replaceChars).append(sign);
        } else {
            final String sign = !isFahrenheit
                    ? tempNode.getNode("celsius", "bedrock").getString()
                    : tempNode.getNode("fahrenheit", "bedrock").getString();
            return Component.text(tempString + sign);
        }
    }

    private Component getThermometer(ConfigurationNode config, boolean bedrock, int tempC, TextColor tempColor) {
        final ConfigurationNode tempNode = config.getNode("temperature");
        String defaultTherm = tempNode.getNode("bedrock").getString("|");
        if (!bedrock) {
            String thermChars = tempNode.getNode("chars").getString(defaultTherm);
            int rangeLow = tempNode.getNode("range-low").getInt();
            int rangeHigh = tempNode.getNode("range-high").getInt();
            int thermIndex = ActionbarUtils.convertRange(rangeLow, rangeHigh, 0, thermChars.length() - 1, tempC);

            return Component.text(thermChars.charAt(thermIndex)).color(NamedTextColor.WHITE);
        } else {
            return Component.text(defaultTherm).color(tempColor);
        }
    }
}
