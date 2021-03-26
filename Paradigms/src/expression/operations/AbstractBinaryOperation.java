package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;
import expression.exceptions.EvaluateException;

public abstract class AbstractBinaryOperation implements GenericExpression {
    protected final GenericExpression first, second;
    protected final String operationToken;

    public AbstractBinaryOperation(final GenericExpression first, final GenericExpression second, final String operationToken) {
        this.first = first;
        this.second = second;
        this.operationToken = operationToken;
    }

    protected abstract <T> T calculate(T x, T y, Calculator<T> calculator) throws EvaluateException;

    @Override
    public <T> T evaluate(final T x, final T y, final T z, final Calculator<T> calculator) throws EvaluateException {
        try {
            return calculate(first.evaluate(x, y, z, calculator), second.evaluate(x, y, z, calculator), calculator);
        } catch (final ArithmeticException e) {
            throw new EvaluateException("Exception caused by arithmetic exception: " + e.getMessage(), e);
        }
    }

    @Override
    public String toString() {
        return "(" + first.toString() + " " + operationToken + " " + second.toString() + ")";
    }
}
