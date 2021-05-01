package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;

public class Negate extends AbstractUnaryOperation {
    public Negate(final GenericExpression term) {
        super(term, "-");
    }

    @Override
    protected <T> T calculate(final T x, final Calculator<T> calculator) {
        return calculator.neg(x);
    }
}
