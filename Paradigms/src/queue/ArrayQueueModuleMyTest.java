package queue;

public class ArrayQueueModuleMyTest {
    private static void fill(int factor) {
        for (int i = 1; i <= 10; i++) {
            ArrayQueueModule.enqueue(i * factor);
        }
    }

    public static void main(String[] args) {
        System.out.println(ArrayQueueModule.toStr()); // []
        fill(10);
        System.out.println(ArrayQueueModule.toStr()); // [10, 20 ... 100]
        ArrayQueueModule.clear();
        System.out.println(ArrayQueueModule.toStr()); // []
        System.out.println(ArrayQueueModule.size()); // 0
        try {
            ArrayQueueModule.dequeue(); // Exception
            Object ignored = ArrayQueueModule.element(); // Exception too
            ArrayQueueModule.enqueue(null); // Exception too
            System.out.println("Unsuccessful: exception expected");
        } catch (Exception e) {
            System.out.println("Successful: " + e.getMessage());
        }
    }
}
