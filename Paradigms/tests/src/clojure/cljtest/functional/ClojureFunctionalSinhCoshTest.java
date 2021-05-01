package cljtest.functional;

import cljtest.multi.MultiSinhCoshTests;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureFunctionalSinhCoshTest {
    public static void main(final String... args) {
        ClojureFunctionalExpressionTest.test(args, ClojureFunctionalSinhCoshTest.class, MultiSinhCoshTests::new);
    }
}
