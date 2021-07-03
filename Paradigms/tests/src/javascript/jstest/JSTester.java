package jstest;

import jstest.expression.BaseTester;
import jstest.expression.Language;

import java.nio.file.Path;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class JSTester extends BaseTester<Object, JSEngine> {
    public JSTester(
            final Language language, final boolean testParsing,
            final Path script, final String evaluate,
            final String toString, final String parse
    ) {
        super(new JSEngine(script, evaluate, parse, toString), language, testParsing);
    }
}
