package queue;

import java.util.List;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayDequeToStrArrayTest {
    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayDequeTest.Model, ArrayQueueToStrTest.Model, ArrayQueueToArrayTest.Model {
    }

    protected interface Tester<T extends Model> extends
            ArrayDequeTest.Tester<T>,
            ArrayQueueToStrTest.Tester<T>,
            ArrayQueueToArrayTest.Tester<T>
    {
        @Override
        default List<T> linearTest(final T queue, final Random random) {
            if (random.nextBoolean()) {
                ArrayQueueToStrTest.Tester.super.linearTest(queue, random);
            } else {
                ArrayQueueToArrayTest.Tester.super.linearTest(queue, random);
            }
            return List.of();
        }
    }
}
