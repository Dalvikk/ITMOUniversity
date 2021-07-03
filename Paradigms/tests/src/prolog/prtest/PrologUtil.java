package prtest;

import alice.tuprolog.Struct;
import alice.tuprolog.Term;
import alice.tuprolog.Theory;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologUtil {
    private static final Term[] NO_TERMS = new Term[0];
    public static final Rule FIB_BENCHMARK = Rule.func("fib_benchmark", 1);

    private static long benchmark = 0;

    /** Utility class. */
    private PrologUtil() {}

    public static Struct pure(final String functor) {
        return Struct.of(functor, NO_TERMS);
    }

    public static long measure(final String description, final Runnable action) {
        final long start = System.currentTimeMillis();
        action.run();
        final long time = System.currentTimeMillis() - start;
        System.out.printf("%s done in %dms%n", description, time);
        return time;
    }

    @SuppressWarnings("deprecation")
    public static long benchmark() {
        if (benchmark == 0) {
            final PrologScript prolog = new PrologScript();
            prolog.addTheory(new Theory(
                    "fib_benchmark(N, R) :- fib_benchmark_table(N, R), !.\n" +
                            "fib_benchmark(1, 1).\n" +
                            "fib_benchmark(2, 1).\n" +
                            "fib_benchmark(N, R) :-\n" +
                            "    N > 0,\n" +
                            "    N1 is N - 1, fib_benchmark(N1, R1),\n" +
                            "    N2 is N - 2, fib_benchmark(N2, R2),\n" +
                            "    R is mod(R1 + R2, 100000000),\n" +
                            "    assertz(fib_benchmark_table(N, R)).\n"
            ));
            benchmark = PrologUtil.measure(
                    "Benchmark",
                    () -> prolog.solveOne(FIB_BENCHMARK, 2_000)
            );
        }
        return benchmark;
    }
}
