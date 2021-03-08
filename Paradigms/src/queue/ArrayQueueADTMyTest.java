package queue;

public class ArrayQueueADTMyTest {
    private static void fill(ArrayQueueADT queue, int factor) {
        for (int i = 1; i <= 10; i++) {
            ArrayQueueADT.enqueue(queue, i * factor);
        }
    }

    public static void main(String[] args) {
        ArrayQueueADT queue = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        fill(queue, 10);
        fill(queue2, 20);
        System.out.println(ArrayQueueADT.toStr(queue)); // [10, 20 ... 100]
        System.out.println(ArrayQueueADT.toStr(queue2)); // [20, 40 ... 200]
        ArrayQueueADT.clear(queue);
        ArrayQueueADT.clear(queue2);
        System.out.println(ArrayQueueADT.toStr(queue)); // []
        System.out.println(ArrayQueueADT.toStr(queue2)); // []
        System.out.println(ArrayQueueADT.size(queue)); // 0
        System.out.println(ArrayQueueADT.isEmpty(queue)); // true
        try {
            ArrayQueueADT.dequeue(queue); // Exception
            Object ignored = ArrayQueueADT.element(queue); // Exception too
            ArrayQueueADT.enqueue(queue, null); // Exception too
            System.out.println("Unsuccessful: exception expected");
        } catch (Exception e) {
            System.out.println("Successful: " + e.getMessage());
        }
    }
}
