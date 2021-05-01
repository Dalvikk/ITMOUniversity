package cljtest.functional;

import cljtest.multi.MultiMeanVarnTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureFunctionalMeanVarnTest {
    public static void main(final String... args) {
        ClojureFunctionalExpressionTest.test(args, ClojureFunctionalMeanVarnTest.class, MultiMeanVarnTests::new);
    }
}
