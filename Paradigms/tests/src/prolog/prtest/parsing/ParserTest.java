package prtest.parsing;

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
            t.variable(first + t.randomString("xyzXYZ"), index);
        };
        for (int i = 0; i < 10; i++) {
            var.accept('x', 0);
            var.accept('y', 1);
            var.accept('z', 2);
        }
    };
    private static final Selector SELECTOR = ParserTester.SELECTOR.copy()
            .add("Base", ARITH)
            .add("SinCos",                 ARITH, SIN,         COS)
            .add("VarSinhCosh", VARIABLES, ARITH, SINH,        COSH)
            .add("VarBoolean",  VARIABLES, ARITH, INFIX_AND, INFIX_OR,INFIX_XOR)
            ;

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
