package jstest.prefix;

import jstest.expression.Selector;

import static jstest.expression.Operations.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PrefixTest {
    private static final Selector SELECTOR = Kind.selector(
            "prefix", "parsePrefix", ParserTester.PREFIX,
            "Empty input", "",
            "Unknown variable", "a",
            "Invalid number", "-a",
            "Missing )", "(* z (+ x y)",
            "Unknown operation", "(@@  x y)",
            "Excessive info", "(+ x y) x",
            "Empty op", "()",
            "Invalid unary (0 args)", "(negate)",
            "Invalid unary (2 args)", "(negate x y)",
            "Invalid binary (0 args)", "(+)",
            "Invalid binary (1 args)", "(+ x)",
            "Invalid binary (3 args)", "(+ x y z)",
            "Variable op (0 args)", "(x)",
            "Variable op (1 args)", "(x 1)",
            "Variable op (2 args)", "(x 1 2)",
            "Const op (0 args)", "(0)",
            "Const op (1 args)", "(0 1)",
            "Const op (2 args)", "(0 1 2)")
            .copy()
            .add("Means", ARITH_MEAN, GEOM_MEAN, HARM_MEAN)
            .add("SinhCosh", SINH, COSH)
            ;

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
