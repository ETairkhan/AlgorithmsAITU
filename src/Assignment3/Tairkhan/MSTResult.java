package Assignment3.Tairkhan;

import java.util.List;

class MSTResult {
    private List<Edge> mstEdges;
    private int totalCost;
    private int operationsCount;
    private double executionTimeMs;

    public MSTResult(List<Edge> mstEdges, int totalCost, int operationsCount, double executionTimeMs) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operationsCount = operationsCount;
        this.executionTimeMs = executionTimeMs;
    }

    // Getters
    public List<Edge> getMstEdges() { return mstEdges; }
    public int getTotalCost() { return totalCost; }
    public int getOperationsCount() { return operationsCount; }
    public double getExecutionTimeMs() { return executionTimeMs; }
}