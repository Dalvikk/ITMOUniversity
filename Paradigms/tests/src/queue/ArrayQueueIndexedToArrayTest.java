package queue;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueIndexedToArrayTest {
    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, (Tester<Model>) d -> () -> d);
    }

    protected interface Model extends
            ArrayQueueIndexedTest.Model, ArrayQueueToArrayTest.Model {}

    protected interface Tester<T extends Model>
            extends ArrayQueueIndexedTest.Tester<T>, ArrayQueueToArrayTest.Tester<T> {}
}
