package jstest.functional;

import jstest.expression.AbstractTests;

import java.nio.file.Path;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class MiniTest {
    public static void main(final String... args) {
        ExpressionTest.test(args, MiniTest.class, Path.of("functionalMiniExpression.js"), new AbstractTests() {{
            tests(c(10), variable("x", 0));
        }});
    }
}
