package Assignment2.StudentB.algorithms;

import Assignment2.StudentB.metrics.PerformanceTracker;
public class HeapSort {
    private PerformanceTracker tracker;

    public HeapSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public void sort(int[] arr) {
        if (arr == null) throw new IllegalArgumentException("Input array cannot be null");
        if (arr.length <= 1) return;

        int n = arr.length;

        // Build max heap using bottom-up heapify
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }

        // Extract elements from heap one by one
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            tracker.recordSwap();
            heapify(arr, i, 0);
        }
    }

    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        tracker.recordComparison();
        if (left < n && arr[left] > arr[largest]) {
            largest = left;
        }

        tracker.recordComparison();
        if (right < n && arr[right] > arr[largest]) {
            largest = right;
        }

        if (largest != i) {
            swap(arr, i, largest);
            tracker.recordSwap();
            heapify(arr, n, largest);
        }
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}