package Assignment3.Tairkhan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UnionFind {
    private Map<String, String> parent;
    private Map<String, Integer> rank;
    private int operations;

    public UnionFind(List<String> nodes) {
        parent = new HashMap<>();
        rank = new HashMap<>();
        operations = 0;

        for (String node : nodes) {
            parent.put(node, node);
            rank.put(node, 0);
            operations += 2;
        }
    }

    public String find(String node) {
        operations++;
        if (!parent.get(node).equals(node)) {
            parent.put(node, find(parent.get(node)));
            operations += 2;
        }
        return parent.get(node);
    }

    public boolean union(String node1, String node2) {
        String root1 = find(node1);
        String root2 = find(node2);
        operations += 2;

        if (root1.equals(root2)) {
            operations++;
            return false;
        }

        operations++;
        if (rank.get(root1) < rank.get(root2)) {
            parent.put(root1, root2);
            operations++;
        } else if (rank.get(root1) > rank.get(root2)) {
            parent.put(root2, root1);
            operations++;
        } else {
            parent.put(root2, root1);
            rank.put(root1, rank.get(root1) + 1);
            operations += 3;
        }

        return true;
    }

    public int getOperations() {
        return operations;
    }
}