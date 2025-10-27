package org.example.algorithms;

import org.example.model.Edge;
import org.example.model.Graph;

import java.util.*;

public class KruskalAlgorithm {
    private int operationsCount;
    private Map<String, String> parent;
    private Map<String, Integer> rank;

    public KruskalAlgorithm() {
        this.operationsCount = 0;
    }

    public List<Edge> findMST(Graph graph) {
        operationsCount = 0;
        List<Edge> mst = new ArrayList<>();
        List<Edge> edges = new ArrayList<>(graph.getEdges());

        // Sort edges by weight
        edges.sort(Comparator.comparingInt(Edge::getWeight));
        operationsCount += edges.size() * (int)(Math.log(edges.size())); // Approximation for sort operations

        initializeUnionFind(graph.getNodes());

        for (Edge edge : edges) {
            operationsCount++;
            String rootFrom = find(edge.getFrom());
            String rootTo = find(edge.getTo());
            operationsCount += 2;

            if (!rootFrom.equals(rootTo)) {
                mst.add(edge);
                union(edge.getFrom(), edge.getTo());
                operationsCount++;
            }

            if (mst.size() == graph.getNodes().size() - 1) {
                break;
            }
        }

        return mst;
    }

    private void initializeUnionFind(List<String> nodes) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        for (String node : nodes) {
            parent.put(node, node);
            rank.put(node, 0);
            operationsCount += 2;
        }
    }

    private String find(String node) {
        operationsCount++;
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node)));
            operationsCount++;
        }
        return parent.get(node);
    }

    private void union(String node1, String node2) {
        String root1 = find(node1);
        String root2 = find(node2);
        operationsCount += 2;

        if (!root1.equals(root2)) {
            if (rank.get(root1) < rank.get(root2)) {
                parent.put(root1, root2);
            } else if (rank.get(root1) > rank.get(root2)) {
                parent.put(root2, root1);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank.get(root1) + 1);
                operationsCount++;
            }
            operationsCount += 2;
        }
    }

    public int getTotalCost(List<Edge> mst) {
        return mst.stream().mapToInt(Edge::getWeight).sum();
    }

    public int getOperationsCount() {
        return operationsCount;
    }
}