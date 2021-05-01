package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueModule {
    private static int size = 0;
    private static int front = 0;
    private static Object[] elements = new Object[2];

    /*
    Model:
        size -- queue's size
        [a_1, a_2, ... a_size] -- queue's elements
     */

    // Imm: size' = size && for i: 1 <= i <= size: a'[i] == a[i]

    // Pred: o != null
    // Post: size' = size + 1 && a'[size] = o && for i: 1 <= i <= size: a'[i] = a[i]
    public static void enqueue(Object o) {
        Objects.requireNonNull(o);
        ensureCapacity(size + 1);
        elements[getRear()] = o;
        size++;
    }

    private static int getRear() {
        return (front + size) % elements.length;
    }

    private static int inc(int i) {
        return i == (elements.length - 1) ? 0 : i + 1;
    }

    private static int dec(int i) {
        return i == 0 ? elements.length - 1 : i - 1;
    }

    private static void ensureCapacity(int newCapacity) {
        if (elements.length < newCapacity) {
            elements = Arrays.copyOf(toArray(), 2 * newCapacity);
            front = 0;
        }
    }

    // Pred: size > 0
    // Post: R == a[1] && Imm
    public static Object element() {
        if (size == 0) {
            throw new IllegalStateException("Queue's size is zero");
        }
        return elements[front];
    }

    // Pred: size > 0
    // Post: R == a[1] && size' = size - 1 && for i : 1 <= i < size: a'[i] = a[i + 1]
    public static Object dequeue() {
        Object element = element();
        elements[front] = null;
        front = inc(front);
        size--;
        return element;
    }

    // Pred: o != null
    // Post: size' = size + 1 && a'[1] = o && for i: 2 <= i <= size': a'[i] = a[i - 1]
    public static void push(Object o) {
        Objects.requireNonNull(o);
        ensureCapacity(size + 1);
        front = dec(front);
        elements[front] = o;
        size++;
    }

    // Pred: size > 0
    // Post: R == a[size] && Imm
    public static Object peek() {
        if (size == 0) {
            throw new IllegalStateException("Queue's size is zero");
        }
        return elements[dec(getRear())];
    }

    // Pred: size > 0
    // Post: R == a[size] && size' = size - 1 && for i : 0 <= i < size - 1: a'[i] = a[i + 1]
    public static Object remove() {
        Object element = peek();
        size--;
        elements[getRear()] = null;
        return element;
    }

    // Pred: true
    // Post: R == Object[]{a1, a_2 ... a_size} && Imm
    public static Object[] toArray() {
        Object[] ans = new Object[size];
        if (front + size < elements.length) {
            System.arraycopy(elements, front, ans, 0, size);
        } else {
            System.arraycopy(elements, front, ans, 0, elements.length - front);
            System.arraycopy(elements, 0, ans, elements.length - front, getRear());
        }
        return ans;
    }

    // Pred: true
    // Post: R == "[a_1, a_2 ... a_size]" && Imm
    public static String toStr() {
        return Arrays.toString(toArray());
    }


    // Pred: true
    // Post: R == size && Imm
    public static int size() {
        return size;
    }

    // Pred: true
    // Post: R == (size == 0) && Imm
    public static boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: size' = 0, a' = []
    public static void clear() {
        Arrays.fill(elements, null);
        front = size = 0;
    }
}
