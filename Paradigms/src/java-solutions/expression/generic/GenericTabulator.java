package expression.generic;

import expression.GenericExpression;
import expression.calculators.*;
import expression.exceptions.EvaluateException;
import expression.exceptions.IllegalModeException;
import expression.exceptions.ParseException;
import expression.parser.ExpressionParser;

import java.util.Map;

public class GenericTabulator implements Tabulator {
    private static final Map<String, Calculator<?>> CALCULATOR_BY_MODE = Map.of(
            "i", new CheckedIntCalculator(),
            "d", new DoubleCalculator(),
            "bi", new BigIntCalculator(),
            "u", new IntCalculator(),
            "p", new ModCalculator(),
            "b", new ByteCalculator()
    );

    public static void main(final String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Program arguments: <mode> <expression>");
            return;
        }
        final Object[][][] ans = new GenericTabulator().tabulate(args[0].substring(1), args[1], -2, 2, -2, 2, -2, 2);
        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                for (int k = 0; k <= 4; k++) {
                    System.out.printf("x = %d, y = %d, z = %d, result = %s%n", i - 2, j - 2, k - 2, ans[i][j][k]);
                }
            }
        }
    }

    @Override
    public Object[][][] tabulate(final String mode, final String expression, final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) throws IllegalModeException {
        final Calculator<?> calculator = CALCULATOR_BY_MODE.get(mode);
        if (calculator == null) {
            throw new IllegalModeException("Illegal mode: " + mode);
        }
        return tabulate(calculator, expression, x1, x2, y1, y2, z1, z2);
    }

    public static <T> Object[][][] tabulate(final Calculator<T> calc, final String stringExpression, final int x1, final int x2, final int y1, final int y2, final int z1, final int z2) {
        final Object[][][] ans = new Object[x2 - x1 + 1][y2 - y1 + 1][z2 - z1 + 1];
        final GenericExpression expression;
        try {
            expression = new ExpressionParser().parse(stringExpression);
        } catch (final ParseException ignored) {
            return ans;
        }
        for (int i = x1; i <= x2; i++) {
            final T x = calc.parse(Integer.toString(i));
            for (int j = y1; j <= y2; j++) {
                final T y = calc.parse(Integer.toString(j));
                for (int k = z1; k <= z2; k++) {
                    final T z = calc.parse(Integer.toString(k));
                    try {
                        ans[i - x1][j - y1][k - z1] = expression.evaluate(x, y, z, calc);
                    } catch (final EvaluateException ignored) {
                    }
                }
            }
        }
        return ans;
    }
}