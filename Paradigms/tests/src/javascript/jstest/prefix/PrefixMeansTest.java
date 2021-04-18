package jstest.prefix;

import jstest.ArithmeticTests;
import jstest.Language;
import jstest.object.ObjectExpressionTest;

import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.DoubleStream;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixMeansTest extends PrefixParsingErrorTest {
    private static final Random RANDOM = new Random(343243543059325L);
    public static final Dialect MEANS_DIALECT = ObjectExpressionTest.ARITHMETIC_DIALECT.copy()
            .rename("arith-mean", "ArithMean")
            .rename("geom-mean", "GeomMean")
            .rename("harm-mean", "HarmMean");

    public static class MeansTests extends ArithmeticTests {
        public MeansTests() {
            any("arith-mean", 1, 5, mean((args, n) -> args.sum() / n));
            any("geom-mean", 1, 5, mean((args, n) -> Math.pow(Math.abs(product(args)), 1 / n)));
            any("harm-mean", 1, 5, mean((args, n) -> n / args.map(a -> 1 / a).sum()));

            tests("arith-mean");
            tests("geom-mean");
            tests("harm-mean");

            final Supplier<AbstractExpression> generator = () -> random(vx, vy, vz, c(RANDOM.nextInt(10)));
            for (int i = 1; i < 10; i++) {
                final String[] means = Stream.generate(() -> random("arith-mean", "geom-mean", "harm-mean"))
                        .limit(3).toArray(String[]::new);
                final AbstractExpression[] args = Stream.generate(generator).limit(i).toArray(AbstractExpression[]::new);
                tests(
                        f("arith-mean", args),
                        f("geom-mean", args),
                        f("harm-mean", args),
                        f(means[0], f(means[1], vx, vy), f(means[2], vy, vz))
                );
            }
        }

        private void tests(final String mean) {
            tests(
                    f(mean, vx),
                    f(mean, vx, vy),
                    f(mean, vx, vy, vz),
                    f(mean, vx, vy, vz, c(3), c(5)),
                    f(mean, f("+", vx, c(2))),
                    f(mean, f("+", vx, vy)),
                    f(mean, f("negate", vz), f("/", vx, vy)),
                    f(mean, f("negate", vz), f(mean, vx, vy))
            );
        }
    }

    private static double product(final DoubleStream args) {
        return args.reduce(1, (a, b) -> a * b);
    }

    private static Function<List<Double>, Double> mean(final BiFunction<DoubleStream, Double, Double> f) {
        return args -> f.apply(args.stream().mapToDouble(a -> a), (double) args.size());
    }

    @SafeVarargs
    private static <T> T random(final T... values) {
        return values[RANDOM.nextInt(values.length)];
    }

    protected PrefixMeansTest(final int mode, final Language language, final String toString) {
        super(mode, language, toString);
        insertions = "()+*/@ABC";
    }

    public static void main(final String... args) {
        test(PrefixMeansTest.class, PrefixMeansTest::new, new MeansTests(), args, MEANS_DIALECT, "prefix");
    }
}
