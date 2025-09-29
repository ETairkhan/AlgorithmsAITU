package Assignment2.StudentA.algorithms;

import Assignment2.StudentA.metrics.PerformanceTracker;

public class MinHeap {
    private int[] heap;
    private int size;
    private PerformanceTracker tracker;

    public MinHeap(int capacity, PerformanceTracker tracker) {
        heap = new int[capacity];
        size = 0;
        this.tracker = tracker;
    }


    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i) { return 2 * i + 1; }
    private int right(int i) { return 2 * i + 2; }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        tracker.recordSwap();
        tracker.recordArrayAccesses(4); // читаем/записываем 4 раза
    }

    public void insert(int key) {
        if (size == heap.length) return;

        heap[size] = key;
        tracker.recordArrayAccess(); // запись
        int i = size;
        size++;

        while (i != 0) {
            tracker.recordComparison();
            tracker.recordArrayAccesses(2); // heap[i], heap[parent(i)]
            if (heap[parent(i)] > heap[i]) {
                swap(i, parent(i));
                i = parent(i);
            } else {
                break;
            }
        }
    }

    private void heapify(int i) {
        tracker.recordRecursiveCall();

        int l = left(i);
        int r = right(i);
        int smallest = i;

        if (l < size) {
            tracker.recordComparison();
            tracker.recordArrayAccesses(2);
            if (heap[l] < heap[smallest]) smallest = l;
        }

        if (r < size) {
            tracker.recordComparison();
            tracker.recordArrayAccesses(2);
            if (heap[r] < heap[smallest]) smallest = r;
        }

        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    public int extractMin() {
        if (size <= 0) return Integer.MAX_VALUE;

        int root = heap[0];
        tracker.recordArrayAccess();

        if (size == 1) {
            size--;
            return root;
        }

        heap[0] = heap[size - 1];
        tracker.recordArrayAccesses(2);

        size--;
        heapify(0);

        return root;
    }

    public void buildHeap(int[] arr) {
        this.heap = arr;
        this.size = arr.length;
        tracker.recordArrayAccesses(arr.length);

        for (int i = size / 2 - 1; i >= 0; i--) {
            heapify(i);
        }
    }

    public boolean isEmpty() {
        return size == 0;
    }
}
