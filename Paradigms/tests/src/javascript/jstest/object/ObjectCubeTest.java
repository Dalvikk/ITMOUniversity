package jstest.object;

import jstest.ArithmeticTests;
import jstest.Language;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectCubeTest extends ObjectExpressionTest {
    public static final Dialect CUBE_CBRT_DIALECT = ObjectExpressionTest.ARITHMETIC_DIALECT.copy()
            .rename("cube", "Cube")
            .rename("cbrt", "Cbrt");

    public static class CubeCbrtTests extends ArithmeticTests {{
        unary("cube", a -> a * a * a);
        unary("cbrt", Math::cbrt);
        tests(
                f("cube", f("-", vx, vy)),
                f("cbrt", f("+", vx, vy)),
                f("cbrt", f("/", f("cube", vz), f("+", vx, vy))),
                f("+", vx, f("cbrt", f("cube", c(2)))),
                f("+", vx, f("cbrt", f("-", vy, f("/", f("cube", c(3)), c(4))))),
                f("+", f("cube", f("-", vx, c(3))), f("*", vz, f("*", vy, f("cbrt", c(-1)))))
        );
    }}

    protected ObjectCubeTest(final int mode, final Language language) {
        super(mode, language);
        simplifications.addAll(List.of(new int[][]{
                new int[]{17, 22, 1},
                new int[]{44, 44, 1},
                new int[]{94, 94, 86},
                new int[]{1, 1, 1},
                new int[]{1, 50, 1},
                new int[]{17, 6, 6},
        }));
    }

    public static void main(final String... args) {
        test(ObjectCubeTest.class, ObjectCubeTest::new, new CubeCbrtTests(), args, CUBE_CBRT_DIALECT);
    }
}
