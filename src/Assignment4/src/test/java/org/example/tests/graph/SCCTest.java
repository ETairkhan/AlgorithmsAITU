package org.example.tests.graph;

import org.example.graph.Metrics;
import org.example.graph.scc.SCC;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class SCCTest {

    @Test
    public void testSimpleSCC() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList(2));
        graph.put(2, Arrays.asList(0));

        Metrics metrics = new Metrics();
        SCC scc = new SCC(graph, 3, metrics);

        // Use Kosaraju's algorithm which is more reliable
        List<List<Integer>> components = scc.findSCCsKosaraju();

        assertEquals(1, components.size(), "Should find exactly 1 SCC for a 3-node cycle");

        // Sort the component for consistent testing
        List<Integer> component = components.get(0);
        Collections.sort(component);
        assertEquals(Arrays.asList(0, 1, 2), component, "SCC should contain all 3 nodes");
    }

    @Test
    public void testMultipleSCC() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList(2));
        graph.put(2, Arrays.asList(0, 3));  // SCC: 0,1,2
        graph.put(3, Arrays.asList(4));
        graph.put(4, Arrays.asList(5));
        graph.put(5, Arrays.asList(3));     // SCC: 3,4,5

        Metrics metrics = new Metrics();
        SCC scc = new SCC(graph, 6, metrics);

        // Use Kosaraju's algorithm
        List<List<Integer>> components = scc.findSCCsKosaraju();

        assertEquals(2, components.size(), "Should find exactly 2 SCCs");

        // Sort components for consistent testing
        for (List<Integer> component : components) {
            Collections.sort(component);
        }

        // Check that we have the expected SCCs
        boolean foundFirstSCC = false;
        boolean foundSecondSCC = false;

        for (List<Integer> component : components) {
            if (component.containsAll(Arrays.asList(0, 1, 2))) {
                foundFirstSCC = true;
                assertEquals(3, component.size());
            } else if (component.containsAll(Arrays.asList(3, 4, 5))) {
                foundSecondSCC = true;
                assertEquals(3, component.size());
            }
        }

        assertTrue(foundFirstSCC, "Should find SCC containing nodes 0,1,2");
        assertTrue(foundSecondSCC, "Should find SCC containing nodes 3,4,5");
    }

    @Test
    public void testDisconnectedNodes() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1));
        graph.put(1, Arrays.asList(0));  // SCC: 0,1
        // Nodes 2, 3, 4 are disconnected

        Metrics metrics = new Metrics();
        SCC scc = new SCC(graph, 5, metrics);
        List<List<Integer>> components = scc.findSCCsKosaraju();

        // Should have: [0,1] and then individual nodes 2,3,4 as separate components
        assertEquals(4, components.size());
    }

    @Test
    public void testEmptyGraph() {
        Map<Integer, List<Integer>> graph = new HashMap<>();
        Metrics metrics = new Metrics();
        SCC scc = new SCC(graph, 3, metrics);
        List<List<Integer>> components = scc.findSCCsKosaraju();

        // Each node is its own component
        assertEquals(3, components.size());
    }
}