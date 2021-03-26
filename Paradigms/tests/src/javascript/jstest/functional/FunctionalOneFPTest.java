package jstest.functional;

import jstest.Language;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalOneFPTest extends FunctionalOneTwoTest {
    public static final Dialect FP_FUNCTIONS = ARITHMETIC_FUNCTIONS.copy()
            .rename("*+", "madd")
            .rename("_", "floor")
            .rename("^", "ceil");

    public static class FPTests extends OneTwoTests {{
        any("*+", 3, FunctionalOneFPTest::madd);
        any("madd", 3, FunctionalOneFPTest::madd);
        unary("_", Math::floor);
        unary("floor", Math::floor);
        unary("^", Math::ceil);
        unary("ceil", Math::ceil);

        tests(
                f("*+", f("-", vx, vy), vz, one),
                f("madd", f("-", vx, vy), vz, two),
                f("_", f("/", vx, vy)),
                f("floor", f("/", vx, vy)),
                f("^", f("-", vx, f("/", vy, c(3)))),
                f("ceil", f("-", vx, f("/", vy, c(3))))
        );
    }}

    private static double madd(final List<Double> args) {
        return args.get(0) * args.get(1) + args.get(2);
    }

    protected FunctionalOneFPTest(final Language language, final boolean testParsing) {
        super(language, testParsing);
    }

    public static void main(final String... args) {
        test(args, FunctionalOneFPTest.class, FunctionalOneFPTest::new, new FPTests(), FP_FUNCTIONS);
    }
}
