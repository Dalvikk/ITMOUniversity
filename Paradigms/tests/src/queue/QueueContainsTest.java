package queue;

import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueContainsTest {
    public static void main(final String[] args) {
        QueueTest.testQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayQueueTest.Model {
        default boolean contains(final Object element) {
            return model().contains(element);
        }

        default boolean removeFirstOccurrence(final Object element) {
            return model().removeFirstOccurrence(element);
        }
    }

    protected interface Tester<T extends Model> extends ArrayQueueTest.Tester<T> {
        @Override
        default List<T> linearTest(final T queue, final Random random) {
            final Object element = random.nextBoolean() ? randomElement(random) : random.nextInt();
            if (random.nextBoolean()) {
                queue.contains(element);
            } else {
                queue.removeFirstOccurrence(element);
            }
            return List.of();
        }
    }
}
