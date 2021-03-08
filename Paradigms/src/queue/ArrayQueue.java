package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueue {
    private int size = 0;
    private int front = 0;
    private Object[] elements = new Object[2];

    /*
    Model:
        size -- queue's size
        [a_1, a_2, ... a_size] -- queue's elements
     */

    // Imm: size' = size && for i: 1 <= i <= size: a'[i] == a[i]

    // Pred: o != null
    // Post: size' = size + 1 && a'[size] = o && for i: 1 <= i <= size: a'[i] = a[i]
    public void enqueue(Object o) {
        Objects.requireNonNull(o);
        ensureCapacity(size + 1);
        elements[getRear()] = o;
        size++;
    }

    private int getRear() {
        return (front + size) % elements.length;
    }

    private int inc(int i) {
        return i == (elements.length - 1) ? 0 : i + 1;
    }

    private int dec(int i) {
        return i == 0 ? elements.length - 1 : i - 1;
    }

    private void ensureCapacity(int newCapacity) {
        if (elements.length < newCapacity) {
            elements = Arrays.copyOf(toArray(), 2 * newCapacity);
            front = 0;
        }
    }

    // Pred: size > 0
    // Post: R == a[1] && Imm
    public Object element() {
        if (size == 0) {
            throw new IllegalStateException("Queue's size is zero");
        }
        return elements[front];
    }

    // Pred: size > 0
    // Post: R == a[1] && size' = size - 1 && for i : 1 <= i < size: a'[i] = a[i + 1]
    public Object dequeue() {
        Object element = element();
        elements[front] = null;
        front = inc(front);
        size--;
        return element;
    }

    // Pred: o != null
    // Post: size' = size + 1 && a'[1] = o && for i: 2 <= i <= size': a'[i] = a[i - 1]
    public void push(Object o) {
        Objects.requireNonNull(o);
        ensureCapacity(size + 1);
        front = dec(front);
        elements[front] = o;
        size++;
    }

    // Pred: size > 0
    // Post: R == a[size] && Imm
    public Object peek() {
        if (size == 0) {
            throw new IllegalStateException("Queue's size is zero");
        }
        return elements[dec(getRear())];
    }

    // Pred: size > 0
    // Post: R == a[size] && size' = size - 1 && for i : 0 <= i < size - 1: a'[i] = a[i + 1]
    public Object remove() {
        Object element = peek();
        size--;
        elements[getRear()] = null;
        return element;
    }

    // Pred: true
    // Post: R == Object[]{a1, a_2 ... a_size} && Imm
    public Object[] toArray() {
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
    public String toStr() {
        return Arrays.toString(toArray());
    }


    // Pred: true
    // Post: R == size && Imm
    public int size() {
        return size;
    }

    // Pred: true
    // Post: R == (size == 0) && Imm
    public boolean isEmpty() {
        return size == 0;
    }

    // Pred: true
    // Post: size' = 0, a' = []
    public void clear() {
        Arrays.fill(elements, null);
        front = size = 0;
    }
}
