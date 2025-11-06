package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Validator {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            System.out.println("Usage: java org.example.Validator <output_json_file>");
            return;
        }

        String outputFile = args[0];
        String jsonContent = new String(Files.readAllBytes(Paths.get(outputFile)));
        Map<String, Object> results = mapper.readValue(jsonContent, Map.class);

        System.out.println("=== VALIDATION RESULTS ===");

        // Validate SCC results
        validateSCC(results);

        // Validate Topological Sort
        validateTopologicalSort(results);

        // Validate Shortest Paths
        validateShortestPaths(results);

        System.out.println("Validation completed!");
    }

    private static void validateSCC(Map<String, Object> results) {
        System.out.println("\n1. SCC Validation:");
        Map<String, Object> sccResults = (Map<String, Object>) results.get("scc");

        List<List<Integer>> components = (List<List<Integer>>) sccResults.get("components");
        Map<String, Object> metrics = (Map<String, Object>) sccResults.get("metrics");

        System.out.println("   - Found " + components.size() + " SCC components");
        System.out.println("   - Time: " + metrics.get("time_ns") + " ns");
        System.out.println("   - DFS visits: " + metrics.get("dfs_visits"));

        // Check that all components are non-empty
        for (int i = 0; i < components.size(); i++) {
            List<Integer> component = components.get(i);
            if (component.isEmpty()) {
                System.out.println("   ❌ ERROR: Empty component found at index " + i);
            } else {
                System.out.println("   ✓ Component " + i + ": " + component.size() + " nodes");
            }
        }
    }

    private static void validateTopologicalSort(Map<String, Object> results) {
        System.out.println("\n2. Topological Sort Validation:");
        Map<String, Object> topoResults = (Map<String, Object>) results.get("topological_sort");

        List<Integer> componentOrder = (List<Integer>) topoResults.get("component_order");
        Map<String, Object> metrics = (Map<String, Object>) topoResults.get("metrics");

        System.out.println("   - Topological order length: " + componentOrder.size());
        System.out.println("   - Time: " + metrics.get("time_ns") + " ns");
        System.out.println("   - Queue operations: " + metrics.get("queue_operations"));

        if (componentOrder.isEmpty()) {
            System.out.println("   ❌ ERROR: Empty topological order");
        } else {
            System.out.println("   ✓ Valid topological order generated");
        }
    }

    private static void validateShortestPaths(Map<String, Object> results) {
        System.out.println("\n3. Shortest Paths Validation:");
        Map<String, Object> spResults = (Map<String, Object>) results.get("shortest_paths");

        int source = (Integer) spResults.get("source");
        List<Integer> distances = (List<Integer>) spResults.get("distances");
        List<Integer> longestDistances = (List<Integer>) spResults.get("longest_distances");
        Map<String, Object> criticalPath = (Map<String, Object>) spResults.get("critical_path");
        Map<String, Object> metrics = (Map<String, Object>) spResults.get("metrics");

        System.out.println("   - Source node: " + source);
        System.out.println("   - Time: " + metrics.get("time_ns") + " ns");
        System.out.println("   - Relax operations: " + metrics.get("relax_operations"));

        // Check source distance is 0
        if (distances.get(source) == 0) {
            System.out.println("   ✓ Source distance is 0 (correct)");
        } else {
            System.out.println("   ❌ ERROR: Source distance should be 0, got " + distances.get(source));
        }

        // Check critical path
        List<Integer> path = (List<Integer>) criticalPath.get("path");
        int length = (Integer) criticalPath.get("length");
        System.out.println("   - Critical path length: " + length);
        System.out.println("   - Critical path nodes: " + path.size());

        if (path.isEmpty()) {
            System.out.println("   ⚠ WARNING: Empty critical path");
        } else {
            System.out.println("   ✓ Critical path found");
        }
    }
}