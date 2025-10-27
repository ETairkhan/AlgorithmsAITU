package Assignment3.Tairkhan;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.*;
import java.util.*;

public class MSTApplication {

    public static void main(String[] args) {
        try {
            // Read input JSON
            FileInputStream input = new FileInputStream("input_example.json");
            JSONObject inputJson = new JSONObject(new JSONTokener(input));

            // Parse graphs
            List<Graph> graphs = parseGraphs(inputJson);

            // Process each graph
            JSONArray results = new JSONArray();

            for (Graph graph : graphs) {
                // Run Prim's algorithm
                PrimAlgorithm prim = new PrimAlgorithm(graph);
                MSTResult primResult = prim.findMST();

                // Run Kruskal's algorithm
                KruskalAlgorithm kruskal = new KruskalAlgorithm(graph);
                MSTResult kruskalResult = kruskal.findMST();

                // Build result JSON
                JSONObject result = buildResultJson(graph, primResult, kruskalResult);
                results.put(result);
            }

            // Write output JSON
            JSONObject outputJson = new JSONObject();
            outputJson.put("results", results);

            try (FileWriter file = new FileWriter("output.json")) {
                file.write(outputJson.toString(2));
                System.out.println("Output written to output.json");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Graph> parseGraphs(JSONObject inputJson) {
        List<Graph> graphs = new ArrayList<>();
        JSONArray graphsArray = inputJson.getJSONArray("graphs");

        for (int i = 0; i < graphsArray.length(); i++) {
            JSONObject graphJson = graphsArray.getJSONObject(i);
            int id = graphJson.getInt("id");

            // Parse nodes
            List<String> nodes = new ArrayList<>();
            JSONArray nodesArray = graphJson.getJSONArray("nodes");
            for (int j = 0; j < nodesArray.length(); j++) {
                nodes.add(nodesArray.getString(j));
            }

            // Parse edges
            List<Edge> edges = new ArrayList<>();
            JSONArray edgesArray = graphJson.getJSONArray("edges");
            for (int j = 0; j < edgesArray.length(); j++) {
                JSONObject edgeJson = edgesArray.getJSONObject(j);
                String from = edgeJson.getString("from");
                String to = edgeJson.getString("to");
                int weight = edgeJson.getInt("weight");
                edges.add(new Edge(from, to, weight));
            }

            graphs.add(new Graph(id, nodes, edges));
        }

        return graphs;
    }

    private static JSONObject buildResultJson(Graph graph, MSTResult primResult, MSTResult kruskalResult) {
        JSONObject result = new JSONObject();
        result.put("graph_id", graph.getId());

        // Input statistics
        JSONObject inputStats = new JSONObject();
        inputStats.put("vertices", graph.getVertexCount());
        inputStats.put("edges", graph.getEdgeCount());
        result.put("input_stats", inputStats);

        // Prim's results
        JSONObject primJson = new JSONObject();
        primJson.put("total_cost", primResult.getTotalCost());
        primJson.put("operations_count", primResult.getOperationsCount());
        primJson.put("execution_time_ms", String.format("%.2f", primResult.getExecutionTimeMs()));

        JSONArray primEdges = new JSONArray();
        for (Edge edge : primResult.getMstEdges()) {
            JSONObject edgeJson = new JSONObject();
            edgeJson.put("from", edge.getFrom());
            edgeJson.put("to", edge.getTo());
            edgeJson.put("weight", edge.getWeight());
            primEdges.put(edgeJson);
        }
        primJson.put("mst_edges", primEdges);
        result.put("prim", primJson);

        // Kruskal's results
        JSONObject kruskalJson = new JSONObject();
        kruskalJson.put("total_cost", kruskalResult.getTotalCost());
        kruskalJson.put("operations_count", kruskalResult.getOperationsCount());
        kruskalJson.put("execution_time_ms", String.format("%.2f", kruskalResult.getExecutionTimeMs()));

        JSONArray kruskalEdges = new JSONArray();
        for (Edge edge : kruskalResult.getMstEdges()) {
            JSONObject edgeJson = new JSONObject();
            edgeJson.put("from", edge.getFrom());
            edgeJson.put("to", edge.getTo());
            edgeJson.put("weight", edge.getWeight());
            kruskalEdges.put(edgeJson);
        }
        kruskalJson.put("mst_edges", kruskalEdges);
        result.put("kruskal", kruskalJson);

        return result;
    }
}