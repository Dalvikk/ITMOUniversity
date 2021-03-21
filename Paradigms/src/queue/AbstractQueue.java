package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size = 0;

    public abstract Queue newQueue();

    public void enqueue(Object o) {
        Objects.requireNonNull(o);
        enqueueImpl(o);
        size++;
    }

    public Object element() {
        if (size == 0) {
            throw new IllegalStateException("Queue is empty");
        }
        return elementImpl();
    }

    public Object dequeue() {
        Object element = element();
        dequeueImpl();
        size--;
        return element;
    }

    public Queue removeNth(int k) {
        Queue queue = getNth(k);
        dropNth(k);
        return queue;
    }

    public Queue getNth(int k) {
        return subQueue(k, false);
    }

    public void dropNth(int k) {
        subQueue(k, true);
    }

    private Queue subQueue(int k, boolean remove) {
        Queue ans = remove ? this : newQueue();
        int sz = this.size;
        for (int i = 1; i <= sz; i++) {
            Object o = dequeue();
            if ((!remove && (i % k == 0)) || (remove && (i % k != 0))) {
                ans.enqueue(o);
            }
            if (!remove) {
                this.enqueue(o);
            }
        }
        return ans;
    }

    protected abstract void enqueueImpl(Object o);

    protected abstract Object elementImpl();

    protected abstract void dequeueImpl();

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void clear() {
        while (!isEmpty()) {
            dequeue();
        }
    }
}
