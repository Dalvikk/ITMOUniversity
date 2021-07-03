package search;

import base.MainChecker;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BinarySearchMinTest extends BinarySearchBaseTest {
    public static final int[] SIZES = {5, 4, 2, 1, 10, 100, 300};
    public static final int[] VALUES = new int[]{5, 4, 2, 1, 0, 10, 100, Integer.MAX_VALUE / 2};

    public static void test() {
        final MainChecker checker = new MainChecker("search.BinarySearchMin");

        test(checker, 0, 0);
        for (final int size : SIZES) {
            for (final int max : VALUES) {
                final int[] a = sortedInts(checker, size, max);
                final int answer = a[0];
                for (int k = 0; k < a.length; k++) {
                    test(checker, answer, a);

                    final int last = a[a.length - 1];
                    System.arraycopy(a, 0, a, 1, a.length - 1);
                    a[0] = last;
                }
            }
        }
        checker.printStatus();
    }

    private static void test(final MainChecker checker, final int k, final int... a) {
        checker.checkEquals(
                List.of(Integer.toString(k)),
                checker.run(IntStream.of(a).mapToObj(Integer::toString).toArray(String[]::new))
        );
    }

    public static void main(final String... args) {
        test();
    }
}
