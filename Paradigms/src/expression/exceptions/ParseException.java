package expression.exceptions;

public class ParseException extends RuntimeException {
    public ParseException(final String message) {
        super(message);
    }
}
