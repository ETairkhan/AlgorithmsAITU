package Assignment3.Tairkhan;

public class Edge implements Comparable<Edge> {
    private String from;
    private String to;
    private int weight;

    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    // Getters
    public String getFrom() { return from; }
    public String getTo() { return to; }
    public int getWeight() { return weight; }

    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }

    @Override
    public String toString() {
        return "{\"from\": \"" + from + "\", \"to\": \"" + to + "\", \"weight\": " + weight + "}";
    }
}