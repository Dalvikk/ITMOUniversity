package expression;

import base.Asserts;
import base.Randomized;
import base.TestCounter;

import java.util.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract strictfp class BaseTest extends Asserts implements Randomized {
    protected final Random random = Randomized.initRandom(4375084350844354352L, getClass());

    protected final TestCounter counter = new TestCounter();

    protected BaseTest() {
        Locale.setDefault(Locale.US);

        checkAssert(getClass());
    }

    protected <T> T random(final List<T> variants) {
        return variants.get(random.nextInt(variants.size()));
    }

    public void run() {
        run(getClass());
    }

    public void run(final Class<?> test, final String... args) {
        System.out.println("=== Testing " + test.getSimpleName() + " " + String.join(" ", args));
        test();
        counter.printStatus(test, args);
    }

    protected abstract void test();

    @SafeVarargs
    protected static <T> List<T> list(final T... items) {
        return new ArrayList<>(Arrays.asList(items));
    }

    protected static void addRange(final List<Integer> values, final int d, final int c) {
        for (int i = -d; i <= d; i++) {
            values.add(c + i);
        }
    }

    public Random getRandom() {
        return random;
    }

    public static final class Op<T> {
        public final String name;
        public final T f;

        private Op(final String name, final T f) {
            this.name = name;
            this.f = f;
        }
    }

    public static <T> Op<T> op(final String name, final T f) {
        return new Op<>(name, f);
    }

    public static int mode(final String[] args, final String... modes) {
        if (args.length != 1) {
            throw error("Single argument expected. Supported modes: %s", Arrays.asList(modes));
        }
        final int index = List.of(modes).indexOf(args[0]);
        if (index < 0) {
            throw error("Invalid mode '%s'. Supported moves: %s", args[0], Arrays.asList(modes));
        }
        return index;
    }
}
