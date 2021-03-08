package queue;

import base.TestCounter;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueTest<T extends ArrayQueueTest.Model> extends ReflectionTest {
    private static final int OPERATIONS = 50_000;

    private static final Object[] ELEMENTS = new Object[]{
            "Hello",
            "world",
            1, 2, 3,
            List.of("a"),
            List.of("a"),
            List.of("b"),
            Map.of()
    };

    private final Class<T> model;
    private final Tester<T> tester;
    private final TestCounter counter = new TestCounter();
    protected final Random random = new Random(2474258720358724193L);

    protected ArrayQueueTest(final Class<T> model, final Tester<T> tester) {
        this.model = model;
        this.tester = tester;
    }

    public static <M extends Model, T extends Tester<M>> void testArrayQueue(final Class<M> type, final T tester) {
        new ArrayQueueTest<>(type, tester).test();
    }

    public static void main(final String[] args) {
        ArrayQueueTest.testArrayQueue(Model.class, d -> () -> d);
    }

    protected void test() {
        test("ArrayQueue", Mode.values());
    }

    protected void test(final String className, final Mode... modes) {
        for (final Mode mode : modes) {
            System.err.printf("Running %s for %s in %s mode%n", model.getEnclosingClass().getSimpleName(), className, mode);
            test(className, mode);
        }
        counter.printStatus(getClass());
    }

    private void test(final String className, final Mode mode) {
        final Supplier<T> factory = factory(className, mode);
        testEmpty(factory.get());
        testSingleton(factory.get());
        testClear(factory.get());
        for (int i = 0; i <= 10; i += 2) {
            testRandom(factory.get(), (double) i / 10);
        }
    }

    protected Supplier<T> factory(final String name, final Mode mode) {
        final ProxyFactory<T> factory = new ProxyFactory<>(model, mode, "queue." + name);
        checkImplementation(factory.implementation);
        return () -> checking(model, tester.wrap(new ArrayDeque<>()), factory.create());
    }

    protected void checkImplementation(final Class<?> implementation) {
        // Do nothing by default
    }

    protected void testEmpty(final T queue) {
        nextTest("testEmpty");
        assertSize(0, queue);
        counter.passed();
    }

    protected void testSingleton(final T queue) {
        nextTest("testSingleton");
        assertSize(0, queue);
        final String value = "value";
        queue.enqueue(value);
        assertSize(1, queue);
        assertEquals("element()", value, queue.element());
        assertEquals("dequeue()", value, queue.dequeue());
        assertSize(0, queue);
        counter.passed();
    }

    private void nextTest(final String name) {
        System.err.println("\t=== " + name);
        counter.nextTest();
    }

    protected void testClear(final T queue) {
        nextTest("testClear");
        assertSize(0, queue);

        final String value = "value";
        queue.enqueue(value);
        queue.enqueue(value);
        queue.clear();
        assertSize(0, queue);

        final String value1 = "value1";
        queue.enqueue(value1);
        assertEquals("deque()", value1, queue.dequeue());
        counter.passed();
    }

    protected void testRandom(final T initial, final double addFreq) {
        nextTest("testRandom, add frequency = " + addFreq);
        final List<T> queues = new ArrayList<>();
        queues.add(initial);
        int ops = 0;
        for (int i = 0; i < OPERATIONS; i++) {
            counter.nextTest();
            final T queue = queues.get(random.nextInt(queues.size()));
            if (queue.isEmpty() || random.nextDouble() < addFreq) {
                tester.add(queue, randomElement(), random);
            } else {
                tester.remove(queue, random);
            }

            final int size = checkAndSize(queue);
            counter.passed();

            if (ops++ >= size && random.nextInt(4) == 0) {
                ops -= size;

                counter.nextTest();
                queues.addAll(tester.linearTest(queue, random));
                checkAndSize(queue);
                counter.passed();
            }
        }

        for (final T queue : queues) {
            counter.nextTest();
            tester.linearTest(queue, random);
            checkAndSize(queue);
            for (int i = queue.size(); i > 0; i--) {
                tester.remove(queue, random);
                checkAndSize(queue);
            }
            counter.passed();
        }

        counter.passed();
    }

    private int checkAndSize(final T queue) {
        final int size = queue.size();
        if (!queue.isEmpty() && random.nextBoolean()) {
            tester.check(queue, random);
        }
        return size;
    }

    protected Object randomElement() {
        return ELEMENTS[random.nextInt(ELEMENTS.length)];
    }

    protected void assertSize(final int size, final T queue) {
        counter.nextTest();
        assertEquals("size()", size, queue.size());
        assert queue.size() == size : "Expected size() " + size + ", found " + queue.size();
        assert (size == 0) == queue.isEmpty() : "Expected isEmpty() " + (size == 0) + ", found " + queue.isEmpty();
        counter.passed();
    }

    @Override
    protected void checkResult(final String call, final Object expected, final Object actual) {
        if (expected instanceof Model) {
            super.checkResult(call, toList((Model) expected), toList((Model) actual));
        } else {
            super.checkResult(call, expected, actual);
        }
    }

    private static List<Object> toList(final Model queue) {
        final List<Object> list = Stream.generate(queue::dequeue).limit(queue.size()).collect(Collectors.toUnmodifiableList());
        list.forEach(queue::enqueue);
        return list;
    }

    protected static ArrayDeque<Object> collect(final Stream<Object> elements) {
        return elements.collect(Collectors.toCollection(ArrayDeque::new));
    }

    /**
     * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
     */
    protected interface Model {
        @Ignore
        ArrayDeque<Object> model();

        default Object dequeue() {
            return model().removeFirst();
        }

        default int size() {
            return model().size();
        }

        default boolean isEmpty() {
            return model().isEmpty();
        }

        default void clear() {
            model().clear();
        }

        default void enqueue(final Object element) {
            model().addLast(element);
        }

        default Object element() {
            return model().getFirst();
        }
    }

    protected interface Tester<T extends Model> {
        T wrap(ArrayDeque<Object> reference);

        default List<T> linearTest(final T queue, final Random random) {
            // Do nothing by default
            return List.of();
        }

        default void check(final T queue, final Random random) {
            queue.element();
        }

        default void add(final T queue, final Object element, final Random random) {
            queue.enqueue(element);
        }

        default Object randomElement(final Random random) {
            return ELEMENTS[random.nextInt(ELEMENTS.length)];
        }

        default void remove(final T queue, final Random random) {
            queue.dequeue();
        }

        @SuppressWarnings("unchecked")
        default T cast(final Model model) {
            return (T) model;
        }
    }
}
