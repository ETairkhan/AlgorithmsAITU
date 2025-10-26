package Assignment3;

import com.google.gson.*;
import java.io.*;
import java.util.*;

public class PrimMST {

    // Рёбра
    public static class Edge implements Comparable<Edge> {
        String from;
        String to;
        int cost;

        public Edge(String from, String to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }

        @Override
        public int compareTo(Edge other) {
            return Integer.compare(this.cost, other.cost);
        }
    }

    // Рёбра MST
    static class MSTEdge {
        String from;
        String to;
        int cost;

        public MSTEdge(String from, String to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    // Чтение  графов
    public static Map<String, JsonObject> readAllGraphsFromJSON(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            JsonElement jsonElement = JsonParser.parseReader(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            Map<String, JsonObject> graphs = new LinkedHashMap<>();
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                if (entry.getValue().isJsonObject()) {
                    graphs.put(entry.getKey(), entry.getValue().getAsJsonObject());
                }
            }
            return graphs;
        }
    }

    // пропуск несуществующих вершин, выбор минимального веса для каждой пары
    private static List<Edge> normalizeEdges(List<String> vertices, List<Edge> rawEdges) {
        Set<String> vertexSet = new HashSet<>(vertices);
        // key: u#v (u < v), value: min cost
        Map<String, Integer> minEdgeByPair = new HashMap<>();

        for (Edge e : rawEdges) {
            if (e == null || e.from == null || e.to == null) continue;

            if (!vertexSet.contains(e.from) || !vertexSet.contains(e.to)) {
                System.err.println("⚠️  Edge skipped (unknown vertex): " + e.from + " - " + e.to);
                continue;
            }
            if (e.from.equals(e.to)) {
                // self-loop не нужен для MST
                continue;
            }

            String u = e.from;
            String v = e.to;
            // нормализуем пару (u < v) лексикографически для неориентированного графа
            String a = u.compareTo(v) <= 0 ? u : v;
            String b = u.compareTo(v) <= 0 ? v : u;
            String key = a + "#" + b;

            minEdgeByPair.merge(key, e.cost, Math::min);
        }

        List<Edge> edges = new ArrayList<>();
        for (Map.Entry<String, Integer> en : minEdgeByPair.entrySet()) {
            String[] parts = en.getKey().split("#", 2);
            edges.add(new Edge(parts[0], parts[1], en.getValue()));
        }
        return edges;
    }

    // Прима для одной компоненты
    private static void runPrimFromStart(
            int startIdx,
            List<String> vertices,
            List<List<Edge>> adj,
            boolean[] inMST,
            List<MSTEdge> mst,
            long[] totalCostHolder, // обёртка для long
            int[] operationsHolder   // обёртка для int
    ) {
        Map<String, Integer> vertexIndexMap = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) vertexIndexMap.put(vertices.get(i), i);

        PriorityQueue<Edge> pq = new PriorityQueue<>();
        // from = null, to = стартовая вершина
        pq.add(new Edge(null, vertices.get(startIdx), 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = vertexIndexMap.get(current.to);
            if (inMST[u]) continue;

            inMST[u] = true;
            totalCostHolder[0] += current.cost;
            if (current.from != null) {
                mst.add(new MSTEdge(current.from, current.to, current.cost));
            }

            for (Edge nb : adj.get(u)) {
                operationsHolder[0]++;
                int v = vertexIndexMap.get(nb.to.equals(vertices.get(u)) ? nb.from : nb.to);
                // У нас в adj хранится обе стороны явно, поэтому определяем соседа корректно:
                v = vertexIndexMap.get(nb.to);
                if (!inMST[v]) {
                    pq.add(new Edge(vertices.get(u), vertices.get(v), nb.cost));
                }
            }
        }
    }

    // Основной алгоритм
    public static Map<String, Object> primMST(List<String> vertices, List<Edge> inputEdges) {

        Map<String, Object> result = new HashMap<>();
        List<MSTEdge> mst = new ArrayList<>();
        long totalCost = 0L;
        int operations = 0;

        // Пустой граф
        if (vertices == null || vertices.isEmpty()) {
            result.put("mst_edges", mst);
            result.put("total_cost", 0);
            result.put("operations_count", 0);
            result.put("execution_time_ms", 0.0);
            return result;
        }

        // Нормализуем рёбра
        List<Edge> edges = normalizeEdges(vertices, inputEdges);

        // Построение списка смежности
        Map<String, Integer> idx = new HashMap<>();
        for (int i = 0; i < vertices.size(); i++) idx.put(vertices.get(i), i);

        List<List<Edge>> adj = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) adj.add(new ArrayList<>());

        for (Edge e : edges) {
            // добавляем обе стороны
            int u = idx.get(e.from);
            int v = idx.get(e.to);
            adj.get(u).add(new Edge(e.from, e.to, e.cost));
            adj.get(v).add(new Edge(e.to, e.from, e.cost));
        }

        boolean[] inMST = new boolean[vertices.size()];

        long start = System.nanoTime();

        // Запускаем Прима из каждой ещё не посещённой компоненты
        int components = 0;
        for (int s = 0; s < vertices.size(); s++) {
            if (!inMST[s]) {
                components++;
                long[] costHolder = new long[] {0L};
                int[] opsHolder = new int[] {0};

                runPrimFromStart(s, vertices, adj, inMST, mst, costHolder, opsHolder);

                totalCost += costHolder[0];
                operations += opsHolder[0];
            }
        }

        long end = System.nanoTime();
        double execMs = (end - start) / 1_000_000.0;

        if (components > 1) {
            System.err.println("Graph is disconnected. Built Minimum Spanning Forest (components: " + components + ").");
        }

        result.put("mst_edges", mst);
        result.put("total_cost", (totalCost > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int) totalCost));
        result.put("operations_count", operations);
        result.put("execution_time_ms", Math.round(execMs * 100.0) / 100.0);

        return result;
    }

    // Запись результатов
    public static void writeResultsToJson(List<Map<String, Object>> results, String outputFilePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("[\n");

        for (int i = 0; i < results.size(); i++) {
            Map<String, Object> graphResult = results.get(i);

            sb.append("  {\n");
            sb.append("    \"graph_id\": ").append(i + 1).append(",\n");
            sb.append("    \"input_stats\": {\n");
            sb.append("      \"vertices\": ").append(graphResult.get("vertices_count")).append(",\n");
            sb.append("      \"edges\": ").append(graphResult.get("edges_count")).append("\n");
            sb.append("    },\n");
            sb.append("    \"prim\": {\n");

            sb.append("      \"mst_edges\": [\n");
            @SuppressWarnings("unchecked")
            List<MSTEdge> mstEdges = (List<MSTEdge>) graphResult.get("mst_edges");
            for (int j = 0; j < mstEdges.size(); j++) {
                MSTEdge e = mstEdges.get(j);
                sb.append("        {\"from\": \"").append(e.from)
                        .append("\", \"to\": \"").append(e.to)
                        .append("\", \"weight\": ").append(e.cost).append("}");
                if (j < mstEdges.size() - 1) sb.append(",");
                sb.append("\n");
            }
            sb.append("      ],\n");

            sb.append("      \"total_cost\": ").append(graphResult.get("total_cost")).append(",\n");
            sb.append("      \"operations_count\": ").append(graphResult.get("operations_count")).append(",\n");
            sb.append("      \"execution_time_ms\": ").append(graphResult.get("execution_time_ms")).append("\n");
            sb.append("    }\n");
            sb.append("  }");

            if (i < results.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("]");

        try (FileWriter writer = new FileWriter(outputFilePath)) {
            writer.write(sb.toString());
        }

        System.out.println("Results saved to " + outputFilePath);
    }


}

