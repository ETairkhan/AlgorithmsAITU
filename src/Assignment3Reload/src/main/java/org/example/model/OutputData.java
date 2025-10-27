package org.example.model;

import java.util.List;

public class OutputData {
    private List<GraphResult> results;

    public OutputData() {}

    public OutputData(List<GraphResult> results) {
        this.results = results;
    }

    // Getters and Setters
    public List<GraphResult> getResults() { return results; }
    public void setResults(List<GraphResult> results) { this.results = results; }
}