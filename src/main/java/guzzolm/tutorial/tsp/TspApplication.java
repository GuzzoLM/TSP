package guzzolm.tutorial.tsp;

import guzzolm.tutorial.tsp.impl.BruteForceSolver;
import guzzolm.tutorial.tsp.impl.RandomSolver;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.*;
import java.util.stream.Collectors;

@SpringBootApplication
public class TspApplication {

    public static void main(String[] args) {
        var definitions = getProblemsDefinitions();
        var benchmark = new Benchmark(definitions);
        benchmark.addSolver(new RandomSolver());
        benchmark.addSolver(new BruteForceSolver());

        benchmark.runBenchmarks();
        var result = benchmark.getAggregatedResults();
        var solvers = result.stream().collect(Collectors.groupingBy(BenchmarkAggregatedResult::solver));
        var solversKeys = solvers.keySet();

        System.out.println("=== Aggregated Results ===");

        System.out.println();
        System.out.println("=== Execution Time ===");
        for(var solver : solversKeys) {
            var solverResults = solvers.get(solver).stream()
                    .sorted(Comparator.comparing(BenchmarkAggregatedResult::key))
                    .toList();

            System.out.println("Solver: " + solver);

            for(var solverResult : solverResults){
                System.out.println("  " + solverResult.key() + " cities: " + solverResult.averageTime());
            }
        }

        System.out.println();
        System.out.println("=== Time Outs ===");
        for(var solver : solversKeys){
            var solverResults = solvers.get(solver).stream()
                    .sorted(Comparator.comparing(BenchmarkAggregatedResult::key))
                    .toList();

            System.out.println("Solver: " + solver);

            for(var solverResult : solverResults){
                System.out.println("  " + solverResult.key() + " cities: " + solverResult.timeouts() * 100 + " %");
            }
        }

        System.out.println();
        System.out.println("=== Optimal Route Hit Rate ===");
        for(var solver : solversKeys){
            var solverResults = solvers.get(solver).stream()
                    .sorted(Comparator.comparing(BenchmarkAggregatedResult::key))
                    .toList();

            System.out.println("Solver: " + solver);

            for(var solverResult : solverResults){
                System.out.println("  " + solverResult.key() + " cities: " + solverResult.optimalRouteHitRate() * 100 + " %");
            }
        }
    }

    private static Map<Integer, int[][]> getProblemsDefinitions(){
        var maxDistance = 100;
        var numberOfExamples = 5;
        var exampleSizes = new int[] {5, 10, 15, 20};
        var examples = new HashMap<Integer, int[][]>();
        var setId = 0;

        for (var size : exampleSizes) {
            for (var j = 0; j < numberOfExamples; j++) {
                examples.put(setId, citiesSet(size, maxDistance));
                setId++;
            }
        }

        return examples;
    }

    private static int[][] citiesSet(int numberOfCities, int maxDistance){
        var random = new Random();
        int[][] matrix = new int[numberOfCities][numberOfCities];

        for(var i = 0; i < numberOfCities; i++){
            for(var j = i + 1; j < numberOfCities; j++){
                var distance = random.nextInt(maxDistance) + 1;
                matrix[i][j] = distance;
                matrix[j][i] = distance;
            }
        }

        return matrix;
    }

}
