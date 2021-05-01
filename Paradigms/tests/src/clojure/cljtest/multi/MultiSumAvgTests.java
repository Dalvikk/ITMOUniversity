package cljtest.multi;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MultiSumAvgTests extends MultiTests {
    public MultiSumAvgTests(final boolean testMulti) {
        super(testMulti);
        final int arity = testMulti ? -1 : 2;
        any("sum", arity, xs -> xs.stream().mapToDouble(a -> a).sum());
        any("avg", arity, xs -> xs.stream().mapToDouble(a -> a).average().getAsDouble());
        tests(
                f("sum", vx, vy),
                f("avg", vx, vy),
                f("sum", vx, c(3)),
                f("avg", vx, c(3)),
                f("avg", vx, f("sum", vy, vz)),
                f("sum", vx, f("avg", vy, vz)),
                f("/", vz, f("avg", vx, vy)),
                f("+", f("sum", f("+", vx, c(10)), f("*", vz, f("*", vy, c(0)))), c(2))
        );
        randomMulti(testMulti, "sum", "avg");
    }
}
