package expression.exceptions;

public class DivisionByZeroException extends EvaluateException {
    public DivisionByZeroException(final String message) {
        super(message);
    }
}
