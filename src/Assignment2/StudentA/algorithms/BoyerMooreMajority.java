package Assignment2.StudentA.algorithms;

import Assignment2.StudentA.metrics.PerformanceTracker;

public class BoyerMooreMajority {

    public static Integer findMajority(int[] arr, PerformanceTracker tracker) {
        int candidate = -1, count = 0;
        tracker.startTimer();

        for (int num : arr) {
            tracker.recordArrayAccess();
            if (count == 0) {
                candidate = num;
                count = 1;
            } else {
                tracker.recordComparison();
                if (num == candidate) {
                    count++;
                } else {
                    count--;
                }
            }
        }

        count = 0;
        for (int num : arr) {
            tracker.recordArrayAccess();
            tracker.recordComparison();
            if (num == candidate) count++;
        }

        tracker.stopTimer();
        return count > arr.length / 2 ? candidate : null;
    }
}
