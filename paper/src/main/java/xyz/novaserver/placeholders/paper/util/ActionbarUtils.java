package xyz.novaserver.placeholders.paper.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;

import java.util.HashSet;
import java.util.Set;

public final class ActionbarUtils {

    /**
     * @param toConvert The source string to replace numbers in
     * @param replaceString A string of replacement chars in the format [-0123456789]
     * @return A converted string with replaced numbers
     */
    public static Component convertNumbers(Component toConvert, final String replaceString) {
        // Reference string of numbers and negative sign
        final String NUM_STRING = "-0123456789";
        final Set<TextReplacementConfig> replacementConfigs = new HashSet<>();

        for (int i = 0; i < NUM_STRING.length(); i++) {
            replacementConfigs.add(TextReplacementConfig.builder()
                    .matchLiteral(String.valueOf(NUM_STRING.charAt(i)))
                    .times(1000) // Hopefully there is never more than 1000 of one number
                    .replacement(String.valueOf(replaceString.charAt(i)))
                    .build());
        }

        for (TextReplacementConfig replacementConfig : replacementConfigs) {
            toConvert = toConvert.replaceText(replacementConfig);
        }
        return toConvert;
    }

    /**
     * @param length The length of the content to align
     * @param amount The max amount of chars to use / the max length of the content
     * @param c The char to use for alignment
     * @return A string of chars to use for alignment
     */
    public static String getAlignChars(int length, int amount, String c) {
        final String CHARS = c.repeat(Math.max(0, amount));
        return CHARS.substring(length);
    }

    /**
     * @param amount Amount of spaces to generate
     * @return A string of spaces of the specified amount
     */
    public static String getSpaces(int amount) {
        final String SPACE = " ";
        return SPACE.repeat(Math.max(0, amount));
    }

    /**
     * @param oldMin The minimum of the old range
     * @param oldMax The maximum of the old range
     * @param newMin The minimum of the new range
     * @param newMax The maximum of the new range
     * @param value The value to convert into the new range
     * @return A converted value based in a new range
     */
    public static int convertRange(double oldMin, double oldMax, double newMin, double newMax, double value) {
        // Clamp the value based on the min and max
        value = Math.max(Math.min(value, oldMax), oldMin);
        // Calculate percentage of the value based on old range
        double percent = (value - oldMin) / (oldMax - oldMin);
        // Use percentage to get a value from the new range
        double result = ((newMax - newMin) * percent) + newMin;
        return (int) Math.round(result);
    }
}
