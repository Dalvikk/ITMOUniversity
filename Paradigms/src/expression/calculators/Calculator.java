package expression.calculators;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ModByZeroException;

public interface Calculator<T> {
    T add(T x, T y);

    T sub(T x, T y);

    T mul(T x, T y);

    T div(T x, T y) throws DivisionByZeroException;

    T neg(T x);

    T parse(String s) throws NumberFormatException;

    T mod(T x, T y) throws ModByZeroException;

    T abs(T x);

    T square(T x);
}
