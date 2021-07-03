package queue;

import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueTest<T extends ArrayQueueTest.Model> extends ArrayQueueTest<T> {
    protected QueueTest(final Class<T> type, final Tester<T> tester) {
        super(type, tester);
    }

    public static void main(final String[] args) {
        testQueue(Model.class, d -> () -> d);
    }

    public static <T extends Model, TT extends Tester<T>> void testQueue(final Class<T> type, final TT tester) {
        new QueueTest<>(type, tester).test();
    }

    public void test() {
        test("LinkedQueue", Mode.CLASS);
        test("ArrayQueue", Mode.CLASS);
    }

    private static boolean implementsQueue(final Class<?> type) {
        return type != Object.class
                && (Stream.of(type.getInterfaces()).map(Class::getName).anyMatch("queue.Queue"::equals)
                    || implementsQueue(type.getSuperclass()));
    }

    @Override
    protected void checkImplementation(final Class<?> type) {
        assertTrue(type + " should extend AbstractQueue", "queue.AbstractQueue".equals(type.getSuperclass().getName()));
        assertTrue(type + " should implement interface Queue", implementsQueue(type));
    }
}
