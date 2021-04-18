package jstest.prefix;

import jstest.Language;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class PostfixMeansTest extends PrefixMeansTest {
    protected PostfixMeansTest(final int mode) {
        super(mode, new Language(MEANS_DIALECT, PostfixMixin.DIALECT, new MeansTests()), "postfix");
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
        new PostfixMeansTest(prefixMode(args, PostfixMeansTest.class)).run();
    }
}
