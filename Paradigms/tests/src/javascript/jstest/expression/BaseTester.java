package jstest.expression;

import expression.BaseTest;
import jstest.Engine;
import jstest.JSEngine;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * @author Niyaz Nigmatullin
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class BaseTester<X, E extends Engine<X>> extends BaseTest {
    public static final int N = 5;
    public static final double EPS = 1e-3;
    public static int TESTS = 444;

    protected final E engine;
    /*package*/ final Language language;
    private final List<Runnable> stages = new ArrayList<>();

    final boolean testParsing;

    protected BaseTester(final E engine, final Language language, final boolean testParsing) {
        this.engine = engine;
        this.language = language;
        this.testParsing = testParsing;
    }

    private static boolean safe(final char ch) {
        return !Character.isLetterOrDigit(ch) && "+-*/.<>=&|^".indexOf(ch) == -1;
    }

    public static String addSpaces(final String expression, final Random random) {
        String spaced = expression;
        for (int n = StrictMath.min(10, 200 / expression.length()); n > 0;) {
            final int index = random.nextInt(spaced.length() + 1);
            final char c = index == 0 ? 0 : spaced.charAt(index - 1);
            final char nc = index == spaced.length() ? 0 : spaced.charAt(index);
            if ((safe(c) || safe(nc)) && c != '\'' && nc != '\'' && c != '"' && nc != '"') {
                spaced = spaced.substring(0, index) + " " + spaced.substring(index);
                n--;
            }
        }
        return spaced;
    }

    @Override
    protected void test() {
        for (final Expr test : language.getTests()) {
            test(engine.prepare(test.parsed), test.answer, test.unparsed);
            if (testParsing) {
                test(parse(test.unparsed), test.answer, test.unparsed);
                test(parse(addSpaces(test.unparsed, random)), test.answer, test.unparsed);
            }
        }

        testRandom(TESTS);
        stages.forEach(Runnable::run);
    }

    public Engine.Result<X> parse(final String expression) {
        return engine.parse(expression);
    }

    protected void test(final Engine.Result<X> prepared, final Func f, final String unparsed) {
        System.out.println("Testing: " + prepared);

        for (double i = 0; i <= N; i++) {
            for (double j = 0; j <= N; j++) {
                for (double k = 0; k <= N; k++) {
                    final double[] vars = new double[]{i, j, k};
                    evaluate(prepared, vars, f.applyAsDouble(vars));
                }
            }
        }

        test(prepared, unparsed);
    }

    protected void test(final Engine.Result<X> prepared, final String unparsed) {
    }

    public void testRandom(final int n) {
        System.out.println("Testing random tests");
        for (int i = 0; i < n; i++) {
            if (i % 100 == 0) {
                System.out.printf("    Completed %3d out of %d%n", i, n);
            }
            final double[] vars = random.doubles().limit(language.getVariables().size()).toArray();

            final Expr test = language.randomTest(i);
            final double answer = test.answer.applyAsDouble(vars);

            final Engine.Result<X> prepared = engine.prepare(test.parsed);
            test(prepared, vars, test, answer);
            if (testParsing) {
                counter.nextTest();
                test(parse(test.unparsed), vars, test, answer);
                test(parse(addSpaces(test.unparsed, random)), vars, test, answer);
                counter.passed();
            }
        }
    }

    public void test(final Engine.Result<X> prepared, final double[] vars, final Expr test, final double answer) {
        evaluate(prepared, vars, answer);
        test(prepared, test.unparsed);
    }

    public void evaluate(final Engine.Result<X> prepared, final double[] vars, final double expected) {
        counter.nextTest();
        final Engine.Result<Number> result = engine.evaluate(prepared, vars);
        assertEquals(result.context, EPS, expected, result.value.doubleValue());
        counter.passed();
    }

    public static int mode(final String[] args, final Class<?> type, final String... modes) {
        if (args.length == 0) {
            System.err.println("ERROR: No arguments found");
        } else if (args.length > 1) {
            System.err.println("ERROR: Only one argument expected, " + args.length + " found");
        } else if (!Arrays.asList(modes).contains(args[0])) {
            System.err.println("ERROR: First argument should be one of: \"" + String.join("\", \"", modes) + "\", found: \"" + args[0] + "\"");
        } else {
            return Arrays.asList(modes).indexOf(args[0]);
        }
        System.err.println("Usage: java -ea " + JSEngine.OPTIONS + " " + type.getName() + " {" + String.join("|", modes) + "}");
        System.exit(0);
        return -1;
    }

    public void addStage(final Runnable stage) {
        stages.add(stage);
    }

    public interface Func extends ToDoubleFunction<double[]> {
        @Override
        double applyAsDouble(double... args);
    }

    public static class Expr {
        public final String parsed;
        public final String unparsed;
        public final Func answer;

        public Expr(final String parsed, final String unparsed, final Func answer) {
            this.parsed = Objects.requireNonNull(parsed);
            this.unparsed = Objects.requireNonNull(unparsed);
            this.answer = answer;
        }
    }
}
