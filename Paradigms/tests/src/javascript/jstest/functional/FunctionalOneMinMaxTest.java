package jstest.functional;

import jstest.Language;

import java.util.Comparator;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalOneMinMaxTest extends FunctionalOneTwoTest {
    public static class MinMaxTests extends OneTwoTests {{
        any("min5", 5, args -> args.stream().min(Comparator.naturalOrder()).orElse(0.0));
        any("max3", 3, args -> args.stream().max(Comparator.naturalOrder()).orElse(0.0));
        tests(
                f("min5", vx, vy, vz, one, two),
                f("max3", vx, vy, vz)
        );
    }}

    protected FunctionalOneMinMaxTest(final Language language, final boolean testParsing) {
        super(language, testParsing);
    }

    public static void main(final String... args) {
        test(args, FunctionalOneMinMaxTest.class, FunctionalOneMinMaxTest::new, new MinMaxTests());
    }
}
