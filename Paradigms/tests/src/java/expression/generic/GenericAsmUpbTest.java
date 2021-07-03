package expression.generic;

/**
 * Abs, square, mod over unchecked int, float, byte test.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class GenericAsmUpbTest extends GenericUpbTest {
    public GenericAsmUpbTest() {
        GenericAsmTest.addAsm(this);
        upb(
                "1 + 5 mod 3",
                (x, y, z) -> 1 + 5 % 3,
                (x, y, z) -> add(1, 5 % 3),
                (x, y, z) -> b(1 + 5 % 3)
        );
        upb(
                "x + y mod (z + 1)",
                (x, y, z) -> x + y % (z + 1),
                (x, y, z) -> add(x, y % add(z, 1)),
                (x, y, z) -> b(x + y % (z + 1))
        );
        upbConst("abs -5", 5, MOD - 5, b(5));
        upb(
                "abs (x - y) / z",
                (x, y, z) -> Math.abs(x - y) / z,
                (x, y, z) -> div(sub(x, y), z),
                (x, y, z) -> b(Math.abs(x - y) / z)
        );

        upbConst("square -5", 25);
        upbConst("square -100", 100 * 100);
        upb(
                "x -square y",
                (x, y, z) -> x - y * y,
                (x, y, z) -> sub(x, sqr(y)),
                (x, y, z) -> b(x - y * y)
        );
        upb(
                "square -y",
                (x, y, z) -> y * y,
                (x, y, z) -> sqr(y),
                (x, y, z) -> b(y * y)
        );
        upb(
                "square-y",
                (x, y, z) -> y * y,
                (x, y, z) -> sqr(y),
                (x, y, z) -> b(y * y)
        );
        upb(
                "square - y",
                (x, y, z) -> y * y,
                (x, y, z) -> sqr(y),
                (x, y, z) -> b(y * y)
        );
        upb(
                "square(y * z)",
                (x, y, z) -> y * z * y * z,
                (x, y, z) -> sqr(mul(y, z)),
                (x, y, z) -> b(b(y * z) * b(y * z))
        );
        upb(
                "square x - y / z",
                (x, y, z) -> x * x - y / z,
                (x, y, z) -> sub(sqr(x), div(y, z)),
                (x, y, z) -> b(x * x - y / z)
        );
    }

    public int sqr(final int a) {
        return mul(a, a);
    }

    public static void main(final String[] args) {
        new GenericAsmUpbTest().run();
    }
}
