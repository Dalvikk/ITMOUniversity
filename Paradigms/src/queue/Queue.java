package queue;

public interface Queue {
    /*
        Model:
            size -- queue's size
            [a_1, a_2, ... a_size] -- queue's elements
    */

    // Imm: size' = size && for i: 1 <= i <= size: a'[i] == a[i]

    // Pred: o != null
    // Post: size' = size + 1 && a'[size] = o && for i: 1 <= i <= size: a'[i] = a[i]
    void enqueue(Object o);

    // Pred: size > 0
    // Post: R == a[1] && Imm
    Object element();

    // Pred: size > 0
    // Post: R == a[1] && size' = size - 1 && for i : 1 <= i < size: a'[i] = a[i + 1]
    Object dequeue();

    // Pred: k >= 1
    // Post: Imm && R is queue && R != null && R.size = size / k && for i: 1 <= i <= R.size: R[i] = a[k*i]
    Queue getNth(int k);

    // Pred: k >= 1
    // Post: size' = size - [size / k] && for i: 1 <= i <= size': a'[i] = a[i + (i / k)]
    void dropNth(int k);

    // Pred: k >= 1
    // Post: (R is queue && R != null && R.size = size / k && for i: 1 <= i <= R.size: R[i] = a[k*i]) &&
    // size' = size - [size / k] && for i: 1 <= i <= size': a'[i] = a[i + (i / k)]
    Queue removeNth(int k);

    // Pred: true
    // Post: R == size && Imm
    int size();

    // Pred: true
    // Post: R == (size == 0) && Imm
    boolean isEmpty();

    // Pred: true
    // Post: size' = 0, a' = []
    void clear();


}
