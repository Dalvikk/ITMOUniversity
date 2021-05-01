package cljtest.linear;

import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class LinearCuboidTest extends LinearTest {
    private static final List<Item.Fun> CUBOID = Item.functions("c");

    public LinearCuboidTest(final String[] args) {
        super(args);
    }

    public static void main(final String... args) {
        new LinearCuboidTest(args).run();
    }

    protected void test(final int args) {
        super.test(args);

        for (int complexity = 1; complexity < 10; complexity++) {
            for (int size1 = 1; size1 < complexity; size1++) {
                for (int size2 = 1; size1 + size2 < complexity; size2++) {
                    test(args, CUBOID, Item.generator(size1, size2, complexity - size1 - size2));
                }
            }
        }

        if (hard) {
            expectException(CUBOID, new int[]{3, 3, 3}, new int[][]{{}, {3}, {3, 3}, {3, 3, 3, 3}});
        }
    }
}
