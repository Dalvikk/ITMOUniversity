package cljtest.multi;

import java.util.List;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MultiMeanVarnTests extends MultiTests {
    public MultiMeanVarnTests(final boolean testMulti) {
        super(testMulti);
        final int arity = testMulti ? -1 : 2;
        any("mean", arity, args -> mean(args.stream()));
        any("varn", arity, MultiMeanVarnTests::varn);
        tests(
                f("mean", vx, vy),
                f("varn", vx, vy),
                f("mean", vx, c(3)),
                f("varn", vx, c(3)),
                f("varn", vx, f("mean", vy, vz)),
                f("mean", vx, f("varn", vy, vz)),
                f("/", vz, f("varn", vx, vy)),
                f("+", f("mean", f("+", vx, c(10)), f("*", vz, f("*", vy, c(0)))), c(2))
        );

        randomMulti(testMulti, "mean", "varn");
    }

    private static double varn(final List<Double> args) {
        final double mean = mean(args.stream());
        return mean(args.stream().map(a -> a - mean).map(a -> a * a));
    }

    private static double mean(final Stream<Double> args) {
        //noinspection OptionalGetWithoutIsPresent
        return args.mapToDouble(Double::doubleValue).average().getAsDouble();
    }
}
