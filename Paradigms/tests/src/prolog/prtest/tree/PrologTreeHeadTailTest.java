package prtest.tree;

import prtest.Rule;
import prtest.map.State;

/**
 * Tests for <code>HeadTail</code> modification.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrologTreeHeadTailTest {
    public static void main(final String... args) {
        PrologTreeTest.test(args, PrologTreeHeadTailTest.class, test -> {
            test.checker(Rule.func("map_headMapSize", 2), State::randomKey, (map, key) -> map.headMap(key).size());
            test.checker(Rule.func("map_tailMapSize", 2), State::randomKey, (map, key) -> map.tailMap(key).size());
        });
    }
}
