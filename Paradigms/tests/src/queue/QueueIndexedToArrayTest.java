package queue;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueIndexedToArrayTest {
    public static void main(final String[] args) {
        QueueTest.testQueue(
                ArrayQueueIndexedToArrayTest.Model.class,
                (ArrayQueueIndexedToArrayTest.Tester<ArrayQueueIndexedToArrayTest.Model>) d -> () -> d
        );
    }
}
