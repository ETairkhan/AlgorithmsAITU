package org.example.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.model.GraphResult;
import org.example.model.InputData;
import org.example.model.OutputData;
import org.example.model.AlgorithmResult;
import org.example.model.Edge;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JsonUtil {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static InputData readInput(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, InputData.class);
        }
    }

    // Этот метод не используется, можно удалить
    public static void writeOutput(String filePath, OutputData outputData) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(outputData, writer);
        }
    }

    public static void writeOutput(List<GraphResult> results, String filePath) throws IOException {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write("{\n");
            writer.write("  \"results\": [\n");

            for (int i = 0; i < results.size(); i++) {
                GraphResult result = results.get(i);
                writer.write("    {\n");
                writer.write("      \"graph_id\": " + result.getGraphId() + ",\n");
                writer.write("      \"input_stats\": {\n");
                writer.write("        \"vertices\": " + result.getInputStats().getVertices() + ",\n");
                writer.write("        \"edges\": " + result.getInputStats().getEdges() + "\n");
                writer.write("      },\n");

                // Prim results
                writer.write("      \"prim\": {\n");
                writeAlgorithmResult(writer, result.getPrim());
                if (result.getKruskal() != null) {
                    writer.write("      },\n");
                } else {
                    writer.write("      }\n");
                }

                // Kruskal results
                if (result.getKruskal() != null) {
                    writer.write("      \"kruskal\": {\n");
                    writeAlgorithmResult(writer, result.getKruskal());
                    writer.write("      }\n");
                }

                writer.write("    }");
                if (i < results.size() - 1) writer.write(",");
                writer.write("\n");
            }

            writer.write("  ]\n");
            writer.write("}\n");
        }
    }

    private static void writeAlgorithmResult(FileWriter writer, AlgorithmResult result) throws IOException {
        // Write MST edges
        writer.write("        \"mst_edges\": [\n");
        List<Edge> edges = result.getMstEdges();
        for (int j = 0; j < edges.size(); j++) {
            Edge edge = edges.get(j);
            writer.write("          {\"from\": \"" + edge.getFrom() +
                    "\", \"to\": \"" + edge.getTo() +
                    "\", \"weight\": " + edge.getWeight() + "}");
            if (j < edges.size() - 1) writer.write(",");
            writer.write("\n");
        }
        writer.write("        ],\n");

        // Write other fields
        writer.write("        \"total_cost\": " + result.getTotalCost() + ",\n");
        writer.write("        \"operations_count\": " + result.getOperationsCount() + ",\n");
        writer.write("        \"execution_time_ms\": " + String.format("%.4f", result.getExecutionTimeMs()) + "\n");
    }
}