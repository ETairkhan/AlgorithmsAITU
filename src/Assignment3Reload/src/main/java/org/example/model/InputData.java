package org.example.model;

import java.util.List;

public class InputData {
    private List<Graph> graphs;

    public InputData() {}

    public InputData(List<Graph> graphs) {
        this.graphs = graphs;
    }

    // Getters and Setters
    public List<Graph> getGraphs() { return graphs; }
    public void setGraphs(List<Graph> graphs) { this.graphs = graphs; }
}