package Assignment2.StudentA.cli;

import Assignment2.StudentA.algorithms.*;
import Assignment2.StudentA.metrics.PerformanceTracker;

import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    public static void runBenchmarks(String algo, int size) {
        int[] data = generateRandomArray(size);
        PerformanceTracker tracker = new PerformanceTracker();

        switch (algo.toLowerCase()) {
            case "insertion":
                benchmarkInsertionSort(data, tracker);
                break;
            case "shell":
                benchmarkShellSort(data, tracker);
                break;
            case "boyer-moore":
                benchmarkBoyerMoore(data, tracker);
                break;
            case "minheap":
                benchmarkMinHeap(data, tracker);
                break;
            default:
                System.out.println("Unknown algorithm: " + algo);
        }
    }

    public static void runAllBenchmarks() {
        int[] sizes = {100, 1000, 10000};
        String[] algorithms = {"insertion", "shell", "boyer-moore", "minheap"};

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

    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) arr[i] = rand.nextInt(size * 10);
        return arr;
    }

    private static void benchmarkInsertionSort(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Insertion Sort with n=" + data.length);

        int[] copy = Arrays.copyOf(data, data.length);
        tracker.reset();
        tracker.startTimer();
        InsertionSort.sort(copy, tracker);
        tracker.stopTimer();

        System.out.println("Insertion Sort Results: " + tracker.getMetrics());
        System.out.println("Sorted correctly: " + isSorted(copy));
    }

    private static void benchmarkShellSort(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Shell Sort with n=" + data.length);

        int[] copy = Arrays.copyOf(data, data.length);
        tracker.reset();
        tracker.startTimer();
        ShellSort.sort(copy, tracker, "knuth");
        tracker.stopTimer();

        System.out.println("Shell Sort Results: " + tracker.getMetrics());
        System.out.println("Sorted correctly: " + isSorted(copy));
    }

    private static void benchmarkBoyerMoore(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Boyer-Moore Majority with n=" + data.length);

        tracker.reset();
        tracker.startTimer();
        Integer majority = BoyerMooreMajority.findMajority(data, tracker);
        tracker.stopTimer();

        System.out.println("Boyer-Moore Results: " + tracker.getMetrics());
        System.out.println("Majority element: " + majority);
    }

    private static void benchmarkMinHeap(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Min-Heap Operations with n=" + data.length);

        tracker.reset();
        tracker.startTimer();
        MinHeap heap = new MinHeap();
        for (int x : data) heap.insert(x);
        while (!heap.isEmpty()) heap.extractMin();
        tracker.stopTimer();

        System.out.println("MinHeap Results: " + tracker.getMetrics());
    }

    private static boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) return false;
        }
        return true;
    }
}
