package org.example;

import org.example.algorithms.KruskalAlgorithm;
import org.example.algorithms.PrimAlgorithm;
import org.example.model.*;
import org.example.util.JsonUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            // Read input data
            InputData inputData = JsonUtil.readInput("input.json");

            List<GraphResult> results = new ArrayList<>();
            PrimAlgorithm prim = new PrimAlgorithm();
            KruskalAlgorithm kruskal = new KruskalAlgorithm();

            for (Graph graph : inputData.getGraphs()) {
                System.out.println("Processing graph " + graph.getId());

                // Calculate input statistics
                InputStats inputStats = new InputStats(
                        graph.getNodes().size(),
                        graph.getEdges().size()
                );

                // Run Prim's algorithm
                long primStartTime = System.nanoTime();
                List<Edge> primMST = prim.findMST(graph);
                long primEndTime = System.nanoTime();
                double primTimeMs = (primEndTime - primStartTime) / 1_000_000.0;

                AlgorithmResult primResult = new AlgorithmResult(
                        primMST,
                        prim.getTotalCost(primMST),
                        prim.getOperationsCount(),
                        primTimeMs
                );

                // Run Kruskal's algorithm
                long kruskalStartTime = System.nanoTime();
                List<Edge> kruskalMST = kruskal.findMST(graph);
                long kruskalEndTime = System.nanoTime();
                double kruskalTimeMs = (kruskalEndTime - kruskalStartTime) / 1_000_000.0;

                AlgorithmResult kruskalResult = new AlgorithmResult(
                        kruskalMST,
                        kruskal.getTotalCost(kruskalMST),
                        kruskal.getOperationsCount(),
                        kruskalTimeMs
                );

                // Create result for this graph
                GraphResult graphResult = new GraphResult(
                        graph.getId(),
                        inputStats,
                        primResult,
                        kruskalResult
                );

                results.add(graphResult);
            }

            // Write output
            OutputData outputData = new OutputData(results);
            JsonUtil.writeOutput("output.json", outputData);

            System.out.println("Processing completed successfully!");
            System.out.println("Results written to output.json");

        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        }
    }
}