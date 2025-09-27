package Assignment2.StudentB.algorithms;

import Assignment2.StudentB.metrics.PerformanceTracker;
import java.util.Arrays;

public class KadaneAlgorithm {
    private PerformanceTracker tracker;

    public KadaneAlgorithm(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public static class Result {
        public int maxSum;
        public int start;
        public int end;
        public int[] subarray;

        public Result(int maxSum, int start, int end, int[] originalArray) {
            this.maxSum = maxSum;
            this.start = start;
            this.end = end;
            this.subarray = Arrays.copyOfRange(originalArray, start, end + 1);
        }
    }

    public Result findMaximumSubarray(int[] arr) {
        if (arr == null || arr.length == 0) {
            throw new IllegalArgumentException("Array cannot be null or empty");
        }

        int maxSoFar = arr[0];
        int maxEndingHere = arr[0];
        int start = 0, end = 0;
        int tempStart = 0;

        for (int i = 1; i < arr.length; i++) {
            tracker.recordComparison();
            if (maxEndingHere + arr[i] < arr[i]) {
                maxEndingHere = arr[i];
                tempStart = i;
            } else {
                maxEndingHere += arr[i];
            }

            tracker.recordComparison();
            if (maxEndingHere > maxSoFar) {
                maxSoFar = maxEndingHere;
                start = tempStart;
                end = i;
            }
        }

        return new Result(maxSoFar, start, end, arr);
    }
}