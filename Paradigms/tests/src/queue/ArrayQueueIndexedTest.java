package queue;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Random;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueIndexedTest {
    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends ArrayQueueTest.Model {
        Field ELEMENTS = getField("elements");
        Field HEAD = getField("head");

        default Object get(final int index) {
            final Object[] elements = get(ELEMENTS);
            final int head = get(HEAD);
            return elements[(head + index) % elements.length];
        }

        default void set(final int index, final Object value) {
            final Object[] elements = get(ELEMENTS);
            final int head = get(HEAD);
            elements[(head + index) % elements.length] = value;
        }

        private static Field getField(final String name) {
            try {
                final Field field = ArrayDeque.class.getDeclaredField(name);
                field.setAccessible(true);
                return field;
            } catch (final NoSuchFieldException e) {
                throw new AssertionError("Reflection error: " + e.getMessage(), e);
            }
        }

        @SuppressWarnings("unchecked")
        private <Z> Z get(final Field field) {
            try {
                return (Z) field.get(model());
            } catch (final IllegalAccessException e) {
                throw new AssertionError("Cannot access field " + field.getName() + ": " + e.getMessage(), e);
            }
        }
    }

    protected interface Tester<T extends Model> extends ArrayQueueTest.Tester<T> {
        @Override
        default void check(final T queue, final Random random) {
            ArrayQueueTest.Tester.super.check(queue, random);
            queue.get(randomIndex(queue, random));
        }

        @Override
        default void add(final T queue, final Object element, final Random random) {
            if (queue.isEmpty() || random.nextBoolean()) {
                ArrayQueueTest.Tester.super.add(queue, element, random);
            } else {
                queue.set(randomIndex(queue, random), randomElement(random));
            }
        }

        private static int randomIndex(final ArrayQueueTest.Model queue, final Random random) {
            return random.nextInt(queue.size());
        }
    }
}
