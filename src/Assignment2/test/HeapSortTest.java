package Assignment2.test;

import Assignment2.metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class HeapSortTest {
    private PerformanceTracker tracker;
    private HeapSort heapSort;

    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
        heapSort = new HeapSort(tracker);
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void testNullInput() {
        assertThrows(IllegalArgumentException.class, () -> heapSort.sort(null));
    }

    @Test
    @DisplayName("Should handle empty array")
    void testEmptyArray() {
        int[] input = {};
        assertDoesNotThrow(() -> heapSort.sort(input));
        assertEquals(0, input.length);
        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getSwaps());
    }

    @Test
    @DisplayName("Should handle single element array")
    void testSingleElement() {
        int[] input = {5};
        heapSort.sort(input);
        assertArrayEquals(new int[]{5}, input);
        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getSwaps());
    }

    @Test
    @DisplayName("Should sort already sorted array")
    void testSortedArray() {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        heapSort.sort(input);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should sort reverse sorted array")
    void testReverseSortedArray() {
        int[] input = {5, 4, 3, 2, 1};
        int[] expected = {1, 2, 3, 4, 5};
        heapSort.sort(input);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should sort array with duplicates")
    void testArrayWithDuplicates() {
        int[] input = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        int[] expected = {1, 1, 2, 3, 4, 5, 5, 6, 9};
        heapSort.sort(input);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should sort array with negative numbers")
    void testArrayWithNegativeNumbers() {
        int[] input = {-3, -1, -4, 0, 2, -2};
        int[] expected = {-4, -3, -2, -1, 0, 2};
        heapSort.sort(input);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should sort array with all equal elements")
    void testArrayWithAllEqualElements() {
        int[] input = {7, 7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7, 7};
        heapSort.sort(input);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @ParameterizedTest
    @MethodSource("provideRandomArrays")
    @DisplayName("Should sort random arrays correctly")
    void testRandomArrays(int[] input) {
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        heapSort.sort(input);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should build max heap correctly during heap construction")
    void testHeapConstruction() {
        int[] input = {3, 1, 4, 1, 5, 9, 2};
        heapSort.sort(input);

        // After heap sort, array should be completely sorted
        assertTrue(isSorted(input));

        // Verify specific heap property during construction would require
        // access to private heapify method, so we test through final result
    }

    @Test
    @DisplayName("Should track performance metrics correctly")
    void testPerformanceMetrics() {
        int[] input = {5, 3, 8, 1, 2};
        tracker.reset();

        heapSort.sort(input);

        assertTrue(tracker.getComparisons() > 0, "Should record comparisons");
        assertTrue(tracker.getSwaps() > 0, "Should record swaps");
        assertTrue(tracker.getArrayAccesses() > 0, "Should record array accesses");
        assertTrue(tracker.getElapsedTime() >= 0, "Should record elapsed time");
    }

    @Test
    @DisplayName("Should handle large array efficiently")
    void testLargeArray() {
        int[] input = generateRandomArray(10000);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle very large array")
    void testVeryLargeArray() {
        int[] input = generateRandomArray(100000);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle worst-case scenario")
    void testWorstCaseScenario() {
        // For heap sort, worst case is O(n log n) like average case
        // but we can test with reverse sorted array
        int[] input = generateReverseSortedArray(1000);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle best-case scenario")
    void testBestCaseScenario() {
        // For heap sort, best case is also O(n log n)
        // but we can test with already sorted array
        int[] input = generateSortedArray(1000);
        int[] expected = Arrays.copyOf(input, input.length);

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should maintain in-place sorting property")
    void testInPlaceSorting() {
        int[] input = {5, 3, 8, 1, 2};
        int[] originalReference = input; // Keep reference to original array

        heapSort.sort(input);

        // Verify it's the same array reference (in-place)
        assertSame(originalReference, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle array with repeated patterns")
    void testRepeatedPatterns() {
        int[] input = {1, 2, 3, 1, 2, 3, 1, 2, 3};
        int[] expected = {1, 1, 1, 2, 2, 2, 3, 3, 3};

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should verify heap sort stability in terms of performance")
    void testPerformanceConsistency() {
        int[] input1 = generateRandomArray(1000);
        int[] input2 = Arrays.copyOf(input1, input1.length);

        PerformanceTracker tracker1 = new PerformanceTracker();
        HeapSort heapSort1 = new HeapSort(tracker1);
        heapSort1.sort(input1);

        PerformanceTracker tracker2 = new PerformanceTracker();
        HeapSort heapSort2 = new HeapSort(tracker2);
        heapSort2.sort(input2);

        // Should produce same sorted result
        assertArrayEquals(input1, input2);

        // Performance metrics should be similar for same input
        double ratio = (double) tracker1.getComparisons() / tracker2.getComparisons();
        assertTrue(ratio > 0.8 && ratio < 1.2, "Comparison counts should be similar");
    }

    @Test
    @DisplayName("Should handle edge case with two elements")
    void testTwoElements() {
        int[] input = {2, 1};
        int[] expected = {1, 2};

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle edge case with three elements")
    void testThreeElements() {
        int[] input = {3, 1, 2};
        int[] expected = {1, 2, 3};

        heapSort.sort(input);

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should verify O(n log n) behavior through metrics")
    void testTimeComplexityBehavior() {
        // Test that comparisons grow approximately as n log n
        int[] sizes = {100, 1000, 10000};
        long[] comparisons = new long[sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            int[] input = generateRandomArray(sizes[i]);
            PerformanceTracker localTracker = new PerformanceTracker();
            HeapSort localHeapSort = new HeapSort(localTracker);

            localHeapSort.sort(input);
            comparisons[i] = localTracker.getComparisons();
        }

        // Verify that comparisons increase roughly as n log n
        for (int i = 1; i < sizes.length; i++) {
            double sizeRatio = (double) sizes[i] / sizes[i-1];
            double comparisonRatio = (double) comparisons[i] / comparisons[i-1];
            double logNRatio = (sizes[i] * Math.log(sizes[i])) / (sizes[i-1] * Math.log(sizes[i-1]));

            // Allow some tolerance for measurement variation
            assertTrue(Math.abs(comparisonRatio - logNRatio) < 2.0,
                    "Comparisons should grow approximately as n log n");
        }
    }

    private static Stream<Arguments> provideRandomArrays() {
        Random random = new Random(42);
        return Stream.of(
                Arguments.of((Object) generateRandomArray(10, random)),
                Arguments.of((Object) generateRandomArray(50, random)),
                Arguments.of((Object) generateRandomArray(100, random)),
                Arguments.of((Object) generateRandomArray(200, random)),
                Arguments.of((Object) generateRandomArray(500, random))
        );
    }

    private static int[] generateRandomArray(int size, Random random) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = random.nextInt(1000);
        }
        return arr;
    }

    private static int[] generateRandomArray(int size) {
        return generateRandomArray(size, new Random(42));
    }

    private static int[] generateReverseSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = size - i;
        }
        return arr;
    }

    private static int[] generateSortedArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = i;
        }
        return arr;
    }

    private boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
}