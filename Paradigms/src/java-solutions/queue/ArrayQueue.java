package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueue extends AbstractQueue {
    private int front = 0;
    private Object[] elements = new Object[2];

    @Override
    public Queue newQueue() {
        return new ArrayQueue();
    }

    @Override
    protected void enqueueImpl(Object o) {
        ensureCapacity(size + 1);
        elements[getTail()] = o;
    }

    @Override
    protected Object elementImpl() {
        return elements[front];
    }

    @Override
    protected void dequeueImpl() {
        elements[front] = null;
        front = inc(front);
    }

    private void ensureCapacity(int newCapacity) {
        if (elements.length < newCapacity) {
            elements = Arrays.copyOf(toArray(), 2 * newCapacity);
            front = 0;
        }
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
            throw new IllegalStateException("Queue is empty");
        }
        return elements[dec(getTail())];
    }

    // Pred: size > 0
    // Post: R == a[size] && size' = size - 1 && for i : 0 <= i < size - 1: a'[i] = a[i + 1]
    public Object remove() {
        Object element = peek();
        size--;
        elements[getTail()] = null;
        return element;
    }

    private int getTail() {
        return (front + size) % elements.length;
    }

    private int inc(int i) {
        return i == (elements.length - 1) ? 0 : i + 1;
    }

    private int dec(int i) {
        return i == 0 ? elements.length - 1 : i - 1;
    }

    // Pred: true
    // Post: R == Object[]{a1, a_2 ... a_size} && Imm
    public Object[] toArray() {
        Object[] ans = new Object[size];
        if (front + size < elements.length) {
            System.arraycopy(elements, front, ans, 0, size);
        } else {
            System.arraycopy(elements, front, ans, 0, elements.length - front);
            System.arraycopy(elements, 0, ans, elements.length - front, getTail());
        }
        return ans;
    }

    // Pred: true
    // Post: R == "[a_1, a_2 ... a_size]" && Imm
    public String toStr() {
        return Arrays.toString(toArray());
    }

    @Override
    public void clear() {
        Arrays.fill(elements, null);
        front = size = 0;
    }
}
