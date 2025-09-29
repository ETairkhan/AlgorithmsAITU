package Assignment2;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Algorithm Benchmarking System");
        System.out.println("=============================\n");

        while (true) {
            System.out.println("\nOptions:");
            System.out.println("1. Run all benchmarks (A + B)");
            System.out.println("2. Run specific algorithm");
            System.out.println("3. Exit");
            System.out.print("Choose option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Running Student A Benchmarks ---");
                    Assignment2.StudentA.cli.BenchmarkRunner.runAllBenchmarks();
                    System.out.println("\n--- Running Student B Benchmarks ---");
                    Assignment2.StudentB.cli.BenchmarkRunner.runAllBenchmarks();
                    break;

                case 2:
                    System.out.print("Enter algorithm "
                            + "(insertion/shell/boyer-moore/minheap/"
                            + "selection/heap/kadane/maxheap): ");
                    String algo = scanner.next();
                    System.out.print("Enter size: ");
                    int size = scanner.nextInt();

                    if (algo.equalsIgnoreCase("insertion")
                            || algo.equalsIgnoreCase("shell")
                            || algo.equalsIgnoreCase("boyer-moore")
                            || algo.equalsIgnoreCase("minheap")) {
                        Assignment2.StudentA.cli.BenchmarkRunner.runBenchmarks(algo, size);
                    } else {
                        Assignment2.StudentB.cli.BenchmarkRunner.runBenchmarks(algo, size);
                    }
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
