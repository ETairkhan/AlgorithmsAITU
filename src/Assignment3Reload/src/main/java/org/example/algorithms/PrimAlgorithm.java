package org.example.algorithms;

import org.example.model.Edge;
import org.example.model.Graph;

import java.util.*;

public class PrimAlgorithm {
    private int operationsCount;

    public PrimAlgorithm() {
        this.operationsCount = 0;
    }

    public List<Edge> findMST(Graph graph) {
        operationsCount = 0;
        List<Edge> mst = new ArrayList<>();
        Set<String> visited = new HashSet<>();
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(Edge::getWeight));

        // Start from first node
        if (!graph.getNodes().isEmpty()) {
            String startNode = graph.getNodes().get(0);
            visited.add(startNode);
            operationsCount++;

            // Add all edges from start node to priority queue
            for (Edge edge : graph.getEdges()) {
                operationsCount++;
                if (edge.getFrom().equals(startNode) || edge.getTo().equals(startNode)) {
                    pq.add(edge);
                    operationsCount++;
                }
            }

            while (!pq.isEmpty() && visited.size() < graph.getNodes().size()) {
                operationsCount++;
                Edge minEdge = pq.poll();
                operationsCount++;

                String nextNode = null;
                if (!visited.contains(minEdge.getFrom())) {
                    nextNode = minEdge.getFrom();
                } else if (!visited.contains(minEdge.getTo())) {
                    nextNode = minEdge.getTo();
                }

                if (nextNode != null) {
                    visited.add(nextNode);
                    mst.add(minEdge);
                    operationsCount += 2;

                    // Add all edges from nextNode to priority queue
                    for (Edge edge : graph.getEdges()) {
                        operationsCount++;
                        if ((edge.getFrom().equals(nextNode) && !visited.contains(edge.getTo())) ||
                                (edge.getTo().equals(nextNode) && !visited.contains(edge.getFrom()))) {
                            pq.add(edge);
                            operationsCount++;
                        }
                    }
                }
            }
        }

        return mst;
    }

    public int getTotalCost(List<Edge> mst) {
        return mst.stream().mapToInt(Edge::getWeight).sum();
    }

    public int getOperationsCount() {
        return operationsCount;
    }
}