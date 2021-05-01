package expression.calculators;

import expression.exceptions.DivisionByZeroException;

public class ModCalculator extends IntCalculator {
    private static final int MOD = 1009;
    private static final int[] INVERSES = initInverses();

    private static int[] initInverses() {
        final int[] a = new int[MOD];
        a[1] = 1;
        for (int i = 2; i < MOD; ++i) {
            a[i] = (MOD - (MOD / i) * a[MOD % i] % MOD) % MOD;
        }
        return a;
    }

    private static int getByMod(final Integer n) {
        return n >= 0 ? n % MOD : MOD + n % MOD;
    }

    @Override
    public Integer add(final Integer x, final Integer y) {
        return getByMod(super.add(x, y));
    }

    @Override
    public Integer sub(final Integer x, final Integer y) {
        return getByMod(super.sub(x, y));
    }

    @Override
    public Integer mul(final Integer x, final Integer y) {
        return getByMod(super.mul(x, y));
    }

    @Override
    public Integer div(final Integer x, final Integer y) throws DivisionByZeroException {
        if (y == 0) {
            throw new DivisionByZeroException("Division by zero");
        }
        return getByMod(mul(x, INVERSES[y]));
    }

    @Override
    public Integer parse(final String s) {
        return getByMod(super.parse(s));
    }

    @Override
    public Integer mod(final Integer x, final Integer y) {
        return getByMod(super.mod(x, y));
    }

    @Override
    public Integer abs(final Integer x) {
        return getByMod(super.abs(x));
    }

    @Override
    public Integer square(final Integer x) {
        return getByMod(super.square(x));
    }
}
