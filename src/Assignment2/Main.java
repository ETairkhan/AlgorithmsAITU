package Assignment2;

import java.util.Scanner;
import Assignment2.StudentB.cli.BenchmarkRunner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Algorithm Benchmarking System");
        System.out.println("=============================\n");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Run all benchmarks");
            System.out.println("2. Run specific algorithm");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    BenchmarkRunner.runAllBenchmarks();
                    break;
                case 2:
                    System.out.print("Enter algorithm (selection/heap/kadane/maxheap): ");
                    String algo = scanner.next();
                    System.out.print("Enter size: ");
                    int size = scanner.nextInt();
                    BenchmarkRunner.runBenchmarks(algo, size);
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }
}

