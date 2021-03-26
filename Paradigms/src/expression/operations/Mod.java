package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;

public class Mod extends AbstractBinaryOperation {
    public Mod(final GenericExpression first, final GenericExpression second) {
        super(first, second, "mod");
    }

    @Override
    protected <T> T calculate(final T x, final T y, final Calculator<T> calculator) {
        return calculator.mod(x, y);
    }
}
