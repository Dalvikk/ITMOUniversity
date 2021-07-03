package prtest.tree;

import expression.BaseTest;
import prtest.Rule;
import prtest.Value;
import prtest.map.Entry;
import prtest.map.MapChecker;
import prtest.map.PrologMapTest;

import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Tests for <code>Basic</code> modification.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologTreeTest {
    private static final Rule BUILD = Rule.func("map_build", 1);
    public static Path SOLUTION = Path.of("tree-map.pl");

    public static void main(final String... args) {
        test(args, PrologTreeTest.class, test -> {});
    }

    public static void test(final String[] args, final Class<?> type, final Consumer<MapChecker<Void>> addTests) {
        final boolean hard = BaseTest.mode(args, "easy", "hard") == 1;
        test(hard, !hard, type, addTests);
    }

    public static void test(final boolean updates, final boolean sorted, final Class<?> type, final Consumer<MapChecker<Void>> addTests) {
        PrologMapTest.test(type, SOLUTION, updates, sorted, addTests, test -> new MapChecker<>(
                test,
                entries -> null,
                getListValueFunction(test),
                (map, key, entry) -> {},
                (map, key) -> {},
                state -> {
                    for (final Integer key : state.keys.test()) {
                        state.get(key);
                    }
                }
        ));
    }

    private static Function<List<Entry>, Value> getListValueFunction(final PrologMapTest test) {
        return entries -> test.solveOne(BUILD, Value.list(entries, Entry::toValue)).value;
    }
}
