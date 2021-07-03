package jstest.expression;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class Selector {
    private static final StackWalker WALKER = StackWalker.getInstance(Set.of(StackWalker.Option.RETAIN_CLASS_REFERENCE), 1);

    private final Class<?> owner;
    private final List<String> modes;
    private final BiFunction<List<Consumer<Builder>>, Integer, BaseTester<?, ?>> tester;
    private final Map<String, List<Consumer<Builder>>> variants;

    private Selector(final Class<?> owner, final List<String> modes, final BiFunction<List<Consumer<Builder>>, Integer, BaseTester<?, ?>> tester, final Map<String, List<Consumer<Builder>>> variants) {
        this.owner = owner;
        this.modes = modes;
        this.tester = tester;
        this.variants = variants;
    }

    public Selector(final List<String> modes, final BiFunction<List<Consumer<Builder>>, Integer, BaseTester<?, ?>> tester) {
        this(WALKER.getCallerClass(), modes, tester, new LinkedHashMap<>());
    }

    public Selector copy() {
        return new Selector(WALKER.getCallerClass(), modes, tester, new LinkedHashMap<>(variants));
    }

    public void test(final String[] args) {
        check(args.length == 2, "Expected two arguments, found %s", args.length);
        final String var = args[0];
        final String mod = args[1];

        final List<Consumer<Builder>> variant = variants.get(var);
        check(variant != null, "Unknown variant '%s'", var);

        final int mode = modes.indexOf(mod);
        check(mode >= 0, "Unknown mode '%s'", mod);

        tester.apply(variant, mode).run(owner, "variant=" + var + ", mode=" + mod);
    }

    private void check(final boolean condition, final String format, final Object... args) {
        if (!condition) {
            System.err.println("ERROR: " + String.format(format, args));
            System.err.printf("Usage: %s VARIANT MODE%n", owner.getName());
            System.err.println("Variants: " + String.join(", ", variants.keySet()));
            System.err.println("Modes: " + modes.stream().filter(Predicate.not(String::isEmpty)).collect(Collectors.joining(", ")));
            System.exit(1);
        }
    }

    public final Selector add(final String name, final Operation... operations) {
        variants.put(name, List.of(operations));
        return this;
    }
}
