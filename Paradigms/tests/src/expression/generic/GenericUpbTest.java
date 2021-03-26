package expression.generic;

import java.util.stream.IntStream;

/**
 * Generic unchecked int, float, byte test.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class GenericUpbTest extends GenericTest {
    protected static final int MOD = 1009;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private static final int[] INVERSES = IntStream.range(0, MOD)
            .map(a -> IntStream.iterate(a, r -> mul(r, a)).skip(MOD - 3).findFirst().getAsInt())
            .toArray();

    public GenericUpbTest() {
        upbConst("10", 10);
        upb(
                "x",
                (x, y, z) -> x,
                (x, y, z) -> x,
                (x, y, z) -> b(x)
        );
        upb(
                "y + 2",
                (x, y, z) -> y + 2,
                (x, y, z) -> add(y, 2),
                (x, y, z) -> b(y + 2)
        );
        upb(
                "z / 2",
                (x, y, z) -> z / 2,
                (x, y, z) -> div(z, 2),
                (x, y, z) -> b(z / 2)
        );
        upb(
                "y / z",
                (x, y, z) -> y / z,
                (x, y, z) -> div(y, z),
                (x, y, z) -> b(y / z)
        );
        upb(
                "100 * x * y * 100 + z",
                (x, y, z) -> i(100 * x * y * 100 + z),
                (x, y, z) -> add(mul(mul(mul(100, x), y), 100), z),
                (x, y, z) -> b(100 * x * y * 100 + z)
        );
        upb(
                "x * y + (z - 1) / 10",
                (x, y, z) -> x * y + (z - 1) / 10,
                (x, y, z) -> add(mul(x, y), div(mod(z - 1), 10)),
                (x, y, z) -> b(x * y + (z - 1) / 10)
        );
    }

    public static int mod(final int n) {
        return n >= 0 ? n % MOD : MOD + n % MOD;
    }

    protected static int add(final int a, final int b) { return mod(a + b); }
    protected static int sub(final int a, final int b) { return mod(a - b); }
    protected static int mul(final int a, final int b) { return mod(a * b); }

    protected static int div(final int a, final int b) {
        if (b == 0) {
            throw new ArithmeticException("DBZ");
        }
        return mod(a * INVERSES[b]);
    }

    protected static byte b(final int a) {
        return (byte) a;
    }

    protected void upbConst(final String expression, final int v) {
        upbConst(expression, v, mod(v), (byte) v);
    }

    protected void upbConst(final String expression, final int u, final int p, final byte b) {
        upb(expression, (x, y, z) -> u, (x, y, z) -> p, (x, y, z) -> b);
    }

    protected void upb(final String expression, final F<Integer> fu, final F<Integer> fp, final F<Byte> fb) {
        test(expression, "u", fu);
        test(expression, "p", (x, y, z) -> fp.apply(mod(x), mod(y), mod(z)));
        test(expression, "b", (x, y, z) -> fb.apply(b(x), b(y), b(z)));
    }

    public static void main(final String[] args) {
        new GenericUpbTest().run();
    }
}
