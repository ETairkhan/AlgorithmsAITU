package org.example.tests.graph;

import org.example.graph.Metrics;
import org.example.graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    public void testKahnTopologicalSortSimpleDAG() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3));

        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, 4, metrics);
        List<Integer> result = topoSort.kahnTopologicalSort();

        assertEquals(4, result.size());
        assertTrue(result.indexOf(0) < result.indexOf(1));
        assertTrue(result.indexOf(0) < result.indexOf(2));
        assertTrue(result.indexOf(1) < result.indexOf(3));
        assertTrue(result.indexOf(2) < result.indexOf(3));
    }

    @Test
    public void testDFSTopologicalSort() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(3));
        graph.put(2, Arrays.asList(3));

        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, 4, metrics);
        List<Integer> result = topoSort.dfsTopologicalSort();

        assertEquals(4, result.size());

        // For DFS topological sort, we can't easily verify the exact order
        // but we can verify that all dependencies are satisfied
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < result.size(); i++) {
            position.put(result.get(i), i);
        }

        // Verify dependencies: u should come before v for every edge u->v
        for (int u : graph.keySet()) {
            for (int v : graph.get(u)) {
                assertTrue(position.get(u) < position.get(v),
                        "Node " + u + " should come before node " + v + " in topological order");
            }
        }
    }

    @Test
    public void testEmptyGraph() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Metrics metrics = new Metrics();
        TopologicalSort topoSort = new TopologicalSort(graph, 3, metrics);
        List<Integer> result = topoSort.kahnTopologicalSort();

        assertEquals(3, result.size());
        assertTrue(result.containsAll(Arrays.asList(0, 1, 2)));
    }
}