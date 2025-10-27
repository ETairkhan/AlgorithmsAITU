package Assignment3.Tairkhan;
import java.util.*;

import Assignment3.Tairkhan.*;

class KruskalAlgorithm {
    private List<Edge> edges;
    private List<String> nodes;
    private int operations;

    public KruskalAlgorithm(Graph graph) {
        this.edges = new ArrayList<>(graph.getEdges());
        this.nodes = graph.getNodes();
        this.operations = 0;
    }

    public MSTResult findMST() {
        long startTime = System.nanoTime();
        List<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;
        operations = 0;

        // Sort edges by weight
        Collections.sort(edges);
        operations += edges.size() * (int) Math.log(edges.size()); // Approximate sort operations

        UnionFind uf = new UnionFind(nodes);

        for (Edge edge : edges) {
            operations++; // For loop iteration

            if (mstEdges.size() == nodes.size() - 1) {
                operations++; // Break condition check
                break;
            }

            if (uf.union(edge.getFrom(), edge.getTo())) {
                mstEdges.add(edge);
                totalCost += edge.getWeight();
                operations += 2; // Add to mst and cost addition
            }
            operations++; // Union result check
        }

        // Add union-find operations
        operations += uf.getOperations();

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;

        return new MSTResult(mstEdges, totalCost, operations, executionTimeMs);
    }
}