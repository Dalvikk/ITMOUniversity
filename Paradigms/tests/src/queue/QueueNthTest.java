package queue;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueNthTest {
    public static void main(final String[] args) {
        QueueTest.testQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayQueueTest.Model {
        // Deliberately ugly implementation
        @ReflectionTest.Wrap
        default Model getNth(final int n) {
            final ArrayDeque<Object> deque = new ArrayDeque<>();
            final int[] index = {0};
            model().forEach(e -> {
                if (++index[0] % n == 0) {
                    deque.add(e);
                }
            });
            return () -> deque;
        }

        // Deliberately ugly implementation
        @ReflectionTest.Wrap
        default Model removeNth(final int n) {
            final ArrayDeque<Object> deque = new ArrayDeque<>();
            final int[] index = {0};
            model().removeIf(e -> {
                if (++index[0] % n == 0) {
                    deque.add(e);
                    return true;
                } else {
                    return false;
                }
            });
            return () -> deque;
        }

        default void dropNth(final int n) {
            final int[] index = {0};
            model().removeIf(e -> ++index[0] % n == 0);
        }
    }

    protected interface Tester<T extends Model> extends ArrayQueueTest.Tester<T> {
        @Override
        default List<T> linearTest(final T queue, final Random random) {
            final int n = random.nextInt(5) + 1;
            switch (random.nextInt(3)) {
                case 0:
                    final Model model = queue.removeNth(n);
                    return List.of(cast(model));
                case 1:
                    queue.dropNth(n);
                    return List.of();
                case 2:
                    return List.of(cast(queue.getNth(n)));
                default:
                    throw new AssertionError();
            }
        }
    }
}
