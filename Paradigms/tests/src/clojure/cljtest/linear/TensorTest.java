package cljtest.linear;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class TensorTest extends LinearTest {
    public static final List<Item.Fun> TENSOR = Item.functions("t");

    public TensorTest(final String[] args) {
        super(args);
    }

    @Override
    protected void test(final int args) {
        super.test(args);

        for (int complexity = 0; complexity <= 20; complexity++) {
//            System.out.format("Args=%d, complecity=%d%n", args, complexity);
            final int[] dims = tensor(complexity).toArray();
            test(args, TENSOR, Item.generator(dims));
            if (hard && args == 2) {
                expectException(TENSOR, dims, new int[][]{});
            }
        }
    }

    private IntStream tensor(final int complexity) {
        if (complexity <= 0) {
            return IntStream.of();
        }
        final int dim = 1 + random.nextInt(Math.min(complexity, 4));
        return IntStream.concat(IntStream.of(dim), tensor(complexity - dim));
    }

    public static void main(final String... args) {
        new TensorTest(args).run();
    }
}
