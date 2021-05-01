package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;

public class Add extends AbstractBinaryOperation {
    public Add(final GenericExpression first, final GenericExpression second) {
        super(first, second, "+");
    }

    @Override
    protected <T> T calculate(final T x, final T y, final Calculator<T> calculator) {
        return calculator.add(x, y);
    }
}
