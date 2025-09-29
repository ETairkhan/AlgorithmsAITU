package Assignment2.StudentA.algorithms;

import java.util.ArrayList;
import java.util.List;

public class MinHeap {
    private List<Integer> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    private int parent(int i) { return (i - 1) / 2; }
    private int left(int i) { return 2 * i + 1; }
    private int right(int i) { return 2 * i + 2; }

    public void insert(int key) {
        heap.add(key);
        int i = heap.size() - 1;
        while (i > 0 && heap.get(parent(i)) > heap.get(i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    public int extractMin() {
        if (heap.isEmpty()) throw new RuntimeException("Heap is empty");
        if (heap.size() == 1) return heap.remove(0);

        int root = heap.get(0);
        heap.set(0, heap.remove(heap.size() - 1));
        heapify(0);
        return root;
    }

    public void decreaseKey(int i, int newVal) {
        heap.set(i, newVal);
        while (i > 0 && heap.get(parent(i)) > heap.get(i)) {
            swap(i, parent(i));
            i = parent(i);
        }
    }

    public void merge(MinHeap other) {
        for (int key : other.heap) {
            this.insert(key);
        }
    }

    private void heapify(int i) {
        int l = left(i), r = right(i);
        int smallest = i;
        if (l < heap.size() && heap.get(l) < heap.get(smallest)) smallest = l;
        if (r < heap.size() && heap.get(r) < heap.get(smallest)) smallest = r;
        if (smallest != i) {
            swap(i, smallest);
            heapify(smallest);
        }
    }

    private void swap(int i, int j) {
        int tmp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, tmp);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public List<Integer> getHeap() {
        return heap;
    }
}
