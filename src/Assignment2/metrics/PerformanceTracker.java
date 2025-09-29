package Assignment2.metrics;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

class PerformanceTrackerTest {
    private PerformanceTracker tracker;

    @BeforeEach
    void setUp() {
        tracker = new PerformanceTracker();
    }

    @Test
    @DisplayName("Should initialize with zero values")
    void testInitialization() {
        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getSwaps());
        assertEquals(0, tracker.getArrayAccesses());
        assertEquals(0, tracker.getElapsedTime());
    }

    @Test
    @DisplayName("Should reset all metrics to zero")
    void testReset() {
        // Record some activity
        tracker.recordComparison();
        tracker.recordSwap();
        tracker.recordArrayAccess();
        tracker.startTimer();
        try { Thread.sleep(10); } catch (InterruptedException e) {}
        tracker.stopTimer();

        tracker.reset();

        assertEquals(0, tracker.getComparisons());
        assertEquals(0, tracker.getSwaps());
        assertEquals(0, tracker.getArrayAccesses());
        assertEquals(0, tracker.getElapsedTime());
    }

    @Test
    @DisplayName("Should record comparisons correctly")
    void testRecordComparison() {
        tracker.recordComparison();
        tracker.recordComparison();
        tracker.recordComparison();

        assertEquals(3, tracker.getComparisons());
    }

    @Test
    @DisplayName("Should record swaps correctly")
    void testRecordSwap() {
        tracker.recordSwap();
        tracker.recordSwap();

        assertEquals(2, tracker.getSwaps());
    }

    @Test
    @DisplayName("Should record array accesses correctly")
    void testRecordArrayAccess() {
        tracker.recordArrayAccess();
        tracker.recordArrayAccess();
        tracker.recordArrayAccess();
        tracker.recordArrayAccess();

        assertEquals(4, tracker.getArrayAccesses());
    }

    @Test
    @DisplayName("Should record multiple array accesses at once")
    void testRecordArrayAccesses() {
        tracker.recordArrayAccesses(5);
        tracker.recordArrayAccesses(3);

        assertEquals(8, tracker.getArrayAccesses());
    }

    @Test
    @DisplayName("Should measure elapsed time correctly")
    void testElapsedTime() throws InterruptedException {
        tracker.startTimer();
        Thread.sleep(50); // Sleep for 50ms
        tracker.stopTimer();

        long elapsed = tracker.getElapsedTime();
        assertTrue(elapsed >= 50_000_000, "Elapsed time should be at least 50ms");
        assertTrue(elapsed < 100_000_000, "Elapsed time should be reasonable");
    }

    @Test
    @DisplayName("Should provide formatted metrics string")
    void testGetMetrics() {
        tracker.recordComparison();
        tracker.recordSwap();
        tracker.recordArrayAccess();
        tracker.startTimer();
        tracker.stopTimer();

        String metrics = tracker.getMetrics();
        assertTrue(metrics.contains("Comparisons: 1"));
        assertTrue(metrics.contains("Swaps: 1"));
        assertTrue(metrics.contains("Array Accesses: 1"));
        assertTrue(metrics.contains("Time:"));
    }

    @Test
    @DisplayName("Should handle timer without start")
    void testTimerWithoutStart() {
        tracker.stopTimer();
        assertEquals(0, tracker.getElapsedTime());
    }

    @Test
    @DisplayName("Should handle multiple start/stop cycles")
    void testMultipleTimerCycles() {
        tracker.startTimer();
        tracker.stopTimer();
        long firstTime = tracker.getElapsedTime();

        tracker.startTimer();
        tracker.stopTimer();
        long secondTime = tracker.getElapsedTime();

        assertTrue(firstTime >= 0);
        assertTrue(secondTime >= 0);
    }

    @Test
    @DisplayName("Should handle negative array access count gracefully")
    void testNegativeArrayAccesses() {
        assertThrows(IllegalArgumentException.class, () -> tracker.recordArrayAccesses(-1));
    }
}