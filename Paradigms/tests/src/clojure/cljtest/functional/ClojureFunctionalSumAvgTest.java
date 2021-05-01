package cljtest.functional;

import cljtest.multi.MultiSumAvgTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureFunctionalSumAvgTest {
    public static void main(final String... args) {
        ClojureFunctionalExpressionTest.test(args, ClojureFunctionalSumAvgTest.class, MultiSumAvgTests::new);
    }
}
