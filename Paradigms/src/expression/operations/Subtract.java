package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;

public class Subtract extends AbstractBinaryOperation {
    public Subtract(final GenericExpression first, final GenericExpression second) {
        super(first, second, "-");
    }

    @Override
    protected <T> T calculate(final T x, final T y, final Calculator<T> calculator) {
        return calculator.sub(x, y);
    }
}
