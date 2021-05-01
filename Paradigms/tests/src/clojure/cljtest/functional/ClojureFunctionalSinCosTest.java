package cljtest.functional;

import cljtest.multi.MultiSinCosTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureFunctionalSinCosTest {
    public static void main(final String... args) {
        ClojureFunctionalExpressionTest.test(args, ClojureFunctionalSinCosTest.class, MultiSinCosTests::new);
    }
}
