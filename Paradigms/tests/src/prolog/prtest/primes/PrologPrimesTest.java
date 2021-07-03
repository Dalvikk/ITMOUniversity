package prtest.primes;

import base.Asserts;
import expression.BaseTest;
import prtest.PrologTest;
import prtest.PrologUtil;
import prtest.Rule;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.LongBinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologPrimesTest extends PrologTest {
    private static final Rule INIT = new Rule("init", 1);
    private static final Rule PRIME = new Rule("prime", 1);
    private static final Rule COMPOSITE = new Rule("composite", 1);
    private static final Rule PRIME_DIVISORS = new Rule("prime_divisors", 2);
    private static final int MAX_Q = 100_000;

    public static double maxRatio = 0;
    public static String SUFFIX = ".pl";

    final int max;
    final int[] primes;
    final BitSet isPrime = new BitSet();

    private final Consumer<PrologPrimesTest> check;
    public final boolean reversible;

    private PrologPrimesTest(final int max, final boolean reversible, final Consumer<PrologPrimesTest> check) {
        super(Path.of("primes" + SUFFIX));

        this.max = max;
        this.reversible = reversible;
        this.check = check;

        primes = primes(max);
        Arrays.stream(primes).forEach(isPrime::set);
    }

    public void test() {
        final long limit = PrologUtil.benchmark() * 40;
        final long time = PrologUtil.measure("Tests", this::runTests);
        maxRatio = Math.max(maxRatio, time / (double) limit);
        Asserts.assertTrue(String.format("Time limit exceeded: %sms instead of %sms", time, limit), time <= limit);
    }

    public void runTests() {
        PrologUtil.measure("Init", () -> System.out.println("Init: " + test(INIT, max)));
        PrologUtil.measure("checkPrimes", this::checkPrimes);
        PrologUtil.measure("checkComposites", this::checkComposites);

        PrologUtil.measure("prime_divisors", () -> checkDivisors(PRIME_DIVISORS, reversible, Function.identity()));
        PrologUtil.measure("check", () -> check.accept(this));
    }

    void checkDivisors(final Rule rule, final boolean reversible, final Function<IntStream, IntStream> solution) {
        final Rule function = rule.func();
        final Rule reverse = rule.func(0);
        checkDivisors(i -> {
            counter.nextTest();
            final List<Integer> divisors = solution.apply(divisors(i)).boxed().collect(Collectors.toList());
            assertResult(divisors, function, i);

            if (reversible) {
                assertResult(i, reverse, divisors);
                final int hashCode = divisors.hashCode();
                Collections.shuffle(divisors, random);
                if (divisors.hashCode() != hashCode) {
                    assertResult(null, reverse, divisors);
                }
            }
            counter.passed();
        });
    }

    private void checkDivisors(final IntConsumer checker) {
        for (int i = 1; i < 10; i++) {
            checker.accept(i);
        }
        checker.accept(255);
        checker.accept(256);

        for (int i = 0; i < primes.length / 10; i++) {
            checker.accept(randomN());
        }
    }

    int randomN() {
        return randomInt(1, max);
    }

    private void checkComposites() {
        for (int i = 0; i < Math.min(primes.length, MAX_Q); i++) {
            checkPrime(randomN());
        }
    }

    private void checkPrimes() {
        for (int i = 0; i < primes.length; i++) {
            checkPrime(primes[i]);
            if (i > MAX_Q) {
                i += 5;
            }
        }
    }

    private void checkPrime(final int value) {
        counter.nextTest();
        final boolean prime = isPrime.get(value);
        assertSuccess(prime, PRIME, value);
        assertSuccess(!prime, COMPOSITE, value);
        counter.passed();
    }

    private IntStream divisors(final int n) {
        final IntStream.Builder divisors = IntStream.builder();
        int value = n;
        for (final int prime : primes) {
            if (prime * prime > n) {
                break;
            }
            while (value % prime == 0) {
                divisors.add(prime);
                value /= prime;
            }
        }
        if (value > 1) {
            divisors.add(value);
        }
        return divisors.build();
    }

    protected void testBinary(final String name, final LongBinaryOperator op) {
        final Rule f = Rule.func(name, 2);

        for (int a = 1; a < 10; a++) {
            for (int b = 1; b < 10; b++) {
                assertResult(op, f, a, b);
            }
        }
        for (int i = 0; i < 1000; i++) {
            assertResult(op, f, randomN(), randomN());
        }
    }

    private void assertResult(final LongBinaryOperator op, final Rule f, final int a, final int b) {
        assertResult(op.applyAsLong(a, b), f, a, b);
    }

    private static int[] primes(final int max) {
        final int[] primes = new int[(int) (2 * max / Math.log(max))];
        primes[0] = 2;
        int t = 1;
        int h = 0;
        for (int i = 3; i <= max; i += 2) {
            primes[t++] = i;
            if (primes[h] * primes[h] <= i) {
                h++;
            }
            for (int j = 0; j < h; j++) {
                if (i % primes[j] == 0) {
                    t--;
                    break;
                }
            }
        }
        return Arrays.copyOf(primes, t);
    }

    public static void main(final String... args) {
        test(args, PrologPrimesTest.class, t -> {});
    }

    protected static void test(final String[] args, final Class<?> test, final Consumer<PrologPrimesTest> check) {
        final int mode = BaseTest.mode(args, "easy", "hard", "bonus");
        final int max = (int) (1000 * Math.pow(100, mode));
        new PrologPrimesTest(max, mode > 0, check).run(test);
    }
}
