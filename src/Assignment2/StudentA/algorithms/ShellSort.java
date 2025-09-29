package Assignment2.StudentA.algorithms;

import Assignment2.StudentA.metrics.PerformanceTracker;

public class ShellSort {

    public static void sort(int[] arr, PerformanceTracker tracker, String gapType) {
        int n = arr.length;
        int[] gaps = getGaps(n, gapType);

        for (int gap : gaps) {
            for (int i = gap; i < n; i++) {
                int temp = arr[i];
                tracker.recordArrayAccess(); // читаем arr[i]
                int j = i;

                while (j >= gap) {
                    tracker.recordComparison();
                    tracker.recordArrayAccesses(2); // доступы arr[j-gap], arr[j]
                    if (arr[j - gap] > temp) {
                        arr[j] = arr[j - gap];
                        tracker.recordSwap();
                        j -= gap;
                    } else {
                        break;
                    }
                }

                arr[j] = temp;
                tracker.recordArrayAccess(); // запись arr[j]
            }
        }
    }

    private static int[] getGaps(int n, String type) {
        switch (type.toLowerCase()) {
            case "knuth":
                int k = 1;
                java.util.List<Integer> knuth = new java.util.ArrayList<>();
                while (k < n) {
                    knuth.add(0, k);
                    k = 3 * k + 1;
                }
                return knuth.stream().mapToInt(Integer::intValue).toArray();

            case "sedgewick":
                java.util.List<Integer> sedgewick = new java.util.ArrayList<>();
                int i = 0;
                while (true) {
                    int gap = (int) (Math.pow(4, i) + 3 * Math.pow(2, i - 1) + 1);
                    if (gap >= n) break;
                    sedgewick.add(0, gap);
                    i++;
                }
                return sedgewick.stream().mapToInt(Integer::intValue).toArray();

            default: // shell original: n/2, n/4, ..., 1
                java.util.List<Integer> shell = new java.util.ArrayList<>();
                for (int gap = n / 2; gap > 0; gap /= 2) {
                    shell.add(gap);
                }
                return shell.stream().mapToInt(Integer::intValue).toArray();
        }
    }
}
