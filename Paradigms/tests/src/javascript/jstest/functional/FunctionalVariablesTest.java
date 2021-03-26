package jstest.functional;

import jstest.ArithmeticTests;
import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalVariablesTest extends FunctionalExpressionTest {
    public static class VariablesTests extends ArithmeticTests {
    }

    protected FunctionalVariablesTest(final Language language, final boolean testParsing) {
        super(language, testParsing);
    }

    public static void main(final String... args) {
        test(args, FunctionalVariablesTest.class, FunctionalVariablesTest::new, new VariablesTests());
    }
}
