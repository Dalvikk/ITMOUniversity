package cljtest.object;

import jstest.expression.Selector;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectTest {
    private static final Selector SELECTOR = ObjectTester.SELECTOR.copy()
            .add("Base",            ARITH)
            .add("SinCos",          ARITH, SIN,         COS)
            .add("SinhCosh",        ARITH, SINH,        COSH)
            .add("SumAvg",          ARITH, SUM,         AVG)
            .add("Means",           ARITH, ARITH_MEAN,  GEOM_MEAN, HARM_MEAN)
            ;

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
