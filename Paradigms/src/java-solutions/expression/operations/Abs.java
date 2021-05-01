package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;

public class Abs extends AbstractUnaryOperation {
    public Abs(final GenericExpression term) {
        super(term, "abs");
    }

    @Override
    protected <T> T calculate(final T x, final Calculator<T> calculator) {
        return calculator.abs(x);
    }
}
