package Assignment2.metrics;

public class PerformanceTracker {
    private long comparisons, swaps, arrayAccesses, elapsedTime;
    private long startTime;

    public void recordComparison() { comparisons++; }
    public void recordSwap() { swaps++; }
    public void recordArrayAccess() { arrayAccesses++; }
    public void recordArrayAccesses(long n) {
        if (n < 0) throw new IllegalArgumentException("Negative count");
        arrayAccesses += n;
    }
    public void startTimer() { startTime = System.nanoTime(); }
    public void stopTimer() { elapsedTime += System.nanoTime() - startTime; }
    public void reset() {
        comparisons = swaps = arrayAccesses = elapsedTime = 0;
    }

    // getters...
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getElapsedTime() { return elapsedTime; }

    public String getMetrics() {
        return String.format(
                "Comparisons: %d, Swaps: %d, Array Accesses: %d, Time: %d ns",
                comparisons, swaps, arrayAccesses, elapsedTime);
    }
}
