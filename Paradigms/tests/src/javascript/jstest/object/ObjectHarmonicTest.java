package jstest.object;

import jstest.ArithmeticTests;
import jstest.Language;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectHarmonicTest extends ObjectExpressionTest {
    public static final Dialect HYPOT_HMEAN_DIALECT = ObjectExpressionTest.ARITHMETIC_DIALECT.copy()
            .rename("hypot", "Hypot")
            .rename("hmean", "HMean");

    public static class HypotHmeanTests extends ArithmeticTests {{
        binary("hypot", (a, b) -> a * a + b * b);
        binary("hmean", (a, b) -> 2 / (1 / a + 1 / b));
        final AbstractExpression hypot = f(
                "hypot",
                c(2),
                f("+", c(1), f("*", c(2), f("-", vy, vz)))
        );
        final AbstractExpression hmean = f(
                "hmean",
                f("+", c(2), f("*", c(4), f("-", vx, vz))),
                f("+", c(1), f("*", c(2), f("-", vy, vz)))
        );
        tests(
                f("hypot", vx, vy),
                f("hmean", vx, vy),
                f("hypot", vx, f("-", vy, vz)),
                hypot,
                hmean,
                f("hypot", hypot, hmean),
                f("hmean", hypot, hypot),
                f("hmean", vx, f("hypot", vy, vy)),
                f("hypot", vx, f("hmean", vz, vz))
        );
    }}

    protected ObjectHarmonicTest(final int mode, final Language language) {
        super(mode, language);
        simplifications.addAll(List.of(new int[][]{
                {5, 5, 1},
                {21, 21, 1},
                {5, 9, 14},
                {1, 21, 22},
                {61, 61, 127},
                {101, 153, 220},
                {1, 213, 215},
                {37, 45, 1},
                {5, 1, 61}
        }));
    }

    public static void main(final String... args) {
        test(ObjectHarmonicTest.class, ObjectHarmonicTest::new, new HypotHmeanTests(), args, HYPOT_HMEAN_DIALECT);
    }
}
