package org.example.graph.dagsp;

import org.example.graph.Metrics;
import java.util.*;

public class DAGShortestPath {
    private Map<Integer, List<Edge>> graph;
    private int n;
    private Metrics metrics;

    public static class Edge {
        public int v;
        public int weight;

        public Edge(int v, int weight) {
            this.v = v;
            this.weight = weight;
        }
    }

    public DAGShortestPath(Map<Integer, List<Edge>> graph, int n, Metrics metrics) {
        this.graph = graph;
        this.n = n;
        this.metrics = metrics;
    }

    public int[] shortestPaths(List<Integer> topoOrder, int source) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[source] = 0;

        for (int u : topoOrder) {
            metrics.incrementQueueOperations();
            if (dist[u] != Integer.MAX_VALUE && graph.containsKey(u)) {
                for (Edge edge : graph.get(u)) {
                    metrics.incrementRelaxOperations();
                    int newDist = dist[u] + edge.weight;
                    if (newDist < dist[edge.v]) {
                        dist[edge.v] = newDist;
                    }
                }
            }
        }

        return dist;
    }

    public int[] longestPaths(List<Integer> topoOrder, int source) {
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[source] = 0;

        for (int u : topoOrder) {
            metrics.incrementQueueOperations();
            if (dist[u] != Integer.MIN_VALUE && graph.containsKey(u)) {
                for (Edge edge : graph.get(u)) {
                    metrics.incrementRelaxOperations();
                    int newDist = dist[u] + edge.weight;
                    if (newDist > dist[edge.v]) {
                        dist[edge.v] = newDist;
                    }
                }
            }
        }

        return dist;
    }

    public CriticalPathResult findCriticalPath(List<Integer> topoOrder, int source) {
        int[] dist = longestPaths(topoOrder, source);

        // Find the maximum distance and corresponding node
        int maxDist = Integer.MIN_VALUE;
        int endNode = -1;
        for (int i = 0; i < n; i++) {
            if (dist[i] > maxDist && dist[i] != Integer.MIN_VALUE) {
                maxDist = dist[i];
                endNode = i;
            }
        }

        // Reconstruct the critical path
        List<Integer> path = reconstructPath(dist, endNode);

        return new CriticalPathResult(path, maxDist);
    }

    private List<Integer> reconstructPath(int[] dist, int endNode) {
        List<Integer> path = new ArrayList<>();
        if (endNode == -1) return path;

        // We need to reconstruct from end to start
        // Since we don't have parent pointers, we'll work backwards
        int current = endNode;
        path.add(current);

        // Keep going until we reach a node with distance 0 (source)
        while (dist[current] > 0) {
            boolean found = false;

            // Look for a node u that has an edge to current and dist[u] + weight = dist[current]
            for (int u = 0; u < n; u++) {
                if (graph.containsKey(u)) {
                    for (Edge edge : graph.get(u)) {
                        if (edge.v == current && dist[u] != Integer.MIN_VALUE &&
                                dist[current] == dist[u] + edge.weight) {
                            path.add(u);
                            current = u;
                            found = true;
                            break;
                        }
                    }
                }
                if (found) break;
            }

            if (!found) break;
        }

        // Reverse to get path from source to end
        Collections.reverse(path);
        return path;
    }

    public static class CriticalPathResult {
        public List<Integer> path;
        public int length;

        public CriticalPathResult(List<Integer> path, int length) {
            this.path = path;
            this.length = length;
        }
    }
}