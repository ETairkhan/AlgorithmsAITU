package org.example.model;

import java.util.List;

public class AlgorithmResult {
    private List<Edge> mstEdges;
    private int totalCost;
    private int operationsCount;
    private double executionTimeMs;

    public AlgorithmResult() {}

    public AlgorithmResult(List<Edge> mstEdges, int totalCost, int operationsCount, double executionTimeMs) {
        this.mstEdges = mstEdges;
        this.totalCost = totalCost;
        this.operationsCount = operationsCount;
        this.executionTimeMs = executionTimeMs;
    }

    // Getters and Setters
    public List<Edge> getMstEdges() { return mstEdges; }
    public void setMstEdges(List<Edge> mstEdges) { this.mstEdges = mstEdges; }

    public int getTotalCost() { return totalCost; }
    public void setTotalCost(int totalCost) { this.totalCost = totalCost; }

    public int getOperationsCount() { return operationsCount; }
    public void setOperationsCount(int operationsCount) { this.operationsCount = operationsCount; }

    public double getExecutionTimeMs() { return executionTimeMs; }
    public void setExecutionTimeMs(double executionTimeMs) { this.executionTimeMs = executionTimeMs; }
}