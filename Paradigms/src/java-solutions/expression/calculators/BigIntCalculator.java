package expression.calculators;

import expression.exceptions.DivisionByZeroException;
import expression.exceptions.ModByZeroException;

import java.math.BigInteger;

public class BigIntCalculator implements Calculator<BigInteger> {
    @Override
    public BigInteger add(final BigInteger x, final BigInteger y) {
        return x.add(y);
    }

    @Override
    public BigInteger sub(final BigInteger x, final BigInteger y) {
        return x.subtract(y);
    }

    @Override
    public BigInteger mul(final BigInteger x, final BigInteger y) {
        return x.multiply(y);
    }

    @Override
    public BigInteger div(final BigInteger x, final BigInteger y) {
        if (y.equals(BigInteger.ZERO)) {
            throw new DivisionByZeroException("Division by 0");
        }
        return x.divide(y);
    }

    @Override
    public BigInteger neg(final BigInteger x) {
        return x.negate();
    }

    @Override
    public BigInteger parse(final String s) {
        return new BigInteger(s);
    }

    @Override
    public BigInteger mod(final BigInteger x, final BigInteger y) {
        if (y.equals(BigInteger.ZERO)) {
            throw new ModByZeroException("Mod by zero");
        }
        return x.mod(y);
    }

    @Override
    public BigInteger abs(final BigInteger x) {
        return x.abs();
    }

    @Override
    public BigInteger square(final BigInteger x) {
        return x.multiply(x);
    }
}
