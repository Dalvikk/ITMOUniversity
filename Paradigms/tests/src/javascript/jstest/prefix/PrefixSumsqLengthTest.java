package jstest.prefix;

import jstest.ArithmeticTests;
import jstest.Language;
import jstest.object.ObjectExpressionTest;

import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixSumsqLengthTest extends PrefixParsingErrorTest {
    private static final Random RANDOM = new Random(343243543059325L);
    public static final Dialect SUMSQ_LENGTH_DIALECT = ObjectExpressionTest.ARITHMETIC_DIALECT.copy()
            .rename("sumsq", "Sumsq")
            .rename("length", "Length");

    public static class SumsqLengthTests extends ArithmeticTests {{
        any("sumsq", 0, 5, PrefixSumsqLengthTest::sumsq);
        any("length", 0, 5, args -> Math.sqrt(sumsq(args)));

        tests(
                f("sumsq", vx),
                f("sumsq", vx, vy),
                f("sumsq", vx, vy, vz),
                f("sumsq", vx, vy, vz, c(3), c(5)),
                f("sumsq"),
                f("sumsq", f("-", vx, vy)),
                f("sumsq", f("+", vx, vy)),
                f("sumsq", f("negate", vz), f("/", vx, vy)),
                f("sumsq", f("negate", vz), f("sumsq", vx, vy)),
                f("length", vx),
                f("length", vx, vy),
                f("length", vx, vy, vz),
                f("length", vx, vy, vz, c(3), c(5)),
                f("length"),
                f("length", f("negate", vz), f("length", vx, vy))
        );
        final Supplier<AbstractExpression> generator = () -> random(vx, vy, vz, c(RANDOM.nextInt(10)));
        for (int i = 1; i < 10; i++) {
            final AbstractExpression[] args = Stream.generate(generator).limit(i).toArray(AbstractExpression[]::new);
            tests(
                    f("sumsq", args),
                    f("length", args)
            );
        }
    }}

    private static double sumsq(final List<Double> args) {
        return args.stream().mapToDouble(a -> a * a).sum();
    }

    @SafeVarargs
    private static <T> T random(final T... values) {
        return values[RANDOM.nextInt(values.length)];
    }

    protected PrefixSumsqLengthTest(final int mode, final Language language, final String toString) {
        super(mode, language, toString);
        insertions = "()+*/@ABC";
    }

    public static void main(final String... args) {
        test(PrefixSumsqLengthTest.class, PrefixSumsqLengthTest::new, new SumsqLengthTests(), args, SUMSQ_LENGTH_DIALECT, "prefix");
    }
}
