package jstest.expression;

import java.util.Arrays;
import java.util.OptionalDouble;
import java.util.function.*;
import java.util.stream.DoubleStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Operations {
    Operation ONE = constant("one", 1);
    Operation TWO = constant("two", 2);
    Operation MADD = fixed("madd", "*+", 3, args -> args[0] * args[1] + args[2], null);
    Operation FLOOR = unary("floor", "_", Math::floor, null);
    Operation CEIL = unary("ceil", "^", Math::ceil, null);

    static Operation min(final int arity) {
        return fix("min", "Min", arity, DoubleStream::min);
    }

    static Operation max(final int arity) {
        return fix("max", "Max", arity, DoubleStream::max);
    }

    Operation ATAN = unary("atan", "ArcTan", Math::atan,
            new int[][]{{1, 1, 1}, {13, 1, 1}, {21, 26, 1}, {21, 21, 1}, {71, 71, 67}, {59, 22, 22}});
    Operation ATAN2 = binary("atan2", "ArcTan2", Math::atan2,
            new int[][]{{1, 1, 1}, {1, 17, 1}, {16, 1, 1}, {23, 30, 1}, {48, 48, 43}, {50, 46, 41}, {78, 85, 51}, {71, 78, 58}});

    static Operation avg(final int arity) {
        return fix("avg", "Avg", arity, DoubleStream::average);
    }

    static Operation med(final int arity) {
        return fix("med", "Med", arity, args -> {
            final double[] sorted = args.sorted().toArray();
            return OptionalDouble.of(sorted[sorted.length / 2]);
        });
    }

    Operation CUBE = unary("cube", "Cube", a -> a * a * a,
            new int[][]{{1, 1, 1}, {9, 1, 1}, {17, 22, 1}, {17, 17, 1}, {67, 67, 59}, {51, 6, 6}});
    Operation CBRT = unary("cbrt", "Cbrt", Math::cbrt,
            new int[][]{{1, 1, 1}, {36, 1, 1}, {44, 49, 1}, {44, 44, 1}, {94, 94, 113}, {105, 22, 22}});
    
    Operation HYPOT = binary("hypot", "Hypot", (a, b) -> a * a + b * b,
            new int[][]{{1, 1, 1}, {1, 5, 1}, {5, 1, 1}, {5, 5, 1}, {9, 9, 17}, {17, 14, 9}, {21, 21, 17}, {21, 21, 17}});
    Operation HMEAN = binary("hmean", "HMean", (a, b) -> 2 / (1 / a + 1 / b),
            new int[][]{{1, 1, 1}, {1, 21, 1}, {21, 1, 1}, {21, 21, 1}, {39, 39, 41}, {41, 44, 39}, {67, 67, 49}, {67, 67, 49}});

    Operation SINH = unary("sinh", "Sinh", Math::sinh,
            new int[][]{{1, 1, 1}, {6, 1, 1}, {10, 15, 1}, {10, 10, 1}, {51, 51, 40}, {30, 21, 21}});
    Operation COSH = unary("cosh", "Cosh", Math::cosh,
            new int[][]{{1, 1, 1}, {6, 1, 1}, {10, 15, 1}, {10, 10, 1}, {51, 51, 40}, {30, 22, 22}});

    Operation ARITH_MEAN = any("arith-mean", "ArithMean", 1, 5, mean((args, n) -> args.sum() / n));
    Operation GEOM_MEAN = any("geom-mean", "GeomMean", 1, 5, mean((args, n) -> Math.pow(Math.abs(product(args)), 1 / n)));
    Operation HARM_MEAN = any("harm-mean", "HarmMean", 1, 5, mean((args, n) -> n / args.map(a -> 1 / a).sum()));

    private static BaseTester.Func mean(final ToDoubleBiFunction<DoubleStream, Double> f) {
        return args -> f.applyAsDouble(Arrays.stream(args), (double) args.length);
    }

    private static double product(final DoubleStream args) {
        return args.reduce(1, (a, b) -> a * b);
    }

    Operation SUMSQ = any("sumsq", "Sumsq", 0, 3, Operations::sumsq);
    Operation LENGTH = any("length", "Length", 0, 5, args -> Math.sqrt(sumsq(args)));

    private static double sumsq(final double[] args) {
        return Arrays.stream(args).map(a -> a * a).sum();
    }

    Operation ARITH = checker -> {
        checker.unary("negate", "Negate", a -> -a, null);

        checker.any("+", "Add", 0, 2, arith(0, Double::sum));
        checker.any("-", "Subtract", 1, 2, arith(0, (a, b) -> a - b));
        checker.any("*", "Multiply", 0, 2, arith(1, (a, b) -> a * b));
        checker.any("/", "Divide", 1, 2, arith(1, (a, b) -> a / b));
    };

    private static BaseTester.Func arith(final double zero, final DoubleBinaryOperator f) {
        return args -> args.length == 0 ? zero
                : args.length == 1 ? f.applyAsDouble(zero, args[0])
                : Arrays.stream(args).reduce(f).orElseThrow();
    }

    Operation SIN = unary("cos", "Cos", Math::cos, null);

    Operation COS = unary("sin", "Sin", Math::sin, null);


    Operation SUM = any("sum", "Sum", 0, 3, args -> Arrays.stream(args).sum());
    Operation AVG = avg(-2);

    Operation MEAN = any("mean", "Mean", 1, 5, Operations::mean);
    Operation VARN = any("varn", "Varn", 1, 5, Operations::var);

    private static double mean(final double[] args) {
        return Arrays.stream(args).sum() / args.length;
    }

    private static double var(final double[] args) {
        final double mean = mean(args);
        return Arrays.stream(args).map(a -> a - mean).map(a -> a * a).sum() / args.length;
    }

    Operation INFIX_POW = infix("**", "IPow", -300, Math::pow);
    Operation INFIX_LOG = infix("//", "ILog", -300, (a, b) -> Math.log(Math.abs(b)) / Math.log(Math.abs(a)));

    Operation INFIX_AND     = infix("&&",   "And",  90,  bool((a, b) -> a & b));
    Operation INFIX_OR      = infix("||",   "Or",   80,  bool((a, b) -> a | b));
    Operation INFIX_XOR     = infix("^^",   "Xor",  70,  bool((a, b) -> a ^ b));
    Operation INFIX_IMPL    = infix("->",   "Impl", -60, bool((a, b) -> ~a | b));
    Operation INFIX_IFF     = infix("<->",  "Iff",  50,  bool((a, b) -> ~(a ^ b)));

    private static DoubleBinaryOperator bool(final IntBinaryOperator op) {
        return (a, b) -> op.applyAsInt(a > 0 ? -1 : 0, b > 0 ? -1 : 0) == 0 ? 0 : 1;
    }

    private static Operation constant(final String name, final double value) {
        return checker -> checker.constant(name, value);
    }

    private static Operation unary(final String name, final String alias, final DoubleUnaryOperator op, final int[][] simplifications) {
        return checker -> checker.unary(name, alias, op, simplifications);
    }

    private static Operation binary(final String name, final String alias, final DoubleBinaryOperator op, final int[][] simplifications) {
        return checker -> checker.binary(name, alias, op, simplifications);
    }

    private static Operation infix(final String name, final String alias, final int priority, final DoubleBinaryOperator op) {
        return checker -> checker.infix(name, alias, priority, op);
    }

    private static Operation fix(final String name, final String alias, final int arity, final Function<DoubleStream, OptionalDouble> f) {
        final BaseTester.Func wf = args -> f.apply(Arrays.stream(args)).orElseThrow();
        return arity >= 0
                ? fixed(name + arity, alias + arity, arity, wf, null)
                : any(name, alias, -arity - 1, -arity - 1, wf);
    }

    private static Operation fixed(final String name, final String alias, final int arity, final BaseTester.Func f, final int[][] simplifications) {
        return checker -> checker.fixed(name, alias, arity, f, simplifications);
    }

    private static Operation any(final String name, final String alias, final int minArity, final int fixedArity, final BaseTester.Func f) {
        return checker -> checker.any(name, alias, minArity, fixedArity, f);
    }
}
