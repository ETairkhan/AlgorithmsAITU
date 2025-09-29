package Assignment2.StudentA.algorithms;

import Assignment2.StudentA.metrics.PerformanceTracker;

public class InsertionSort {

    public static void sort(int[] arr, PerformanceTracker tracker) {
        int n = arr.length;
        tracker.startTimer();

        for (int i = 1; i < n; i++) {
            tracker.recordArrayAccess();
            int key = arr[i];
            int j = i - 1;

            while (j >= 0) {
                tracker.recordComparison();
                tracker.recordArrayAccess();

                if (arr[j] > key) {
                    tracker.recordArrayAccesses(2);
                    arr[j + 1] = arr[j];
                    tracker.recordSwap();
                    j--;
                } else {
                    break;
                }
            }

            tracker.recordArrayAccess();
            arr[j + 1] = key;
        }

        tracker.stopTimer();
    }
}
