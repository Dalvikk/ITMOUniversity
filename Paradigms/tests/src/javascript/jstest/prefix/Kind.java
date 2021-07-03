package jstest.prefix;

import jstest.object.ObjectTester;
import jstest.expression.Builder;
import jstest.expression.Dialect;
import jstest.expression.Language;
import jstest.expression.Selector;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Kind {
    public static Selector selector(final String toString, final String parse, final Dialect unparsed, final String... parsingTests) {
        assert parsingTests.length % 2 == 0;

        return new Selector(List.of("", "easy", "hard"), (operations, mode) -> {
            final Builder builder = new Builder(true, operations);
            final String insertions = builder.getTests().hasVarargs() ? "abc()+*/@ABC" : "xyz()+*/@ABC";
            final Language language = builder.dialect(ObjectTester.OBJECT, unparsed);
            final ParserTester tester = new ParserTester(mode, language, toString, parse, insertions);
            tester.addStage(() -> {
                for (int i = 0; i < parsingTests.length; i += 2) {
                    printParsingError(tester, parsingTests[i], parsingTests[i + 1]);
                }
            });
            return tester;
        });
    }

    private static void printParsingError(final ParserTester test, final String description, final String input) {
        final String message = test.assertParsingError(input, "", "");
        final int index = message.lastIndexOf("in <eval>");

        System.err.format("%-25s: %s%n", description, message.substring(0, index > 0 ? index : message.length()));
    }
}
