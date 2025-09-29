package Assignment2.test;

import Assignment2.metrics.PerformanceTracker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import Assignment2.algorithms.*;
import Assignment2.metrics.*;
import java.util.stream.Stream;
import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ShellSortTest {
    private PerformanceTracker tracker;
    private ShellSort shellSort;

    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
        shellSort = new ShellSort(tracker);
    }

    @Test
    @DisplayName("Should throw exception for null input")
    void testNullInput() {
        assertThrows(IllegalArgumentException.class,
                () -> shellSort.sort(null, "shell"));
    }

    @Test
    @DisplayName("Should throw exception for invalid gap sequence")
    void testInvalidGapSequence() {
        int[] input = {3, 1, 4};
        assertThrows(IllegalArgumentException.class,
                () -> shellSort.sort(input, "invalid"));
    }

    @Test
    @DisplayName("Should handle empty array")
    void testEmptyArray() {
        int[] input = {};
        assertDoesNotThrow(() -> shellSort.sort(input, "shell"));
        assertEquals(0, input.length);
        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getSwaps());
    }

    @Test
    @DisplayName("Should handle single element array")
    void testSingleElement() {
        int[] input = {5};
        shellSort.sort(input, "shell");
        assertArrayEquals(new int[]{5}, input);
        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getSwaps());
    }

    @ParameterizedTest
    @ValueSource(strings = {"shell", "knuth", "sedgewick"})
    @DisplayName("Should sort already sorted array with different gap sequences")
    void testSortedArray(String gapSequence) {
        int[] input = {1, 2, 3, 4, 5};
        int[] expected = {1, 2, 3, 4, 5};
        shellSort.sort(input, gapSequence);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }


    @ParameterizedTest
    @ValueSource(strings = {"shell", "knuth", "sedgewick"})
    @DisplayName("Should sort array with duplicates with different gap sequences")
    void testArrayWithDuplicates(String gapSequence) {
        int[] input = {3, 1, 4, 1, 5, 9, 2, 6, 5};
        int[] expected = {1, 1, 2, 3, 4, 5, 5, 6, 9};
        shellSort.sort(input, gapSequence);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"shell", "knuth", "sedgewick"})
    @DisplayName("Should sort array with negative numbers with different gap sequences")
    void testArrayWithNegativeNumbers(String gapSequence) {
        int[] input = {-3, -1, -4, 0, 2, -2};
        int[] expected = {-4, -3, -2, -1, 0, 2};
        shellSort.sort(input, gapSequence);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @ParameterizedTest
    @ValueSource(strings = {"shell", "knuth", "sedgewick"})
    @DisplayName("Should sort array with all equal elements with different gap sequences")
    void testArrayWithAllEqualElements(String gapSequence) {
        int[] input = {7, 7, 7, 7, 7};
        int[] expected = {7, 7, 7, 7, 7};
        shellSort.sort(input, gapSequence);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @ParameterizedTest
    @MethodSource("provideRandomArrays")
    @DisplayName("Should sort random arrays correctly with different gap sequences")
    void testRandomArrays(int[] input, String gapSequence) {
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        shellSort.sort(input, gapSequence);
        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should verify gap sequence generation for Shell's sequence")
    void testShellGapSequence() {
        int[] gaps = shellSort.generateShellGaps(16);
        int[] expected = {8, 4, 2, 1};
        assertArrayEquals(expected, gaps);
    }

    @Test
    @DisplayName("Should verify gap sequence generation for Knuth's sequence")
    void testKnuthGapSequence() {
        int[] gaps = shellSort.generateKnuthGaps(100);
        // Knuth sequence for n=100: 40, 13, 4, 1
        assertTrue(gaps.length > 0);
        assertTrue(gaps[0] >= gaps[gaps.length - 1]); // Should be descending
        for (int gap : gaps) {
            assertTrue(gap > 0);
        }
    }

    @Test
    @DisplayName("Should verify gap sequence generation for Sedgewick's sequence")
    void testSedgewickGapSequence() {
        int[] gaps = shellSort.generateSedgewickGaps(100);
        assertTrue(gaps.length > 0);
        assertTrue(gaps[0] >= gaps[gaps.length - 1]); // Should be descending
        for (int gap : gaps) {
            assertTrue(gap > 0);
        }
    }

    @Test
    @DisplayName("Should track performance metrics correctly")
    void testPerformanceMetrics() {
        int[] input = {5, 3, 8, 1, 2};
        tracker.reset();

        shellSort.sort(input, "shell");

        assertTrue(tracker.getComparisons() > 0, "Should record comparisons");
        assertTrue(tracker.getSwaps() > 0, "Should record swaps");
        assertTrue(tracker.getArrayAccesses() > 0, "Should record array accesses");
        assertTrue(tracker.getElapsedTime() >= 0, "Should record elapsed time");
    }

    @Test
    @DisplayName("Should handle large array efficiently")
    void testLargeArray() {
        int[] input = generateRandomArray(1000);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        shellSort.sort(input, "knuth");

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle worst-case scenario")
    void testWorstCaseScenario() {
        // Reverse sorted array is often worst case for Shell Sort
        int[] input = generateReverseSortedArray(100);
        int[] expected = Arrays.copyOf(input, input.length);
        Arrays.sort(expected);

        shellSort.sort(input, "sedgewick");

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should handle best-case scenario")
    void testBestCaseScenario() {
        // Already sorted array is best case
        int[] input = generateSortedArray(100);
        int[] expected = Arrays.copyOf(input, input.length);

        shellSort.sort(input, "shell");

        assertArrayEquals(expected, input);
        assertTrue(isSorted(input));
    }

    @Test
    @DisplayName("Should compare different gap sequences performance")
    void testGapSequenceComparison() {
        int[] input = generateRandomArray(500);
        int[] copy1 = Arrays.copyOf(input, input.length);
        int[] copy2 = Arrays.copyOf(input, input.length);
        int[] copy3 = Arrays.copyOf(input, input.length);

        PerformanceTracker tracker1 = new PerformanceTracker();
        ShellSort shell1 = new ShellSort(tracker1);
        shell1.sort(copy1, "shell");

        PerformanceTracker tracker2 = new PerformanceTracker();
        ShellSort shell2 = new ShellSort(tracker2);
        shell2.sort(copy2, "knuth");

        PerformanceTracker tracker3 = new PerformanceTracker();
        ShellSort shell3 = new ShellSort(tracker3);
        shell3.sort(copy3, "sedgewick");

        assertTrue(isSorted(copy1));
        assertTrue(isSorted(copy2));
        assertTrue(isSorted(copy3));

        // All should produce the same sorted result
        assertArrayEquals(copy1, copy2);
        assertArrayEquals(copy2, copy3);
    }

    private static Stream<Arguments> provideRandomArrays() {
        Random random = new Random(42);
        return Stream.of(
                Arguments.of(generateRandomArray(10, random), "shell"),
                Arguments.of(generateRandomArray(50, random), "knuth"),
                Arguments.of(generateRandomArray(100, random), "sedgewick"),
                Arguments.of(generateRandomArray(20, random), "shell"),
                Arguments.of(generateRandomArray(75, random), "knuth")
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