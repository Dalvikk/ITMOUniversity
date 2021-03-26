package expression.calculators;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ModByZeroException;

public class IntCalculator implements Calculator<Integer> {
    @Override
    public Integer add(final Integer x, final Integer y) {
        return x + y;
    }

    @Override
    public Integer sub(final Integer x, final Integer y) {
        return x - y;
    }

    @Override
    public Integer mul(final Integer x, final Integer y) {
        return x * y;
    }

    @Override
    public Integer div(final Integer x, final Integer y) throws DivisionByZeroException {
        if (y == 0) {
            throw new DivisionByZeroException("Division vy zero");
        }
        return x / y;
    }

    @Override
    public Integer neg(final Integer x) {
        return -x;
    }

    @Override
    public Integer parse(final String s) {
        return Integer.parseInt(s);
    }

    @Override
    public Integer mod(final Integer x, final Integer y) {
        if (y == 0) {
            throw new ModByZeroException("mod by zero");
        }
        return x % y;
    }

    @Override
    public Integer abs(final Integer x) {
        return Math.abs(x);
    }

    @Override
    public Integer square(final Integer x) {
        return x * x;
    }
}
