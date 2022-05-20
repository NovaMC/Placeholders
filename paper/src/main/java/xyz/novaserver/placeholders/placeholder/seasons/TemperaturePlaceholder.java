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
import xyz.novaserver.placeholders.placeholder.util.NumberUtils;
import xyz.novaserver.placeholders.placeholder.util.SeasonsUtils;

public class TemperaturePlaceholder extends Placeholder implements PlayerType {
    public TemperaturePlaceholder(Placeholders placeholders) {
        super(placeholders, "hud_temp", 2000);
    }

    @Override
    public String get(PlayerData player) {
        final int tempC = SeasonsUtils.getTemperature(player.getUuid());
        // Return if temperature is invalid
        if (tempC == Integer.MIN_VALUE) {
            return "";
        }

        final ConfigurationNode node = getPlaceholders().getConfig().getNode("temperature");
        final boolean rpApplied = player.isResourcePackApplied();
        // Convert temperature to string
        String tempString = String.valueOf(SeasonsUtils.isConvertToFahrenheit() ? NumberUtils.celsiusToFahrenheit(tempC) : tempC);

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
            final String sign = !SeasonsUtils.isConvertToFahrenheit()
                    ? node.getNode("celsius", "rp").getString()
                    : node.getNode("fahrenheit", "rp").getString();
            temperature = Component.text(NumberUtils.convertNumbers(tempString, replaceChars) + sign);
        } else {
            final String sign = !SeasonsUtils.isConvertToFahrenheit()
                    ? node.getNode("celsius", "default").getString()
                    : node.getNode("fahrenheit", "default").getString();
            temperature = Component.text(tempString + sign);
        }
        // Color the temperature text
        final TextColor tempColor = SeasonsUtils.getTemperatureColor(player.getUuid());
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
