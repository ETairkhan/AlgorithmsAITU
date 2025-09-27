package Assignment2.StudentB.metrics;

public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long startTime;
    private long endTime;

    public PerformanceTracker() {
        reset();
    }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        startTime = 0;
        endTime = 0;
    }

    public void startTimer() { startTime = System.nanoTime(); }
    public void stopTimer() { endTime = System.nanoTime(); }

    public void recordComparison() { comparisons++; }
    public void recordSwap() { swaps++; }
    public void recordArrayAccess() { arrayAccesses++; }
    public void recordArrayAccesses(int count) { arrayAccesses += count; }

    // Getters
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getElapsedTime() { return endTime - startTime; }

    public String getMetrics() {
        return String.format("Comparisons: %d, Swaps: %d, Array Accesses: %d, Time: %d ns",
                comparisons, swaps, arrayAccesses, getElapsedTime());
    }
}