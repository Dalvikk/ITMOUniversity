package cljtest.parsing;

import jstest.expression.AbstractTests;
import jstest.expression.Operation;
import jstest.expression.Selector;

import java.util.function.BiConsumer;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ParserTest {
    private static final Operation VARIABLES = checker -> {
        final AbstractTests t = checker.getTests();
        final BiConsumer<Character, Integer> var = (first, index) -> {
            final char prefix = t.randomBoolean() ? first : Character.toUpperCase(first);
            t.variable(prefix + t.randomString("xyzXYZ"), index);
        };
        for (int i = 0; i < 10; i++) {
            var.accept('x', 0);
            var.accept('y', 1);
            var.accept('z', 2);
        }
    };
    private static final Selector SELECTOR = ParserTester.SELECTOR.copy()
            .add("Base",                       ARITH)
            .add("Variables",       VARIABLES, ARITH)
            .add("Boolean",         VARIABLES, ARITH, INFIX_AND, INFIX_OR, INFIX_XOR)
            .add("PowLog",          VARIABLES, ARITH, INFIX_POW, INFIX_LOG)
            .add("ImplIff",         VARIABLES, ARITH, INFIX_AND, INFIX_OR, INFIX_XOR, INFIX_IMPL, INFIX_IFF)
            ;

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
