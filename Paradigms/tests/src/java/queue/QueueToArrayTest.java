package queue;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueToArrayTest {
    public static void main(final String[] args) {
        QueueTest.testQueue(
                ArrayQueueToArrayTest.Model.class,
                (ArrayQueueToArrayTest.Tester<ArrayQueueToArrayTest.Model>) d -> () -> d
        );
    }
}
