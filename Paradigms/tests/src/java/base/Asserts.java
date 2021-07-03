package base;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Asserts {
    static {
        Locale.setDefault(Locale.US);
    }

    public static void assertEquals(final String message, final Object expected, final Object actual) {
        final String reason = String.format(
                "%s:%n     expected `%s`,%n       actual `%s`",
                message,
                toString(expected),
                toString(actual)
        );
        assertTrue(reason, Objects.deepEquals(expected, actual));
    }

    private static String toString(final Object value) {
        final String result = Arrays.deepToString(new Object[]{value});
        return result.substring(1, result.length() - 1);
    }

    public static void assertTrue(final String message, final boolean value) {
        if (!value) {
            throw new AssertionError(message);
        }
    }

    public static void assertEquals(final String message, final int expected, final int actual) {
        assertTrue(String.format("%s: Expected %d, found %d", message, expected, actual), actual == expected);
    }

    public static void assertEquals(final String message, final double precision, final double expected, final double actual) {
        assertTrue(
                String.format("%s: Expected %.12f, found %.12f", message, expected, actual),
                isEqual(precision, expected, actual)
        );
    }

    public static boolean isEqual(final double precision, final double expected, final double actual) {
        return Math.abs(actual - expected) < precision
                || Math.abs(actual - expected) < precision * Math.abs(expected)
                || !Double.isFinite(expected)
                || Math.abs(expected) > 1e100
                || Math.abs(expected) < precision && !Double.isFinite(actual);
    }

    public static void checkAssert(final Class<?> c) {
        if (!c.desiredAssertionStatus()) {
            throw error("You should enable assertions by running 'java -ea %s'", c.getName());
        }
    }

    public static AssertionError error(final String format, final Object... args) {
        return new AssertionError(String.format(format, args));
    }

    public static AssertionError error(final String context, final String format, final Object... args) {
        return new AssertionError(context + "\n" + String.format(format, args));
    }
}
