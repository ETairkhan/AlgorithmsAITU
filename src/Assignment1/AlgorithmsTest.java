package Assignment1;

import java.util.*;

public class AlgorithmsTest {


    public static void testMergeSort() {
        int[] arr = randomArray(1_000);
        int[] copy = arr.clone();
        Algorithms.mergeSort(arr);
        Arrays.sort(copy);
        assert Arrays.equals(arr, copy);
    }

    public static void testQuickSort() {
        int[] arr = randomArray(1_000);
        int[] copy = arr.clone();
        Algorithms.quickSort(arr);
        Arrays.sort(copy);
        assert Arrays.equals(arr, copy);
    }

    public static void testDeterministicSelect() {
        for (int t = 0; t < 100; t++) {
            int[] arr = randomArray(200);
            int k = new Random().nextInt(arr.length);
            int[] copy = arr.clone();
            int sel = Algorithms.deterministicSelect(arr, k);
            Arrays.sort(copy);
            assert sel == copy[k];
        }
    }

    public static void testClosestPair() {
        List<Algorithms.Point> pts = new ArrayList<>();
        Random rnd = new Random();
        for (int i = 0; i < 200; i++)
            pts.add(new Algorithms.Point(rnd.nextDouble(), rnd.nextDouble()));

        double fast = Algorithms.closestPairDistance(pts);
        double slow = bruteForce(pts);
        assert Math.abs(fast - slow) < 1e-9;
    }

    public static double bruteForce(List<Algorithms.Point> pts) {
        double d = Double.POSITIVE_INFINITY;
        for (int i = 0; i < pts.size(); i++)
            for (int j = i + 1; j < pts.size(); j++) {
                double dx = pts.get(i).x() - pts.get(j).x();
                double dy = pts.get(i).y() - pts.get(j).y();
                d = Math.min(d, Math.hypot(dx, dy));
            }
        return d;
    }

    public static int[] randomArray(int n) {
        Random r = new Random();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt();
        return a;
    }
}
