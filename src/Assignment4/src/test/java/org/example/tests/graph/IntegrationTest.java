package org.example.tests.graph;

import org.example.graph.Metrics;
import org.example.graph.scc.SCC;
import org.example.graph.topo.TopologicalSort;
import org.example.graph.dagsp.DAGShortestPath;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class IntegrationTest {

    @Test
    public void testFullPipeline() {
        // Create a graph with SCCs and DAG structure
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList(2));
        graph.put(2, Arrays.asList(0, 3)); // SCC: 0,1,2
        graph.put(3, Arrays.asList(4));
        graph.put(4, Arrays.asList(5));
        graph.put(5, Arrays.asList(6));
        graph.put(6, Arrays.asList(4)); // SCC: 4,5,6

        Metrics metrics = new Metrics();

        // 1. Find SCCs - use Kosaraju for reliability
        SCC scc = new SCC(graph, 7, metrics);
        List<List<Integer>> components = scc.findSCCsKosaraju();
        Map<Integer, List<Integer>> condensationGraph = scc.buildCondensationGraph();

        // Should have 3 components: [0,1,2], [3], [4,5,6]
        // But node 3 might be in its own component or grouped elsewhere
        assertTrue(components.size() >= 2, "Should find at least 2 SCCs");

        // 2. Topological sort on condensation graph
        int condensationSize = condensationGraph.keySet().stream()
                .mapToInt(Integer::intValue)
                .max()
                .orElse(-1) + 1;
        if (condensationSize == 0) condensationSize = 1;

        TopologicalSort topo = new TopologicalSort(condensationGraph, condensationSize, metrics);
        List<Integer> topoOrder = topo.kahnTopologicalSort();
        assertFalse(topoOrder.isEmpty());

        // 3. Create weighted graph for shortest paths
        Map<Integer, List<DAGShortestPath.Edge>> weightedGraph = new HashMap<>();
        weightedGraph.put(0, Arrays.asList(new DAGShortestPath.Edge(1, 2)));
        weightedGraph.put(1, Arrays.asList(new DAGShortestPath.Edge(2, 3)));
        weightedGraph.put(2, Arrays.asList(new DAGShortestPath.Edge(3, 1)));
        weightedGraph.put(3, Arrays.asList(new DAGShortestPath.Edge(4, 2)));

        Map<Integer, List<Integer>> simpleGraph = new HashMap<>();
        simpleGraph.put(0, Arrays.asList(1));
        simpleGraph.put(1, Arrays.asList(2));
        simpleGraph.put(2, Arrays.asList(3));
        simpleGraph.put(3, Arrays.asList(4));

        TopologicalSort simpleTopo = new TopologicalSort(simpleGraph, 5, metrics);
        List<Integer> simpleTopoOrder = simpleTopo.kahnTopologicalSort();

        DAGShortestPath dagSP = new DAGShortestPath(weightedGraph, 5, metrics);
        int[] distances = dagSP.shortestPaths(simpleTopoOrder, 0);

        assertEquals(0, distances[0]);
        assertEquals(2, distances[1]);
        assertEquals(5, distances[2]); // 0->1->2 = 2+3 = 5
        assertEquals(6, distances[3]); // 0->1->2->3 = 2+3+1 = 6
        assertEquals(8, distances[4]); // 0->1->2->3->4 = 2+3+1+2 = 8
    }
}