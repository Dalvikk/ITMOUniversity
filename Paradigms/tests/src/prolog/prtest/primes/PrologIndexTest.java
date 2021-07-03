package prtest.primes;

import prtest.PrologScript;
import prtest.Rule;

import static prtest.primes.PrologPrimesTest.test;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologIndexTest {
    public static final Rule PRIME_INDEX = new Rule("prime_index", 2);
    public static final Rule INDEX = PRIME_INDEX.func();
    public static final Rule BACK = PRIME_INDEX.bind(0, PrologScript.V);

    private static void check(final PrologPrimesTest t, final int i) {
        final int prime = t.primes[i];
        t.assertResult(i + 1, INDEX, prime);
        t.assertResult(t.primes[i + 1] == prime + 2 ? i + 2 : null, INDEX, prime + 2);
        if (t.reversible) {
            t.assertResult(prime, BACK, i + 1);
            t.assertResult(t.primes[i + 1], BACK, i + 2);
        }
    }

    public static void main(final String... args) {
        test(args, PrologIndexTest.class, t -> {
            for (int i = 0; i < 10; i++) {
                check(t, i);
            }
            for (int i = 0; t.primes[i] * t.primes[i] < t.max * 10; i += 10) {
                check(t, i);
            }
        });
    }
}
