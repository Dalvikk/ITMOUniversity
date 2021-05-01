package expression.operations;

import expression.GenericExpression;
import expression.calculators.Calculator;
import expression.exceptions.EvaluateException;

public abstract class AbstractUnaryOperation implements GenericExpression {
    protected final GenericExpression term;
    protected final String operationToken;

    public AbstractUnaryOperation(final GenericExpression term, final String operationToken) {
        this.term = term;
        this.operationToken = operationToken;
    }

    protected abstract <T> T calculate(T x, Calculator<T> calculator) throws EvaluateException;

    @Override
    public <T> T evaluate(final T x, final T y, final T z, final Calculator<T> calculator) throws EvaluateException {
        return calculate(term.evaluate(x, y, z, calculator), calculator);
    }

    @Override
    public String toString() {
        return "(" + operationToken + term.toString() + ")";
    }
}
