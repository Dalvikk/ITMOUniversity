package queue;

import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueToStrTest {
    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayQueueTest.Model {
        default String toStr() {
            return model().toString();
        }
    }

    protected interface Tester<T extends Model> extends ArrayQueueTest.Tester<T> {
        @Override
        default List<T> linearTest(final T queue, final Random random) {
            queue.toStr();
            return List.of();
        }
    }
}
