package expression.generic;

/**
 * Abs, square, mod over unchecked int, long, short test.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class GenericAsmUlsTest extends GenericUlsTest {
    public GenericAsmUlsTest() {
        GenericAsmTest.addAsm(this);
        uls(
                "1 + 5 mod 3",
                (x, y, z) -> 1 + 5 % 3,
                (x, y, z) -> 1 + 5 % 3L,
                (x, y, z) -> s(1 + 5 % 3)
        );
        uls(
                "x + y mod (z + 1)",
                (x, y, z) -> x + y % (z + 1),
                (x, y, z) -> x + y % (z + 1L),
                (x, y, z) -> s(x + y % (z + 1))
        );
        uls(
                "abs -5",
                (x, y, z) -> 5,
                (x, y, z) -> 5L,
                (x, y, z) -> s(5)
        );
        uls(
                "abs (x - y) / z",
                (x, y, z) -> Math.abs(x - y) / z,
                (x, y, z) -> Math.abs(x - y) / (long) z,
                (x, y, z) -> s(Math.abs(x - y) / z)
        );
        uls(
                "square -5",
                (x, y, z) -> 25,
                (x, y, z) -> 25L,
                (x, y, z) -> s(25)
        );
        uls(
                "square x - y / z",
                (x, y, z) -> x * x - y / z,
                (x, y, z) -> x * (long) x - y / z,
                (x, y, z) -> s(x * x - y / z)
        );
    }

    public static void main(final String[] args) {
        new GenericAsmUlsTest().run();
    }
}
