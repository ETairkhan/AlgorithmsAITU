package org.example.graph.scc;

import org.example.graph.Metrics;
import java.util.*;

public class SCC {
    private Map<Integer, List<Integer>> graph;
    private Map<Integer, List<Integer>> reverseGraph;
    private boolean[] visited;
    private int[] lowlink;
    private int[] ids;
    private int id;
    private int n;
    private Deque<Integer> stack;
    private boolean[] onStack;
    private List<List<Integer>> components;
    private Metrics metrics;

    public SCC(Map<Integer, List<Integer>> graph, int n, Metrics metrics) {
        this.graph = graph;
        this.n = n;
        this.metrics = metrics;
        this.components = new ArrayList<>();
        buildReverseGraph();
    }

    private void buildReverseGraph() {
        reverseGraph = new HashMap<>();
        for (int u : graph.keySet()) {
            for (int v : graph.get(u)) {
                reverseGraph.computeIfAbsent(v, k -> new ArrayList<>()).add(u);
                metrics.incrementEdgeTraversals();
            }
        }
    }

    public List<List<Integer>> findSCCs() {
        components.clear();
        visited = new boolean[n];
        lowlink = new int[n];
        ids = new int[n];
        onStack = new boolean[n];
        Arrays.fill(ids, -1);
        stack = new ArrayDeque<>();
        id = 0;

        // First pass: DFS to fill order
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfs(i);
            }
        }

        return components;
    }

    private void dfs(int u) {
        visited[u] = true;
        ids[u] = lowlink[u] = id++;
        stack.push(u);
        onStack[u] = true;
        metrics.incrementDfsVisits();

        // Visit all neighbors
        if (graph.containsKey(u)) {
            for (int v : graph.get(u)) {
                metrics.incrementEdgeTraversals();
                if (!visited[v]) {
                    dfs(v);
                    lowlink[u] = Math.min(lowlink[u], lowlink[v]);
                } else if (onStack[v]) {
                    lowlink[u] = Math.min(lowlink[u], ids[v]);
                }
            }
        }

        // If u is a root node, pop the stack and generate an SCC
        if (lowlink[u] == ids[u]) {
            List<Integer> component = new ArrayList<>();
            while (true) {
                int v = stack.pop();
                onStack[v] = false;
                component.add(v);
                if (v == u) break;
            }
            components.add(component);
        }
    }

    // Alternative implementation using Kosaraju's algorithm (more reliable)
    public List<List<Integer>> findSCCsKosaraju() {
        components.clear();
        visited = new boolean[n];
        stack = new ArrayDeque<>();

        // First pass: DFS to fill stack with finishing times
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                dfsFirstPass(i);
            }
        }

        // Second pass: Process in reverse order using reverse graph
        Arrays.fill(visited, false);
        List<List<Integer>> sccs = new ArrayList<>();

        while (!stack.isEmpty()) {
            int u = stack.pop();
            if (!visited[u]) {
                List<Integer> component = new ArrayList<>();
                dfsSecondPass(u, component);
                sccs.add(component);
            }
        }

        return sccs;
    }

    private void dfsFirstPass(int u) {
        visited[u] = true;
        metrics.incrementDfsVisits();

        if (graph.containsKey(u)) {
            for (int v : graph.get(u)) {
                metrics.incrementEdgeTraversals();
                if (!visited[v]) {
                    dfsFirstPass(v);
                }
            }
        }

        stack.push(u);
    }

    private void dfsSecondPass(int u, List<Integer> component) {
        visited[u] = true;
        component.add(u);
        metrics.incrementDfsVisits();

        if (reverseGraph.containsKey(u)) {
            for (int v : reverseGraph.get(u)) {
                metrics.incrementEdgeTraversals();
                if (!visited[v]) {
                    dfsSecondPass(v, component);
                }
            }
        }
    }

    public Map<Integer, List<Integer>> buildCondensationGraph() {
        // Use Kosaraju's algorithm for more reliable SCC detection
        List<List<Integer>> sccs = findSCCsKosaraju();
        Map<Integer, Integer> nodeToComponent = new HashMap<>();
        Map<Integer, List<Integer>> condensationGraph = new HashMap<>();

        // Map each node to its component
        for (int i = 0; i < sccs.size(); i++) {
            for (int node : sccs.get(i)) {
                nodeToComponent.put(node, i);
            }
        }

        // Build edges between components
        for (int u : graph.keySet()) {
            int compU = nodeToComponent.get(u);
            for (int v : graph.get(u)) {
                int compV = nodeToComponent.get(v);
                if (compU != compV) {
                    condensationGraph.computeIfAbsent(compU, k -> new ArrayList<>()).add(compV);
                }
            }
        }

        return condensationGraph;
    }
}