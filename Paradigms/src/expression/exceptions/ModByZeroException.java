package expression.exceptions;

public class ModByZeroException extends EvaluateException {
    public ModByZeroException(final String message) {
        super(message);
    }
}
