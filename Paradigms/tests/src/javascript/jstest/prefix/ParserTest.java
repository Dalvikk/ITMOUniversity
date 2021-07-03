package jstest.prefix;

import jstest.ArithmeticTests;
import jstest.object.ObjectTester;
import jstest.expression.Language;
import jstest.expression.Selector;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ParserTest {
    private static final Selector SELECTOR = new Selector(List.of("", "easy", "hard"), (variant, mode) -> new ParserTester(
            mode,
            new Language(ObjectTester.OBJECT, ParserTester.PREFIX, new ArithmeticTests()),
            "prefix",
            "parsePrefix",
            "xyz()+*/@ABC"
    )).add("Base");

    public static void main(final String... args) {
        SELECTOR.test(args);
    }
}
