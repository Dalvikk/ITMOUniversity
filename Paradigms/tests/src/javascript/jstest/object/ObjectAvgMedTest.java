package jstest.object;

import jstest.ArithmeticTests;
import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectAvgMedTest extends ObjectExpressionTest {
    public static final Dialect AVG_MED_DIALECT = ObjectExpressionTest.ARITHMETIC_DIALECT.copy()
            .rename("med3", "Med3")
            .rename("avg5", "Avg5");

    public static class AvgMedTests extends ArithmeticTests {{
        any("avg5", 5, args -> args.stream().mapToDouble(a -> a).summaryStatistics().getAverage());
        any("med3", 3, args -> args.stream().mapToDouble(a -> a).sorted().skip(1).findFirst().orElse(0));
        tests(
                f("med3", vx, vy, vz),
                f("avg5", vx, vy, vz, c(7), f("*", vy, vz)),
                f("med3", vx, f("-", vy, vz), c(7)),
                f("/",
                        f("/",
                                f("-", vz, vz),
                                f("+",
                                        vx,
                                        f("-", vz, c(1777340624))
                                )
                        ),
                        f("-", vy, vy))
        );
    }}

    protected ObjectAvgMedTest(final int mode, final Language language) {
        super(mode, language);
    }

    public static void main(final String... args) {
        test(ObjectAvgMedTest.class, ObjectAvgMedTest::new, new AvgMedTests(), args, AVG_MED_DIALECT);
    }
}
