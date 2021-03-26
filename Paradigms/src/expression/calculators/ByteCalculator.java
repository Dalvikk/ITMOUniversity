package expression.calculators;

import expression.exceptions.DivisionByZeroException;

public class ByteCalculator implements Calculator<Byte> {
    @Override
    public Byte add(final Byte x, final Byte y) {
        return (byte) (x + y);
    }

    @Override
    public Byte sub(final Byte x, final Byte y) {
        return (byte) (x - y);
    }

    @Override
    public Byte mul(final Byte x, final Byte y) {
        return (byte) (x * y);
    }

    @Override
    public Byte div(final Byte x, final Byte y) {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        return (byte) (x / y);
    }

    @Override
    public Byte neg(final Byte x) {
        return (byte) (-x);
    }

    @Override
    public Byte parse(final String s) {
        return (byte) Integer.parseInt(s);
    }

    @Override
    public Byte mod(final Byte x, final Byte y) {
        return (byte) (x % y);
    }

    @Override
    public Byte abs(final Byte x) {
        return (byte) Math.abs(x);
    }

    @Override
    public Byte square(final Byte x) {
        return (byte) (x * x);
    }
}
