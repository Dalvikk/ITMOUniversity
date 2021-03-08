package queue;

import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayDequeTest {
    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayQueueTest.Model {
        default void push(final Object element) {
            model().addFirst(element);
        }

        default Object peek() {
            return model().getLast();
        }

        default Object remove() {
            return model().removeLast();
        }
    }

    protected interface Tester<T extends Model> extends ArrayQueueTest.Tester<T> {
        @Override
        default void add(final T queue, final Object element, final Random random) {
            if (random.nextBoolean()) {
                ArrayQueueTest.Tester.super.add(queue, element, random);
            } else {
                queue.push(element);
            }
        }

        @Override
        default void check(final T queue, final Random random) {
            if (random.nextBoolean()) {
                ArrayQueueTest.Tester.super.check(queue, random);
            } else {
                queue.peek();
            }
        }

        @Override
        default void remove(final T queue, final Random random) {
            if (random.nextBoolean()) {
                ArrayQueueTest.Tester.super.remove(queue, random);
            } else {
                queue.remove();
            }
        }
    }
}
