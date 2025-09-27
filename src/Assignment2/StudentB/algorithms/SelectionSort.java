package Assignment2.StudentB.algorithms;

import Assignment2.StudentB.metrics.PerformanceTracker;

public class SelectionSort {
    private PerformanceTracker tracker;

    public SelectionSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public int[] sort(int[] arr) {
        if (arr == null) throw new IllegalArgumentException("Input array cannot be null");
        if (arr.length <= 1) return arr.clone();

        int[] result = arr.clone();
        int n = result.length;
        boolean sorted = true;

        for (int i = 0; i < n - 1; i++) {
            int minIndex = i;
            sorted = true; // Assume sorted until proven otherwise

            for (int j = i + 1; j < n; j++) {
                tracker.recordComparison();
                if (result[j] < result[minIndex]) {
                    minIndex = j;
                    sorted = false; // Found out-of-order element
                }
            }

            // Early termination if already sorted
            if (sorted) break;

            if (minIndex != i) {
                swap(result, i, minIndex);
                tracker.recordSwap();
            }
        }
        return result;
    }

    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}