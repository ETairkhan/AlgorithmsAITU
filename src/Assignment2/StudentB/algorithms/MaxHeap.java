package Assignment2.StudentB.algorithms;

import Assignment2.StudentB.metrics.PerformanceTracker;
import java.util.Arrays;

public class MaxHeap {
    private int[] heap;
    private int size;
    private int capacity;
    private PerformanceTracker tracker;

    public MaxHeap(int capacity, PerformanceTracker tracker) {
        this.capacity = capacity;
        this.heap = new int[capacity];
        this.size = 0;
        this.tracker = tracker;
    }

    public MaxHeap(int[] arr, PerformanceTracker tracker) {
        this.heap = arr.clone();
        this.size = arr.length;
        this.capacity = arr.length;
        this.tracker = tracker;
        buildHeap();
    }

    private void buildHeap() {
        for (int i = size / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    public void insert(int value) {
        if (size == capacity) {
            resize();
        }

        heap[size] = value;
        size++;
        heapifyUp(size - 1);
    }

    public int extractMax() {
        if (size == 0) throw new IllegalStateException("Heap is empty");

        int max = heap[0];
        heap[0] = heap[size - 1];
        size--;
        heapifyDown(0);
        return max;
    }

    public void increaseKey(int index, int newValue) {
        if (index < 0 || index >= size) throw new IllegalArgumentException("Invalid index");
        if (newValue < heap[index]) throw new IllegalArgumentException("New value must be greater than current");

        heap[index] = newValue;
        heapifyUp(index);
    }

    public MaxHeap merge(MaxHeap other) {
        int newCapacity = this.size + other.size;
        MaxHeap newHeap = new MaxHeap(newCapacity, this.tracker);

        for (int i = 0; i < this.size; i++) {
            newHeap.heap[i] = this.heap[i];
        }
        for (int i = 0; i < other.size; i++) {
            newHeap.heap[this.size + i] = other.heap[i];
        }

        newHeap.size = this.size + other.size;
        newHeap.buildHeap();
        return newHeap;
    }

    private void heapifyUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;
            tracker.recordComparison();
            if (heap[index] <= heap[parent]) break;

            swap(index, parent);
            index = parent;
        }
    }

    private void heapifyDown(int index) {
        while (true) {
            int left = 2 * index + 1;
            int right = 2 * index + 2;
            int largest = index;

            tracker.recordComparison();
            if (left < size && heap[left] > heap[largest]) {
                largest = left;
            }

            tracker.recordComparison();
            if (right < size && heap[right] > heap[largest]) {
                largest = right;
            }

            if (largest == index) break;

            swap(index, largest);
            index = largest;
        }
    }

    private void resize() {
        capacity *= 2;
        heap = Arrays.copyOf(heap, capacity);
    }

    private void swap(int i, int j) {
        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;
        tracker.recordSwap();
    }

    // Getters
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }
    public int[] getHeap() { return Arrays.copyOf(heap, size); }
}