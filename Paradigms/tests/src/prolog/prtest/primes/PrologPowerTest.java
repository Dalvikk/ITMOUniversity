package prtest.primes;

import prtest.Rule;

import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologPowerTest {
    private static final Rule POWER_DIVISORS = new Rule("power_divisors", 3);

    public static void main(final String... args) {
        PrologPrimesTest.test(args, PrologPowerTest.class, t -> IntStream.of(2, 3, 1, 0, 5).forEach(p -> t.checkDivisors(
                POWER_DIVISORS.bind(1, p),
                t.reversible,
                s -> s.flatMap(v -> IntStream.generate(() -> v).limit(p)))
        ));
    }
}
