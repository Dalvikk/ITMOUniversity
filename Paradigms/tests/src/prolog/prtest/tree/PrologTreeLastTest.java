package prtest.tree;

import expression.BaseTest;
import prtest.Rule;
import prtest.Value;

import java.util.Map;
import java.util.NavigableMap;

/**
 * Tests for <code>Last</code> modification.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologTreeLastTest {
    public static void main(final String... args) {
        PrologTreeTest.test(true, true, PrologTreeLastTest.class, test -> {
            if (BaseTest.mode(args, "easy", "hard") == 0) {
                test.clearUpdaters();
            }

            test.checker(Rule.func("map_getLast", 1), NavigableMap::lastEntry);
            test.noneUpdater(
                    Rule.func("map_removeLast", 1),
                    state -> {
                        final Map.Entry<Integer, Value> entry = state.expected.pollLastEntry();
                        if (entry != null) {
                            state.keys.remove(entry.getKey());
                            state.values.remove(entry.getValue());
                        }
                    }
            );
        });
    }
}
