package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;
import expression.exceptions.OverflowException;

public class Const implements GenericExpression {
    private final String value;

    public Const(final String value) {
        this.value = value;
    }

    @Override
    public <T> T evaluate(final T x, final T y, final T z, final Calculator<T> calculator) {
        final T result;
        try {
            result = calculator.parse(value);
        } catch (final NumberFormatException ignored) {
            throw new OverflowException("Constant overflow");
        }
        return result;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
