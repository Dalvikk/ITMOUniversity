package expression.exceptions;

public class EvaluateException extends RuntimeException {
    public EvaluateException(final String message) {
        super(message);
    }

    public EvaluateException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
