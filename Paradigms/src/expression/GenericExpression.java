package expression;

import expression.calculators.Calculator;
import expression.exceptions.EvaluateException;

public interface GenericExpression {
    <T> T evaluate(T x, T y, T z, Calculator<T> calculator) throws EvaluateException;
}
