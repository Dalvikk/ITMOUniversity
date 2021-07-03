package search;

import base.MainChecker;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BinarySearchMaxTest extends BinarySearchBaseTest {
    public static final int[] SIZES = {5, 4, 2, 1, 10, 100, 300};
    public static final int[] VALUES = new int[]{5, 4, 2, 1, 0, 10, 100, Integer.MAX_VALUE - 1};

    public static void test() {
        final MainChecker checker = new MainChecker("search.BinarySearchMax");

        test(checker, 0, 0);
        for (final int size : SIZES) {
            for (final int max : VALUES) {
                final int[] a = sortedInts(checker, size, max);
                final int[] b = sortedInts(checker, size, max);
                test(checker, a[a.length - 1], a);
                for (int k = 0, i = a.length - 1; k < b.length && i >= 0; k++, i--) {
                    a[i] = b[k];
                    test(checker, i - 1 < 0 ? a[i] : Math.max(a[i], a[i - 1]), a);
                }
            }
        }
        checker.printStatus();
    }

    private static void test(final MainChecker checker, final int expected, final int... a) {
        checker.checkEquals(
                List.of(Integer.toString(expected)),
                checker.run(IntStream.of(a).mapToObj(Integer::toString).toArray(String[]::new))
        );
    }

    public static void main(final String... args) {
        test();
    }
}
