package prtest.map;

import base.TestCounter;
import prtest.PrologTest;

import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologMapTest extends PrologTest {
    private final boolean updates;
    private final boolean sorted;
    private final TestCounter counter = new TestCounter();
    private final MapChecker<?> test;

    public PrologMapTest(
            final boolean updates,
            final boolean sorted,
            final Path file,
            final Function<PrologMapTest, MapChecker<?>> testFactory
    ) {
        super(file);
        this.updates = updates;
        this.sorted = sorted;
        test = testFactory.apply(this);
    }

    protected void test() {
        run();
        counter.printStatus(getClass(), toString());
    }

    @Override
    public String toString() {
        return super.getClass().getSimpleName() + "(" + updates + ")";
    }

    @Override
    public void run() {
        for (int i = 0; i < 10; i++) {
            test.check(new Settings(i, 10, updates ? 10 : 0, sorted, true));
        }
        test.check(new Settings(100, 10000, updates ? 100 : 0, sorted, false));
        test.check(new Settings(200, 10000, 0, sorted, false));
    }

    protected void check(final Runnable check) {
        counter.nextTest();
        check.run();
        counter.passed();
    }

    public static <M> void test(
            final Class<?> type,
            final Path file,
            final boolean updates,
            final boolean sorted,
            final Consumer<MapChecker<M>> addTests,
            final Function<PrologMapTest, MapChecker<M>> testFactory
    ) {
        final Function<PrologMapTest, MapChecker<?>> newFactory = test -> {
            final MapChecker<M> tests = testFactory.apply(test);
            addTests.accept(tests);
            return tests;
        };
        new PrologMapTest(updates, sorted, file, newFactory).run(type);
    }
}
