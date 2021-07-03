package jstest.functional;

import jstest.expression.AbstractTests;
import jstest.JSTester;
import jstest.expression.BaseTester;
import jstest.expression.Dialect;
import jstest.expression.Language;

import java.nio.file.Path;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ExpressionTest {
    public static final Dialect ARITHMETIC = new Dialect("variable('%s')", "cnst(%s)", "{op}({args})", ", ")
                    .renamed("+", "add", "-", "subtract", "/", "divide", "*", "multiply");

    public static final Dialect POLISH = new Dialect("%s", "%s", "{args} {op}", " ");
    private static final Path SCRIPT = Path.of("functionalExpression.js");

    static JSTester tester(final Language language, final boolean testParsing) {
        return tester(language, testParsing, SCRIPT);
    }

    private static JSTester tester(final Language language, final boolean testParsing, final Path script) {
        return new JSTester(language, testParsing, script, "", "toString", "parse");
    }

    static void test(final String[] args, final Class<?> type, final Path script, final AbstractTests tests) {
        final int mode = BaseTester.mode(args, type, "easy", "hard");
        tester(new Language(ARITHMETIC, POLISH, tests), mode == 1, script).run(type, "mode=" + args[0]);
    }

    public static void main(final String... args) {
        test(args, ExpressionTest.class, SCRIPT, new AbstractTests() {{
            final TestExpression vx = variable("x", 0);

            //noinspection Convert2MethodRef
            binary("+", (a, b) -> a + b);
            binary("-", (a, b) -> a - b);
            binary("*", (a, b) -> a * b);
            binary("/", (a, b) -> a / b);

            tests(
                    c(10),
                    vx,
                    f("+", vx, c(2)),
                    f("-", c(3), vx),
                    f("*", c(4), vx),
                    f("/", c(5), vx),
                    f("/", vx, f("*", f("+", vx, c(1)), vx)),
                    f("+", f("+", f("*", vx, vx), f("*", vx, vx)), f("*", vx, vx)),
                    f("-", f("+", f("*", vx, vx), f("*", c(5), f("*", vx, f("*", vx, vx)))), f("*", vx, c(8)))
            );
        }});
    }
}
