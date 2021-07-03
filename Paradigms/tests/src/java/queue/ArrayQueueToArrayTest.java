package queue;

import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueToArrayTest {
    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayQueueTest.Model {
        default Object[] toArray() {
            return model().toArray();
        }
    }

    protected interface Tester<T extends Model> extends ArrayQueueTest.Tester<T> {
        @Override
        default List<T> linearTest(final T queue, final Random random) {
            queue.toArray();
            return List.of();
        }
    }
}
