package prtest.tree;

import prtest.Rule;

import java.util.List;

/**
 * Tests for <code>KeysValues</code> modification.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologTreeKeysValuesTest {
    public static void main(final String... args) {
        PrologTreeTest.test(args, PrologTreeKeysValuesTest.class, test -> {
            test.checker(Rule.func("map_keys", 1), map -> List.copyOf(map.keySet()));
            test.checker(Rule.func("map_values", 1), map -> List.copyOf(map.values()));
        });
    }
}
