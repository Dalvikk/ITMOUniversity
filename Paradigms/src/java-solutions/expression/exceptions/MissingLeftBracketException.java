package expression.exceptions;

public class MissingLeftBracketException extends ParseException {

    public MissingLeftBracketException(final String message) {
        super(message);
    }
}
