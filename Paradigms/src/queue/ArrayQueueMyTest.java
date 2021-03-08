package queue;

public class ArrayQueueMyTest {
    private static void fill(ArrayQueue queue, int factor) {
        for (int i = 1; i <= 10; i++) {
            queue.enqueue(i * factor);
        }
    }

    public static void main(String[] args) {
        ArrayQueue queue = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        System.out.println(queue.toStr()); // []
        System.out.println(queue2.toStr()); // []
        fill(queue, 10);
        fill(queue2, 20);
        System.out.println(queue.toStr()); // [10, 20 ... 100]
        System.out.println(queue2.toStr()); // [20, 40 ... 200]
        queue.clear();
        queue2.clear();
        System.out.println(queue.toStr()); // []
        System.out.println(queue2.toStr()); // []
        System.out.println(queue.size()); // 0
        System.out.println(queue.isEmpty()); // true
        try {
            queue.dequeue(); // Exception
            Object ignored = queue.element(); // Exception too
            queue.enqueue(null); // Exception too
            System.out.println("Unsuccessful: exception expected");
        } catch (Exception e) {
            System.out.println("Successful: " + e.getMessage());
        }
    }
}
