package jstest.object;

import jstest.ArithmeticTests;
import jstest.Language;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ObjectArcTanTest extends ObjectExpressionTest {
    public static final Dialect ARC_TAN_DIALECT = ObjectExpressionTest.ARITHMETIC_DIALECT.copy()
            .rename("atan", "ArcTan")
            .rename("atan2", "ArcTan2");

    public static class AtcTanTests extends ArithmeticTests {{
        unary("atan", Math::atan);
        binary("atan2", Math::atan2);
        tests(
                f("atan", vx),
                f("atan2", vx, vy),
                f("atan", c(10)),
                f("atan2", c(3), c(4)),
                f("atan2", c(3), vx),
                f("atan2", c(4), vy),
                f("atan", f("-", vx, vy)),
                f("atan2", f("+", vx, vy), vz),
                f("atan", f("/", f("atan2", vz, f("+", vx, vy)), c(3))),
                f("+", f("atan", f("atan2", c(1), f("+", vx, c(10)))), f("*", vz, f("*", vy, c(0)))),
                f("+", f("atan", f("atan2", c(1), f("+", vx, c(10)))), f("*", vz, f("*", vy, c(10))))
        );
    }}

    protected ObjectArcTanTest(final int mode, final Language language) {
        super(mode, language);
        simplifications.addAll(List.of(new int[][]{
                {13, 1, 1},
                {23, 30, 1},
                {1, 1, 1},
                {1, 1, 1},
                {17, 1, 1},
                {1, 18, 1},
                {21, 26, 1},
                {27, 27, 38},
                {103, 103, 100},
                {64, 1, 1}
        }));
    }


    public static void main(final String... args) {
        test(ObjectArcTanTest.class, ObjectArcTanTest::new, new AtcTanTests(), args, ARC_TAN_DIALECT);
    }
}
