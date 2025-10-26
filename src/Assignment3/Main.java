import com.google.gson.*;
import Assignment3.PrimMST;
import java.util.*;

import static Assignment3.PrimMST.*;

public static void main(String[] args) {
    try {
        String inputFilePath = "src/input.json";
        String outputFilePath = "src/output.json";

        Map<String, JsonObject> graphs = readAllGraphsFromJSON(inputFilePath);
        List<Map<String, Object>> results = new ArrayList<>();

        int graphId = 1;
        for (Map.Entry<String, JsonObject> entry : graphs.entrySet()) {
            JsonObject graphJson = entry.getValue();

            JsonArray verticesArray = graphJson.getAsJsonArray("vertices");
            List<String> vertices = new ArrayList<>();
            for (JsonElement vertex : verticesArray) vertices.add(vertex.getAsString());

            JsonArray edgesArray = graphJson.getAsJsonArray("edges");
            List<PrimMST.Edge> rawEdges = new ArrayList<>();
            for (JsonElement e : edgesArray) {
                JsonObject obj = e.getAsJsonObject();
                rawEdges.add(new PrimMST.Edge(
                        obj.get("from").getAsString(),
                        obj.get("to").getAsString(),
                        obj.get("cost").getAsInt()
                ));
            }

            Map<String, Object> prim = primMST(vertices, rawEdges);

            Map<String, Object> graphResult = new HashMap<>();
            graphResult.put("vertices_count", vertices.size());
            graphResult.put("edges_count", edgesArray.size()); // входных рёбер
            graphResult.put("mst_edges", prim.get("mst_edges"));
            graphResult.put("total_cost", prim.get("total_cost"));
            graphResult.put("operations_count", prim.get("operations_count"));
            graphResult.put("execution_time_ms", prim.get("execution_time_ms"));

            results.add(graphResult);
            System.out.println("Graph " + graphId + " done ");
            graphId++;
        }

        writeResultsToJson(results, outputFilePath);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void main() {
}
