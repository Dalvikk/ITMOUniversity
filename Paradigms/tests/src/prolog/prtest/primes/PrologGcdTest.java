package prtest.primes;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologGcdTest {
    private static long gcd(final long a, final long b) {
        return a == 0 ? b : gcd(b % a, a);
    }

    public static void main(final String... args) {
        PrologPrimesTest.test(args, PrologGcdTest.class, t -> t.testBinary("gcd", PrologGcdTest::gcd));
    }
}
