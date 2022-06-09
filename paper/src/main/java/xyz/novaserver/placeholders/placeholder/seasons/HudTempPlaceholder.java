package xyz.novaserver.placeholders.placeholder.seasons;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import ninja.leaping.configurate.ConfigurationNode;
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
    private final Map<UUID, Integer> tempCache = new HashMap<>();
    private final Map<UUID, String> displayCache = new HashMap<>();

    public HudTempPlaceholder(Placeholders placeholders) {
        super(placeholders, "hud_temp", 2000);
    }

    @Override
    public String get(PlayerData player) {
        final int tempC = SeasonsUtils.getTemperature(player.getUuid());
        // Return if temperature is invalid
        if (tempC == Integer.MIN_VALUE) {
            return "";
        }

        final UUID uuid = player.getUuid();
        if (!tempCache.containsKey(uuid) || (tempCache.containsKey(uuid) && tempC != tempCache.get(uuid))) {
            tempCache.put(uuid, tempC);
        } else if (displayCache.containsKey(uuid)) {
            return displayCache.get(uuid);
        }

        // Config and cross-platform stuff
        final ConfigurationNode config = getPlaceholders().getConfig();
        final boolean rpApplied = player.isResourcePackApplied();

        // Convert temperature to fahrenheit if specified and convert to string
        final String tempString = String.valueOf(SeasonsUtils.isConvertToFahrenheit()
                ? SeasonsUtils.celsiusToFahrenheit(tempC) : tempC);

        // Added spaces to keep thermometer in place
        Component padding = getPadding(config, rpApplied, tempString);

        // Add temp numbers and celsius/fahrenheit sign
        // Color the temperature text
        final TextColor tempColor = SeasonsUtils.getTemperatureColor(player.getUuid());
        Component temperature = getTemperature(config, rpApplied, tempString).color(tempColor);

        // The gap between the temperature and thermometer
        Component gap = rpApplied
                ? Component.text(config.getNode("hud", "space-char").getString(" ")) : Component.space();

        // Add thermometer icon depending on temp and resource pack status
        Component thermometer = getThermometer(config, rpApplied, tempC, tempColor);

        final Component text = padding.append(temperature).append(gap).append(thermometer);
        final String result = LegacyComponentSerializer.legacySection().serialize(text);
        displayCache.put(uuid, result);

        return result;
    }

    private Component getPadding(ConfigurationNode config, boolean rpApplied, String tempString) {
        if (rpApplied) {
            // Get chars used to right align to hotbar on java
            final String alignChars = ActionbarUtils.getAlignChars(tempString.length(),
                    4, config.getNode("hud", "align-char").getString(""));
            return Component.text(ActionbarUtils.getSpaces(34)).append(Component.text(alignChars));
        } else {
            return Component.text(ActionbarUtils.getSpaces(28));
        }
    }

    private Component getTemperature(ConfigurationNode config, boolean rpApplied, String tempString) {
        final ConfigurationNode tempNode = config.getNode("temperature");
        if (rpApplied) {
            // Convert numbers to resource pack hud numbers
            final String replaceChars = config.getNode("hud", "numbers").getString("");
            final Component sign = Component.text(!SeasonsUtils.isConvertToFahrenheit()
                    ? tempNode.getNode("celsius", "rp").getString("")
                    : tempNode.getNode("fahrenheit", "rp").getString(""));
            return ActionbarUtils.convertNumbers(Component.text(tempString), replaceChars).append(sign);
        } else {
            final String sign = !SeasonsUtils.isConvertToFahrenheit()
                    ? tempNode.getNode("celsius", "default").getString()
                    : tempNode.getNode("fahrenheit", "default").getString();
            return Component.text(tempString + sign);
        }
    }

    private Component getThermometer(ConfigurationNode config, boolean rpApplied, int tempC, TextColor tempColor) {
        final ConfigurationNode tempNode = config.getNode("temperature");
        String defaultTherm = tempNode.getNode("default").getString("|");
        if (rpApplied) {
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