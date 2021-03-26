package expression.exceptions;

public class MissingRightBracketException extends ParseException {
    public MissingRightBracketException(final String message) {
        super(message);
    }
}
