package io.github.tr100000.trutils.api.utils;

import org.apache.commons.lang3.text.WordUtils;
import org.jspecify.annotations.Nullable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Utility stuff not specific to Minecraft.
 */
public final class Utils {
    private Utils() {}

    /**
     * This method should explain itself.
     * @param str the string to check
     * @return {@code true} if the string is null or blank, {@code false} otherwise
     * @see String#isBlank()
     */
    public static boolean stringIsNullOrBlank(@Nullable String str) {
        return str == null || str.isBlank();
    }

    /**
     * Gets any key of a specified value in a map
     * @param map the map to search
     * @param value the value to check for
     * @return any key that maps to the provided value, null if no matching key was found
     * @param <T> the type of keys maintained in the map
     * @param <E> the type of values in the map
     * @throws NullPointerException if the map is null
     */
    public static <T, E> @Nullable T getKeyByValue(Map<T, E> map, E value) {
        Objects.requireNonNull(map, "map must not be null");
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Formats units up to 2 significant values with a metric prefix (k for kilo for example)
     * @param value the value to format
     * @param baseUnit the base unit (meters, grams, joules, etc.)
     * @return the formatted string
     */
    public static String formatUnits(long value, String baseUnit) {
        int exp = value > 0 ? (int)Math.log10(value) / 3 : 0;
        double doubleValue = value / Math.pow(1000, exp);
        return formatUnits(doubleValue, exp, baseUnit);
    }

    /**
     * Formats units up to 2 significant values with a metric prefix (k for kilo for example)
     * @param doubleValue the value
     * @param exponent an exponent value
     * @param baseUnit the base unit (meters, grams, joules, etc.)
     * @return the formatted string
     */
    public static String formatUnits(double doubleValue, int exponent, String baseUnit) {
        if (exponent > 0) {
            char prefix = "kMGTPEZY".charAt(exponent - 1);
            return String.format("%.1f %s%s", doubleValue, prefix, baseUnit);
        }
        else {
            return String.format("%.1f %s", doubleValue, baseUnit);
        }
    }

    /**
     * Delete a folder
     * @param folder the folder to delete
     * @throws IOException if an I/O error occurs
     */
    public static void deleteFolder(Path folder) throws IOException {
        if (!Files.exists(folder)) return;
        List<Path> paths = Files.list(folder).toList();
        for (Path path : paths) {
            if (Files.isDirectory(path)) {
                deleteFolder(path);
            }
            else {
                Files.delete(path);
            }
        }
        Files.delete(folder);
    }

    /**
     * Calls {@link WordUtils#capitalizeFully}
     */
    @SuppressWarnings("deprecation")
    public static String capitalizeFully(String str) {
        return WordUtils.capitalizeFully(str);
    }
}
