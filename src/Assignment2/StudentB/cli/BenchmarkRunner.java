package Assignment2.StudentB.cli;

import Assignment2.StudentB.algorithms.*;
import Assignment2.StudentB.metrics.PerformanceTracker;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkRunner {

    public static void runBenchmarks(String algorithm, int size) {
        int[] data = generateRandomArray(size);
        PerformanceTracker tracker = new PerformanceTracker();

        switch (algorithm.toLowerCase()) {
            case "selection":
                benchmarkSelectionSort(data, tracker);
                break;
            case "heap":
                benchmarkHeapSort(data, tracker);
                break;
            case "kadane":
                benchmarkKadane(data, tracker);
                break;
            case "maxheap":
                benchmarkMaxHeap(data, tracker);
                break;
            default:
                System.out.println("Unknown algorithm: " + algorithm);
        }
    }

    public static void runAllBenchmarks() {
        int[] sizes = {100, 1000, 10000, 100000};
        String[] algorithms = {"selection", "heap", "kadane", "maxheap"};

        for (String algorithm : algorithms) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("BENCHMARKING: " + algorithm.toUpperCase());
            System.out.println("=".repeat(50));

            for (int size : sizes) {
                if (algorithm.equals("selection") && size > 10000) {
                    System.out.println("Skipping selection sort for large size: " + size);
                    continue;
                }

                System.out.println("\n--- Size: " + size + " ---");
                runBenchmarks(algorithm, size);
            }
        }
    }

    private static int[] generateRandomArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000) - 500; // Values between -500 and 499
        }
        return arr;
    }

    private static int[] generatePositiveArray(int size) {
        Random rand = new Random();
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000); // Values between 0 and 999
        }
        return arr;
    }

    private static void benchmarkSelectionSort(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Selection Sort with n=" + data.length);

        SelectionSort sorter = new SelectionSort(tracker);
        tracker.reset();
        tracker.startTimer();
        int[] result = sorter.sort(data);
        tracker.stopTimer();

        System.out.println("Selection Sort Results: " + tracker.getMetrics());
        System.out.println("Sorted correctly: " + isSorted(result));
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

    private static void benchmarkKadane(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Kadane's Algorithm with n=" + data.length);

        KadaneAlgorithm kadane = new KadaneAlgorithm(tracker);
        tracker.reset();
        tracker.startTimer();
        KadaneAlgorithm.Result result = kadane.findMaximumSubarray(data);
        tracker.stopTimer();

        System.out.println("Kadane's Algorithm Results: " + tracker.getMetrics());
        System.out.println("Maximum subarray sum: " + result.maxSum);
        System.out.println("Subarray indices: [" + result.start + " to " + result.end + "]");
    }

    private static void benchmarkMaxHeap(int[] data, PerformanceTracker tracker) {
        System.out.println("Benchmarking Max-Heap Operations with n=" + data.length);

        // Test build heap
        tracker.reset();
        tracker.startTimer();
        MaxHeap heap = new MaxHeap(data, tracker);
        tracker.stopTimer();

        System.out.println("Build Heap - Time: " + tracker.getElapsedTime() + " ns");

        // Test extract max operations
        tracker.reset();
        tracker.startTimer();
        int extractCount = Math.min(10, data.length);
        for (int i = 0; i < extractCount; i++) {
            heap.extractMax();
        }
        tracker.stopTimer();

        System.out.println("Extract Max x" + extractCount + " - Time: " + tracker.getElapsedTime() + " ns");
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