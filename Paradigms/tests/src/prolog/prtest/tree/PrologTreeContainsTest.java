package prtest.tree;

import prtest.Rule;

import java.util.Map;

/**
 * Tests for <code>Contains</code> modification.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologTreeContainsTest {
    public static void main(final String... args) {
        PrologTreeTest.test(args, PrologTreeContainsTest.class, test -> {
            test.keyChecker(new Rule("map_containsKey", 2), Map::containsKey);
            test.valueChecker(new Rule("map_containsValue", 2), Map::containsValue);
        });
    }
}
