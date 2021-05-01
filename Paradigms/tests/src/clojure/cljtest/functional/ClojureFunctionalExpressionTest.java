package cljtest.functional;

import cljtest.ClojureEngine;
import cljtest.multi.MultiTests;
import jstest.BaseJavascriptTest;
import jstest.Language;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ClojureFunctionalExpressionTest extends BaseJavascriptTest<ClojureEngine> {
    public static final Dialect PARSED = dialect(
            "(variable \"%s\")",
            "(constant %s.0)",
            (op, args) -> "(" + op + " " + String.join(" ", args) + ")"
    )
            .rename("+", "add")
            .rename("-", "subtract")
            .rename("*", "multiply")
            .rename("/", "divide");

    public static final Dialect UNPARSED = dialect(
            "%s",
            "%s.0",
            (op, args) -> "("+ op + " " + String.join(" ", args) + ")"
    );

    protected ClojureFunctionalExpressionTest(final Language language, final Optional<String> evaluate) {
        super(new ClojureEngine("expression.clj", evaluate), language, true);
    }

    @Override
    protected String parse(final String expression) {
        return "(parseFunction \"" + expression + "\")";
    }

    static void test(final String[] args, final Class<?> test, final Function<Boolean, ? extends MultiTests> multi) {
        final Language language = new Language(PARSED, UNPARSED, multi.apply(mode(args, test)));
        new ClojureFunctionalExpressionTest(language, Optional.empty()).run(test);
    }

    public static void main(final String... args) {
        test(args, ClojureFunctionalExpressionTest.class, MultiTests::new);
    }

    protected static boolean mode(final String[] args, final Class<?> type) {
        return mode(args, type, "easy", "hard") == 1;
    }
}
