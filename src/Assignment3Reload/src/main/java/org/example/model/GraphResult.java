package org.example.model;

public class GraphResult {
    private int graphId;
    private InputStats inputStats;
    private AlgorithmResult prim;
    private AlgorithmResult kruskal;

    public GraphResult() {}

    public GraphResult(int graphId, InputStats inputStats, AlgorithmResult prim, AlgorithmResult kruskal) {
        this.graphId = graphId;
        this.inputStats = inputStats;
        this.prim = prim;
        this.kruskal = kruskal;
    }

    // Getters and Setters
    public int getGraphId() { return graphId; }
    public void setGraphId(int graphId) { this.graphId = graphId; }

    public InputStats getInputStats() { return inputStats; }
    public void setInputStats(InputStats inputStats) { this.inputStats = inputStats; }

    public AlgorithmResult getPrim() { return prim; }
    public void setPrim(AlgorithmResult prim) { this.prim = prim; }

    public AlgorithmResult getKruskal() { return kruskal; }
    public void setKruskal(AlgorithmResult kruskal) { this.kruskal = kruskal; }
}