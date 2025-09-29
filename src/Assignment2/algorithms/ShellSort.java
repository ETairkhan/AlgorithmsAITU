package Assignment2.algorithms;

import Assignment2.metrics.PerformanceTracker;

public class ShellSort {
    private PerformanceTracker tracker;

    public ShellSort(PerformanceTracker tracker) {
        this.tracker = tracker;
    }

    public void sort(int[] arr, String gapSequence) {
        if (arr == null) throw new IllegalArgumentException("Input array cannot be null");
        if (arr.length <= 1) return;

        int n = arr.length;

        // Generate gap sequence based on the chosen type
        int[] gaps;
        switch (gapSequence.toLowerCase()) {
            case "shell":
                gaps = generateShellGaps(n);
                break;
            case "knuth":
                gaps = generateKnuthGaps(n);
                break;
            case "sedgewick":
                gaps = generateSedgewickGaps(n);
                break;
            default:
                throw new IllegalArgumentException("Unknown gap sequence: " + gapSequence);
        }

        // Shell sort with the chosen gap sequence
        for (int gap : gaps) {
            if (gap == 0) continue;

            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                tracker.recordArrayAccess(); // Record array access for temp

                int j;
                for (j = i; j >= gap; j -= gap) {
                    tracker.recordComparison();
                    tracker.recordArrayAccess(); // Access arr[j - gap]

                    if (arr[j - gap] <= temp) {
                        break;
                    }

                    arr[j] = arr[j - gap];
                    tracker.recordArrayAccess(); // Record assignment
                    tracker.recordSwap();
                }

                arr[j] = temp;
                tracker.recordArrayAccess(); // Record final assignment
            }
        }
    }

    private int[] generateShellGaps(int n) {
        // Original Shell's sequence: n/2, n/4, n/8, ..., 1
        int count = (int) (Math.log(n) / Math.log(2));
        int[] gaps = new int[count];

        for (int i = 0; i < count; i++) {
            gaps[i] = n / 2;
            n /= 2;
        }
        return gaps;
    }

    private int[] generateKnuthGaps(int n) {
        // Knuth's sequence: (3^k - 1)/2, less than n/3
        int k = 1;
        int maxGap = 1;

        while (maxGap < n / 3) {
            maxGap = (int) ((Math.pow(3, k) - 1) / 2);
            k++;
        }

        int[] gaps = new int[k - 1];
        for (int i = 0; i < gaps.length; i++) {
            gaps[i] = (int) ((Math.pow(3, gaps.length - i) - 1) / 2);
        }
        return gaps;
    }

    private int[] generateSedgewickGaps(int n) {
        // Sedgewick's sequence: 4^k + 3*2^(k-1) + 1
        java.util.ArrayList<Integer> gapList = new java.util.ArrayList<>();
        int k = 0;
        int gap;

        do {
            if (k == 0) {
                gap = 1;
            } else {
                gap = (int) (Math.pow(4, k) + 3 * Math.pow(2, k - 1) + 1);
            }

            if (gap < n) {
                gapList.add(gap);
            }
            k++;
        } while (gap < n);

        // Reverse to get descending order
        int[] gaps = new int[gapList.size()];
        for (int i = 0; i < gaps.length; i++) {
            gaps[i] = gapList.get(gapList.size() - 1 - i);
        }
        return gaps;
    }
}