package expression.exceptions;

public class MessageCreator {
    public static String createHighlightMessage(final String message, final String expression, final int pos) {
        if (pos < 1 || pos > expression.length()) {
            throw new IllegalArgumentException();
        }
        final String up = "=".repeat(expression.length());
        final String down = "=".repeat(pos - 1) + "^" + "=".repeat(expression.length() - pos);
        return message +
                System.lineSeparator() +
                up + System.lineSeparator() +
                expression + System.lineSeparator() +
                down;
    }
}
