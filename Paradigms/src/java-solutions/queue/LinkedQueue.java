package queue;

import java.util.Objects;

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    @Override
    public Queue newQueue() {
        return new LinkedQueue();
    }

    @Override
    protected void enqueueImpl(Object o) {
        if (size == 0) {
            head = tail = new Node(o, null);
        } else {
            tail.prev = new Node(o, null);
            tail = tail.prev;
        }
    }

    @Override
    protected Object elementImpl() {
        return head.data;
    }

    @Override
    protected void dequeueImpl() {
        head = head.prev;
        if (size == 1) {
            tail = null;
        }
    }

    private static class Node {
        private final Object data;
        private Node prev;

        public Node(Object data, Node prev) {
            Objects.requireNonNull(data);
            this.data = data;
            this.prev = prev;
        }
    }
}
