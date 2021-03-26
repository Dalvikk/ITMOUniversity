package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;

public class Square extends AbstractUnaryOperation {
    public Square(final GenericExpression term) {
        super(term, "square");
    }

    @Override
    protected <T> T calculate(final T x, final Calculator<T> calculator) {
        return calculator.square(x);
    }
}
