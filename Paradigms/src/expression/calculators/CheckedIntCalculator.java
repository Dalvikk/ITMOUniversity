package expression.calculators;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.OverflowException;

public class CheckedIntCalculator extends IntCalculator {
    @Override
    public Integer add(final Integer x, final Integer y) throws OverflowException {
        if ((x > 0 && y > 0 && Integer.MAX_VALUE - y < x)
                || (x < 0 && y < 0 && Integer.MIN_VALUE - y > x)) {
            throw new OverflowException("Overflow");
        }
        return super.add(x, y);
    }

    @Override
    public Integer sub(final Integer x, final Integer y) throws OverflowException {
        if ((x >= 0 && y < 0 && Integer.MAX_VALUE + y < x) ||
                (x < 0 && y > 0 && Integer.MIN_VALUE + y > x)) {
            throw new OverflowException("Overflow");
        }
        return super.sub(x, y);
    }

    @Override
    public Integer mul(final Integer x, final Integer y) throws OverflowException {
        if ((x > 0 && y > 0 && Integer.MAX_VALUE / x < y) ||
                (x < 0 && y < 0 && Integer.MAX_VALUE / x > y) ||
                ((x > 0 && y < 0) && Integer.MIN_VALUE / x > y) ||
                ((x < 0 && y > 0) && Integer.MIN_VALUE / y > x)) {
            throw new OverflowException("Overflow");
        }
        return super.mul(x, y);
    }

    @Override
    public Integer div(final Integer x, final Integer y) throws OverflowException, DivisionByZeroException {
        if (x == Integer.MIN_VALUE && y == -1) {
            throw new OverflowException("Overflow");
        }
        return super.div(x, y);
    }

    @Override
    public Integer neg(final Integer x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow");
        }
        return super.neg(x);
    }

    @Override
    public Integer abs(final Integer x) throws OverflowException {
        if (x == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow");
        }
        return super.abs(x);
    }

    @Override
    public Integer square(final Integer x) {
        if ((x > 0 && Integer.MAX_VALUE / x < x) ||
                (x < 0 && Integer.MAX_VALUE / x > x)) {
            throw new OverflowException("Overflow");
        }
        return super.square(x);
    }
}
