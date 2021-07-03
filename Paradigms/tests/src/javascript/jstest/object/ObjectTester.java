package jstest.object;

import jstest.Engine;
import jstest.JSTester;
import jstest.expression.*;
import jstest.functional.ExpressionTest;

import java.nio.file.Path;
import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectTester extends JSTester {
    public static final Dialect OBJECT = new Dialect("new Variable('%s')", "new Const(%s)", "new {op}({args})", ", ")
            .renamed("+", "Add", "-", "Subtract", "*", "Multiply", "/", "Divide", "negate", "Negate");

    static final Selector SELECTOR = new Selector(List.of("easy", "", "hard", "bonus"), (variant, mode) -> {
        final Builder builder = new Builder(false, variant);
        final Language language = builder.dialect(OBJECT, ExpressionTest.POLISH);
        return new ObjectTester(mode, language, "toString", "parse");
    });

    private static final Diff DIFF = new Diff(2, N, new Dialect(
            "'%s'", "%s",
            (name, args) -> String.format("%s.%s(%s)", args.get(0), name, String.join(", ", args.subList(1, args.size())))
    ));

    protected ObjectTester(final int mode, final Language language, final String toString, final String parse) {
        super(language, mode >= 1, Path.of("objectExpression.js"), ".evaluate", toString, parse);

        if (mode >= 2) {
            DIFF.diff(this);
        }
        if (mode >= 3) {
            DIFF.simplify(this);
        }
    }

    @Override
    protected void test(final Engine.Result<Object> prepared, final String unparsed) {
        counter.nextTest();
        engine.toString(prepared).assertEquals(unparsed);
        counter.passed();
    }

}
