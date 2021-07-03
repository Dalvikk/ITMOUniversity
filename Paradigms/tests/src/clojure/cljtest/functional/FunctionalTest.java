package cljtest.functional;

import jstest.expression.Selector;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalTest {
    private static final Selector SELECTOR = FunctionalTester.SELECTOR.copy()
            .add("Base",            ARITH)
            .add("SinCos",          ARITH, SIN,         COS)
            .add("SinhCosh",        ARITH, SINH,        COSH)
            .add("SumAvg",          ARITH, SUM,         AVG)
            .add("MeanVarn",        ARITH, MEAN,        VARN)
            ;

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
