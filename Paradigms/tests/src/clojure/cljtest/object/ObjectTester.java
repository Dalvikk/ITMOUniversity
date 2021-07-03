package cljtest.object;

import base.Randomized;
import cljtest.functional.FunctionalTester;
import jstest.Engine;
import jstest.expression.*;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectTester extends FunctionalTester {
    public static final Dialect PARSED = new Dialect("(Variable \"%s\")", "(Constant %s.0)", "({op} {args})", " ");
    private static final Diff DIFF = new Diff(1, N, new Dialect("\"%s\"", "%s", "({op} {args})", " "));
    static final Selector SELECTOR = new Selector(List.of("easy", "hard"), (operations, mode) -> {
        final Builder builder = new Builder(mode == 1, operations);
        final Language language = builder.dialect(PARSED, UNPARSED);
        return new ObjectTester(language, true, "parseObject", "toString", (a, b) -> b);
    });

    public ObjectTester(
            final Language language,
            final boolean testDiff,
            final String parse, final String toString,
            final BiFunction<Randomized, String, String> spoiler
    ) {
        super(language, Optional.of("evaluate"), parse, toString, spoiler);
        if (testDiff) {
            DIFF.diff(this);
        }
    }

    public static void test(final Language language, final boolean testDiff, final String parse, final String toString, final BiFunction<Randomized, String, String> spoiler, final Class<?> test) {
        new ObjectTester(language, testDiff, parse, toString, spoiler).run(test);
    }


    @Override
    protected void test(final Engine.Result<Object> prepared, final String unparsed) {
        counter.nextTest();
        engine.toString(prepared).assertEquals(unparsed);
        counter.passed();
    }
}
