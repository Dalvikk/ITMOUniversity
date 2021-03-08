package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueADT {
    private int size = 0;
    private int front = 0;
    private Object[] elements = new Object[2];

    /*
    Model:
        size -- queue's size
        [a_1, a_2, ... a_size] -- queue's elements
     */

    // Imm: size' = size && for i: 1 <= i <= size: a'[i] == a[i]

    // Pred: queue != null, o != null
    // Post: size' = size + 1 && a'[size] = o && for i: 1 <= i <= size: a'[i] = a[i]
    public static void enqueue(ArrayQueueADT queue, Object o) {
        Objects.requireNonNull(o);
        ensureCapacity(queue, queue.size + 1);
        queue.elements[getRear(queue)] = o;
        queue.size++;
    }

    private static int getRear(ArrayQueueADT queue) {
        return (queue.front + queue.size) % queue.elements.length;
    }

    private static int inc(ArrayQueueADT queue, int i) {
        return i == (queue.elements.length - 1) ? 0 : i + 1;
    }

    private static int dec(ArrayQueueADT queue, int i) {
        return i == 0 ? queue.elements.length - 1 : i - 1;
    }

    private static void ensureCapacity(ArrayQueueADT queue, int newCapacity) {
        if (queue.elements.length < newCapacity) {
            queue.elements = Arrays.copyOf(toArray(queue), 2 * newCapacity);
            queue.front = 0;
        }
    }

    // Pred: queue != null, size > 0
    // Post: R == a[1] && for i: 1 < i <= size: a'[i] = a[i] && Imm
    public static Object element(ArrayQueueADT queue) {
        if (queue.size == 0) {
            throw new IllegalStateException("Queue's size is zero");
        }
        return queue.elements[queue.front];
    }

    // Pred: queue != null, size > 0
    // Post: R == a[1] && size' = size - 1 && for i : 1 <= i < size: a'[i] = a[i + 1]
    public static Object dequeue(ArrayQueueADT queue) {
        Object element = element(queue);
        queue.elements[queue.front] = null;
        queue.front = inc(queue, queue.front);
        queue.size--;
        return element;
    }

    // Pred: queue != null, o != null
    // Post: size' = size + 1 && a'[1] = o && for i: 2 <= i <= size': a'[i] = a[i - 1]
    public static void push(ArrayQueueADT queue, Object o) {
        Objects.requireNonNull(o);
        ensureCapacity(queue, queue.size + 1);
        queue.front = dec(queue, queue.front);
        queue.elements[queue.front] = o;
        queue.size++;
    }

    // Pred: queue != null, size > 0
    // Post: R == a[size] && for i: 1 < i <= size: a'[i] = a[i] && Imm
    public static Object peek(ArrayQueueADT queue) {
        if (queue.size == 0) {
            throw new IllegalStateException("Queue's size is zero");
        }
        return queue.elements[dec(queue, getRear(queue))];
    }

    // Pred: queue != null, size > 0
    // Post: R == a[size] && size' = size - 1 && for i : 0 <= i < size - 1: a'[i] = a[i + 1]
    public static Object remove(ArrayQueueADT queue) {
        Object element = peek(queue);
        queue.size--;
        queue.elements[getRear(queue)] = null;
        return element;
    }

    // Pred: queue != null
    // Post: R == Object[]{a1, a_2 ... a_size} && Imm
    public static Object[] toArray(ArrayQueueADT queue) {
        Object[] ans = new Object[queue.size];
        if (queue.front + queue.size < queue.elements.length) {
            System.arraycopy(queue.elements, queue.front, ans, 0, queue.size);
        } else {
            System.arraycopy(queue.elements, queue.front, ans, 0, queue.elements.length - queue.front);
            System.arraycopy(queue.elements, 0, ans, queue.elements.length - queue.front, getRear(queue));
        }
        return ans;
    }

    // Pred: queue != null
    // Post: R == "[a_1, a_2 ... a_size]" && Imm
    public static String toStr(ArrayQueueADT queue) {
        return Arrays.toString(toArray(queue));
    }


    // Pred: queue != null
    // Post: R == size && Imm
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    // Pred: queue != null
    // Post: R == (size == 0) && Imm
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    // Pred: queue != null
    // Post: size' = 0, a' = []
    public static void clear(ArrayQueueADT queue) {
        Arrays.fill(queue.elements, null);
        queue.front = queue.size = 0;
    }
}
