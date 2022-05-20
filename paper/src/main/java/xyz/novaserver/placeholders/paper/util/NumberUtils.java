package xyz.novaserver.placeholders.paper.util;

import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class NumberUtils {

    /**
     * @param toConvert The source string to replace numbers in
     * @param replaceString A string of replacement chars in the format [-0123456789]
     * @return A converted string with replaced numbers
     */
    public static String convertNumbers(final String toConvert, final String replaceString) {
        // Reference string of numbers and negative sign
        final String NUM_STRING = "-0123456789";
        // String of replacement characters that match the reference string

        char[] numberChars = toConvert.toCharArray();
        for (int i = 0; i < numberChars.length; i++) {
            /*  Takes the current char, finds the index of the char using the reference string, and sets it
                equal to the corresponding replacement char from the replace string. */
            if (i > 0 && numberChars[i-1] == LegacyComponentSerializer.AMPERSAND_CHAR) {
                continue;
            }
            if (NUM_STRING.indexOf(numberChars[i]) > -1) {
                numberChars[i] = replaceString.charAt(NUM_STRING.indexOf(numberChars[i]));
            }
        }
        // Convert numberChars to string and return
        return String.valueOf(numberChars);
    }
}
