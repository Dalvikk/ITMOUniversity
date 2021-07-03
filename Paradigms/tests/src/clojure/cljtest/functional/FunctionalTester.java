package cljtest.functional;

import base.Randomized;
import cljtest.ClojureEngine;
import jstest.Engine;
import jstest.expression.*;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class FunctionalTester extends BaseTester<Object, ClojureEngine> {
    public static final Dialect PARSED = new Dialect("(variable \"%s\")", "(constant %s.0)", "({op} {args})", " ")
            .renamed("+", "add", "-", "subtract", "*", "multiply", "/", "divide");
    public static final Dialect UNPARSED = new Dialect("%s", "%s.0", "({op} {args})", " ");
    static final Selector SELECTOR = new Selector(List.of("easy", "hard"), (operations, mode) -> {
        final Builder builder = new Builder(mode == 1, operations);
        final Language language = builder.language(PARSED, UNPARSED);
        return new FunctionalTester(language, Optional.empty(), "parseFunction", "", (a, b) -> b);
    });

    private final BiFunction<Randomized, String, String> spoiler;

    protected FunctionalTester(
            final Language language,
            final Optional<String> evaluate,
            final String parse,
            final String toString,
            final BiFunction<Randomized, String, String> spoiler
    ) {
        super(new ClojureEngine("expression.clj", evaluate, parse, toString), language, true);
        this.spoiler = spoiler;
    }

    @Override
    public Engine.Result<Object> parse(final String expression) {
        return super.parse(spoiler.apply(this, expression));
    }
}
