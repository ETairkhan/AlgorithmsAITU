import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public final class Algorithms {

    // ---------- 1. MERGESORT ----------
    private static final int INSERTION_SORT_THRESHOLD = 16;

    public static void mergeSort(int[] arr) {
        int[] buf = new int[arr.length];
        mergeSort(arr, 0, arr.length - 1, buf);
    }

    private static void mergeSort(int[] a, int l, int r, int[] buf) {
        if (r - l + 1 <= INSERTION_SORT_THRESHOLD) {
            insertionSort(a, l, r);
            return;
        }
        int m = (l + r) >>> 1;
        mergeSort(a, l, m, buf);
        mergeSort(a, m + 1, r, buf);
        merge(a, l, m, r, buf);
    }

    private static void insertionSort(int[] a, int l, int r) {
        for (int i = l + 1; i <= r; i++) {
            int key = a[i], j = i - 1;
            while (j >= l && a[j] > key) a[j + 1] = a[j--];
            a[j + 1] = key;
        }
    }

    private static void merge(int[] a, int l, int m, int r, int[] buf) {
        int i = l, j = m + 1, k = l;
        while (i <= m && j <= r) buf[k++] = (a[i] <= a[j]) ? a[i++] : a[j++];
        while (i <= m) buf[k++] = a[i++];
        while (j <= r) buf[k++] = a[j++];
        System.arraycopy(buf, l, a, l, r - l + 1);
    }

    // ---------- 2. QUICKSORT ----------
    public static void quickSort(int[] a) { quickSort(a, 0, a.length - 1); }

    private static void quickSort(int[] a, int l, int r) {
        while (l < r) {
            int p = randomPartition(a, l, r);
            if (p - l < r - p) {
                quickSort(a, l, p - 1);
                l = p + 1;
            } else {
                quickSort(a, p + 1, r);
                r = p - 1;
            }
        }
    }

    private static int randomPartition(int[] a, int l, int r) {
        int pivotIndex = ThreadLocalRandom.current().nextInt(l, r + 1);
        swap(a, pivotIndex, r);
        int pivot = a[r], i = l - 1;
        for (int j = l; j < r; j++) if (a[j] <= pivot) swap(a, ++i, j);
        swap(a, i + 1, r);
        return i + 1;
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }

    // ---------- 3. DETERMINISTIC SELECT ----------
    public static int deterministicSelect(int[] a, int k) {
        if (k < 0 || k >= a.length) throw new IllegalArgumentException();
        return select(a, 0, a.length - 1, k);
    }

    private static int select(int[] a, int l, int r, int k) {
        while (true) {
            if (l == r) return a[l];
            int pivot = medianOfMedians(a, l, r);
            int p = partition(a, l, r, pivot);
            if (k == p) return a[k];
            if (k < p) r = p - 1;
            else l = p + 1;
        }
    }

    private static int partition(int[] a, int l, int r, int pivot) {
        int i = l, j = r;
        while (i <= j) {
            while (a[i] < pivot) i++;
            while (a[j] > pivot) j--;
            if (i <= j) swap(a, i++, j--);
        }
        return i - 1;
    }

    private static int medianOfMedians(int[] a, int l, int r) {
        int n = r - l + 1;
        if (n <= 5) {
            Arrays.sort(a, l, r + 1);
            return a[l + n / 2];
        }
        int numMedians = 0;
        for (int i = l; i <= r; i += 5) {
            int subRight = Math.min(i + 4, r);
            Arrays.sort(a, i, subRight + 1);
            swap(a, l + numMedians++, i + (subRight - i) / 2);
        }
        return medianOfMedians(a, l, l + numMedians - 1);
    }

    // ---------- 4. CLOSEST PAIR OF POINTS ----------
    public record Point(double x, double y) {}

    public static double closestPairDistance(List<Point> points) {
        List<Point> pts = new ArrayList<>(points);
        pts.sort(Comparator.comparingDouble(p -> p.x));
        return closestRec(pts);
    }

    private static double closestRec(List<Point> pts) {
        int n = pts.size();
        if (n <= 3) return bruteForce(pts);
        int mid = n / 2;
        double midX = pts.get(mid).x;
        double d = Math.min(
                closestRec(pts.subList(0, mid)),
                closestRec(pts.subList(mid, n))
        );

        List<Point> strip = new ArrayList<>();
        for (Point p : pts) if (Math.abs(p.x - midX) < d) strip.add(p);
        strip.sort(Comparator.comparingDouble(p -> p.y));
        for (int i = 0; i < strip.size(); i++) {
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - strip.get(i).y) < d; j++) {
                d = Math.min(d, dist(strip.get(i), strip.get(j)));
            }
        }
        return d;
    }

    private static double bruteForce(List<Point> pts) {
        double d = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.size(); i++)
            for (int j = i + 1; j < pts.size(); j++)
                d = Math.min(d, dist(pts.get(i), pts.get(j)));
        return d;
    }

    private static double dist(Point p1, Point p2) {
        double dx = p1.x - p2.x, dy = p1.y - p2.y;
        return Math.hypot(dx, dy);
    }
}
