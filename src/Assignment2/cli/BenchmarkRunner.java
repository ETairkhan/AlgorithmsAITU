package Assignment2.cli;

import Assignment2.algorithms.*;
import Assignment2.metrics.PerformanceTracker;
import java.util.Random;
import java.util.Arrays;

public class BenchmarkRunner {

    public static void runBenchmarks(String algorithm, int size) {
        int[] data = generateRandomArray(size);
        PerformanceTracker tracker = new PerformanceTracker();

        switch (algorithm.toLowerCase()) {
            case "shell":
                benchmarkShellSort(data, tracker);
                break;
            case "heap":
                benchmarkHeapSort(data, tracker);
                break;
            default:
                System.out.println("Unknown algorithm: " + algorithm);
        }
    }

    public static void runAllBenchmarks() {
        int[] sizes = {100, 1000, 10000, 100000};
        String[] algorithms = {"shell", "heap"}; // Only shell and heap

        for (String algorithm : algorithms) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("BENCHMARKING: " + algorithm.toUpperCase());
            System.out.println("=".repeat(50));

            for (int size : sizes) {
                System.out.println("\n--- Size: " + size + " ---");
                runBenchmarks(algorithm, size);
            }
        }
    }

    private static void benchmarkShellSort(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Shell Sort with n=" + data.length);

        int[] copy = Arrays.copyOf(data, data.length);
        ShellSort sorter = new ShellSort(tracker);
        tracker.reset();
        tracker.startTimer();
        sorter.sort(copy, "knuth"); // Using Knuth's sequence
        tracker.stopTimer();

        System.out.println("Shell Sort Results: " + tracker.getMetrics());
        System.out.println("Sorted correctly: " + isSorted(copy));
    }

    private static void benchmarkHeapSort(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Heap Sort with n=" + data.length);

        int[] dataCopy = data.clone();
        HeapSort sorter = new HeapSort(tracker);
        tracker.reset();
        tracker.startTimer();
        sorter.sort(dataCopy);
        tracker.stopTimer();

        System.out.println("Heap Sort Results: " + tracker.getMetrics());
        System.out.println("Sorted correctly: " + isSorted(dataCopy));
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000) - 500; // Values between -500 and 499
        }
        return arr;
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}