package jstest.prefix;

import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PostfixSumsqLengthTest extends PrefixSumsqLengthTest {
    protected PostfixSumsqLengthTest(final int mode) {
        super(mode, new Language(SUMSQ_LENGTH_DIALECT, PostfixMixin.DIALECT, new SumsqLengthTests()), "postfix");
    }

    @Override
    protected void testParsing() {
        PostfixMixin.testErrors(this);
    }

    @Override
    protected String parse(final String expression) {
        return "parsePostfix('" + expression + "')";
    }

    public static void main(final String... args) {
        new PostfixSumsqLengthTest(prefixMode(args, PostfixSumsqLengthTest.class)).run();
    }
}
