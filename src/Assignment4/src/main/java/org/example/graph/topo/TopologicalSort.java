package org.example.graph.topo;

import org.example.graph.Metrics;
import java.util.*;

public class TopologicalSort {
    private Map<Integer, List<Integer>> graph;
    private int n;
    private Metrics metrics;

    public TopologicalSort(Map<Integer, List<Integer>> graph, int n, Metrics metrics) {
        this.graph = graph;
        this.n = n;
        this.metrics = metrics;
    }

    public List<Integer> kahnTopologicalSort() {
        // Calculate the actual size needed based on graph content
        int maxNode = n - 1;
        if (!graph.isEmpty()) {
            maxNode = Math.max(maxNode,
                    graph.keySet().stream().mapToInt(Integer::intValue).max().getAsInt());
            for (List<Integer> neighbors : graph.values()) {
                if (!neighbors.isEmpty()) {
                    maxNode = Math.max(maxNode, neighbors.stream().mapToInt(Integer::intValue).max().getAsInt());
                }
            }
        }
        int actualSize = maxNode + 1;
        int[] inDegree = new int[actualSize];

        // Calculate in-degrees
        for (int u : graph.keySet()) {
            for (int v : graph.get(u)) {
                if (v < actualSize) {
                    inDegree[v]++;
                    metrics.incrementEdgeTraversals();
                }
            }
        }

        // Initialize queue with nodes having 0 in-degree
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < actualSize; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementQueueOperations();
            }
        }

        List<Integer> topoOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementQueueOperations();
            topoOrder.add(u);

            if (graph.containsKey(u)) {
                for (int v : graph.get(u)) {
                    if (v < actualSize) {
                        inDegree[v]--;
                        metrics.incrementEdgeTraversals();
                        if (inDegree[v] == 0) {
                            queue.offer(v);
                            metrics.incrementQueueOperations();
                        }
                    }
                }
            }
        }

        return topoOrder;
    }

    public List<Integer> dfsTopologicalSort() {
        boolean[] visited = new boolean[n];
        boolean[] tempMark = new boolean[n]; // for cycle detection
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                if (!dfs(i, visited, tempMark, stack)) {
                    throw new IllegalArgumentException("Graph has cycles - cannot perform topological sort");
                }
            }
        }

        List<Integer> result = new ArrayList<>();
        while (!stack.isEmpty()) {
            result.add(stack.pop());
        }
        return result;
    }

    private boolean dfs(int u, boolean[] visited, boolean[] tempMark, Deque<Integer> stack) {
        if (tempMark[u]) {
            return false; // Cycle detected
        }

        if (visited[u]) {
            return true;
        }

        visited[u] = true;
        tempMark[u] = true;
        metrics.incrementDfsVisits();

        if (graph.containsKey(u)) {
            for (int v : graph.get(u)) {
                metrics.incrementEdgeTraversals();
                if (v < n && !dfs(v, visited, tempMark, stack)) {
                    return false;
                }
            }
        }

        tempMark[u] = false;
        stack.push(u);
        metrics.incrementQueueOperations();
        return true;
    }
}