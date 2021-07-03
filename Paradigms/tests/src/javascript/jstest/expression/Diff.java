package jstest.expression;

import jstest.Engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static jstest.expression.BaseTester.*;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Diff {
    public static final double D = 1e-6;

    private final Dialect dialect;
    private final int min;
    private final int max;
    public static final List<String> VARIABLES = List.of("x", "y", "z");

    public Diff(final int min, final int max, final Dialect dialect) {
        this.dialect = dialect;
        this.min = min;
        this.max = max;
    }

    public <X> void diff(final BaseTester<X, ?> tester) {
        tester.addStage(() -> {
            for (final Expr expr : tester.language.getTests()) {
                diff(tester, expr, false);
            }
        });
    }

    private <X> List<Engine.Result<String>> diff(final BaseTester<X, ?> tester, final Expr expr, final boolean simplify) {
        final List<Engine.Result<String>> results = new ArrayList<>(3);
        for (int variable = 0; variable < 3; variable++) {
            final String diff = dialect.operation("diff", List.of(expr.parsed, dialect.variable(VARIABLES.get(variable))));
            final String value = simplify ? dialect.operation("simplify", List.of(diff)) : diff;
            System.out.println("Testing: " + value);
            final Engine.Result<X> expression = tester.engine.prepare(value);

            results.add(tester.engine.toString(expression));

            final double di = variable == 0 ? D : 0;
            final double dj = variable == 1 ? D : 0;
            final double dk = variable == 2 ? D : 0;
            for (int i = min; i <= max; i++) {
                for (int j = min; j <= max; j++) {
                    for (int k = min; k <= max; k++) {
                        final double d = Math.abs(expr.answer.applyAsDouble(i, j, k));
                        if (EPS < d && d < 1 / EPS) {
                            final double expected = (
                                    expr.answer.applyAsDouble(i + di, j + dj, k + dk) -
                                            expr.answer.applyAsDouble(i - di, j - dj, k - dk)) / D / 2;
                            if (Math.abs(expected) < EPS) {
                                try {
                                    tester.evaluate(expression, new double[]{i, j, k}, expected);
                                } catch (final AssertionError e) {
                                    System.err.format("d = %f%n", d);
                                    throw e;
                                }
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public <X> void simplify(final BaseTester<X, ?> tester) {
        tester.addStage(() -> {
            final List<int[]> newSimplifications = new ArrayList<>();
            final List<int[]> simplifications = tester.language.getSimplifications();
            final List<Expr> tests = tester.language.getTests();

            for (int i = 0; i < simplifications.size(); i++) {
                final Expr expr = tests.get(i);
                final int[] expected = simplifications.get(i);
                final List<Engine.Result<String>> actual = diff(tester, expr, true);
                if (expected != null) {
                    for (int j = 0; j < expected.length; j++) {
                        final Engine.Result<String> result = actual.get(j);
                        final int length = result.value.length();
                        assertTrue(
                                String.format("Simplified length too long: %d instead of %d%s", length, expected[j], result.context),
                                length <= expected[j]
                        );
                    }
                } else {
                    newSimplifications.add(actual.stream().mapToInt(result -> result.value.length()).toArray());
                }
            }
            if (!newSimplifications.isEmpty()) {
                System.err.println(newSimplifications.stream()
                        .map(row -> Arrays.stream(row).mapToObj(Integer::toString).collect(Collectors.joining(", ", "{", "}")))
                        .collect(Collectors.joining(", ", "new int[][]{", "}")));
                throw new AssertionError("Uncovered");
            }
        });
    }
}
