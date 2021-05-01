package cljtest.object;

import cljtest.ClojureScript;
import cljtest.functional.ClojureFunctionalExpressionTest;
import cljtest.multi.MultiTests;
import jstest.Engine;
import jstest.Language;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureObjectExpressionTest extends ClojureFunctionalExpressionTest {
    public static final Dialect PARSED = dialect(
            "(Variable \"%s\")",
            "(Constant %s.0)",
            (op, args) -> "(" + op + " " + String.join(" ", args) + ")"
    )
            .rename("+", "Add")
            .rename("-", "Subtract")
            .rename("*", "Multiply")
            .rename("/", "Divide")
            .rename("negate", "Negate");

    public static final double D = 1e-4;
    private static final ClojureScript.F<String> TO_STRING = ClojureScript.function("toString", String.class);
    private final boolean testDiff;

    protected ClojureObjectExpressionTest(final Language language, final boolean testDiff) {
        super(language, Optional.of("evaluate"));
        this.testDiff = testDiff;
    }

    @Override
    protected String parse(final String expression) {
        return "(parseObject \"" + expression + "\")";
    }

    @Override
    protected void test(final String parsed, final String unparsed) {
        testToString(parsed, unparsed);

        testToString(addSpaces(parsed, random), unparsed);
    }

    protected void testToString(final String expression, final String expected) {
        engine.parse(expression);
        final Engine.Result<String> result = engine.toString(TO_STRING);
        assertEquals(result.context, expected, result.value);
    }

    @Override
    protected void test() {
        super.test();

        if (testDiff) {
            for (final Expr<TExpr> test : language.tests) {
                testDiff(test, test.parsed);
                testDiff(test, parse(test.unparsed));
            }
        }
    }

    private void testDiff(final Expr<TExpr> test, final String expression) {
        for (int variable = 0; variable < 3; variable++) {
            final String value = "(diff " + expression + " \"" + "xyz".charAt(variable) + "\")";
            System.out.println("Testing: " + value);
            engine.parse(value);
            final double di = variable == 0 ? D : 0;
            final double dj = variable == 1 ? D : 0;
            final double dk = variable == 2 ? D : 0;
            for (int i = 1; i <= N; i += 1) {
                for (int j = 1; j <= N; j += 1) {
                    for (int k = 1; k <= N; k += 1) {
                        if (Double.isFinite(test.answer.evaluate(i, j, k))) {
                            final double expected = (test.answer.evaluate(i + di, j + dj, k + dk) - test.answer.evaluate(i - di, j - dj, k - dk)) / D / 2;
                            evaluate(new double[]{i, j, k}, expected, 1e-4);
                        }
                    }
                }
            }
        }
    }

    protected static void test(final String[] args, final Function<Boolean, MultiTests> multi, final Class<?> test, final Dialect parsed) {
        final boolean hard = mode(args, test);
        new ClojureObjectExpressionTest(new Language(parsed, UNPARSED, multi.apply(hard)), hard).run(test);
    }

    public static void main(final String... args) {
        test(args, MultiTests::new, ClojureObjectExpressionTest.class, PARSED);
    }
}
