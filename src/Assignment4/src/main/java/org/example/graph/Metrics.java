package org.example.graph;

public class Metrics {
    private long startTime;
    private long endTime;
    private int dfsVisits;
    private int edgeTraversals;
    private int queueOperations;
    private int relaxOperations;

    public Metrics() {
        reset();
    }

    public void reset() {
        dfsVisits = 0;
        edgeTraversals = 0;
        queueOperations = 0;
        relaxOperations = 0;
    }

    public void startTimer() {
        startTime = System.nanoTime();
    }

    public void stopTimer() {
        endTime = System.nanoTime();
    }

    public long getElapsedTime() {
        return endTime - startTime;
    }

    // Getters and setters
    public void incrementDfsVisits() { dfsVisits++; }
    public void incrementEdgeTraversals() { edgeTraversals++; }
    public void incrementQueueOperations() { queueOperations++; }
    public void incrementRelaxOperations() { relaxOperations++; }

    public int getDfsVisits() { return dfsVisits; }
    public int getEdgeTraversals() { return edgeTraversals; }
    public int getQueueOperations() { return queueOperations; }
    public int getRelaxOperations() { return relaxOperations; }
}