package jstest.object;

import jstest.expression.Selector;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectTest {
    static final Selector SELECTOR = ObjectTester.SELECTOR.copy()
            .add("Base")
            .add("ArcTan", ATAN, ATAN2)
            .add("AvgMed", avg(5), med(3))
            .add("Cube", CUBE, CBRT)
            .add("Harmonic", HYPOT, HMEAN);

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
