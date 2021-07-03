package prtest.primes;

import prtest.Rule;

import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologSquareTest {
    public static void main(final String... args) {
        PrologPrimesTest.test(args, PrologSquareTest.class, t -> t.checkDivisors(
                new Rule("square_divisors", 2),
                t.reversible,
                s -> s.flatMap(v -> IntStream.of(v, v))
        ));
    }
}
