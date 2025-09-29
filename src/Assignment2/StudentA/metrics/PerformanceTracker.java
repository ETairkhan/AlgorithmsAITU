package Assignment2.StudentA.metrics;

public class PerformanceTracker {
    private long comparisons;
    private long swaps;
    private long arrayAccesses;
    private long recursiveCalls;
    private long startTime;
    private long endTime;

    public PerformanceTracker() {
        reset();
    }

    public void reset() {
        comparisons = 0;
        swaps = 0;
        arrayAccesses = 0;
        recursiveCalls = 0;
        startTime = 0;
        endTime = 0;
    }

    public void startTimer() { startTime = System.nanoTime(); }
    public void stopTimer() { endTime = System.nanoTime(); }

    public void recordComparison() { comparisons++; }
    public void recordSwap() { swaps++; }
    public void recordArrayAccess() { arrayAccesses++; }
    public void recordArrayAccesses(int count) { arrayAccesses += count; }
    public void recordRecursiveCall() { recursiveCalls++; }

    // Getters
    public long getComparisons() { return comparisons; }
    public long getSwaps() { return swaps; }
    public long getArrayAccesses() { return arrayAccesses; }
    public long getRecursiveCalls() { return recursiveCalls; }
    public long getElapsedTime() { return endTime - startTime; }

    public String getMetrics() {
        return String.format(
                "Comparisons: %d, Swaps/Shifts: %d, Array Accesses: %d, Recursive Calls: %d, Time: %d ns",
                comparisons, swaps, arrayAccesses, recursiveCalls, getElapsedTime()
        );
    }
}
