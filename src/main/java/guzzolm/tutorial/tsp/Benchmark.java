package guzzolm.tutorial.tsp;

import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class Benchmark {
    private static final int TIMEOUT_SECONDS = 5;

    private final Map<Integer, int[][]> DistanceMatrices;
    private final List<TspSolver> Solvers;
    private final Map<String, List<BenchmarkResult>> Results = new HashMap<>();

    public Benchmark(Map<Integer, int[][]> distanceMatrices) {
        DistanceMatrices = distanceMatrices;
        Solvers = new ArrayList<>();
    }

    public void addSolver(TspSolver solver) {
        Solvers.add(solver);
    }

    public void runBenchmarks() {
        var executorService = Executors.newSingleThreadExecutor();
        for (TspSolver solver : Solvers) {
            for (var distanceMatrixItem : DistanceMatrices.keySet()) {

                var distanceMatrix = DistanceMatrices.get(distanceMatrixItem);
                BenchmarkResult result;
                int[] route = null;

                var future = executorService.submit(() -> solver.solve(distanceMatrix));
                var startTime = System.nanoTime();

                try{
                    route = future.get(TIMEOUT_SECONDS, TimeUnit.SECONDS);
                    result = new BenchmarkResult(startTime, System.nanoTime(), route, distanceMatrix, distanceMatrixItem);

                } catch (TimeoutException e){
                    System.err.println("Solver " + solver.getName() + " timed out for distance matrix " + distanceMatrixItem);
                    future.cancel(true);
                    result = new BenchmarkResult(startTime, System.nanoTime(), null, distanceMatrix, distanceMatrixItem);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }

                Results.computeIfAbsent(solver.getName(), x -> new ArrayList<>()).add(result);
            }
        }
    }

    public List<BenchmarkAggregatedResult> getAggregatedResults(){
        var bestResults = bestResults();

        var aggregatedResults = new ArrayList<BenchmarkAggregatedResult>();

        for(var solver : Results.keySet()){
            var solverResults = Results.get(solver);
            var distances = solverResults.stream().collect(Collectors.toMap(BenchmarkResult::DistanceMatrixId, x -> x.TotalDistance));
            for(var key : distances.keySet()){
                var solverResult = solverResults.stream().filter(x -> x.DistanceMatrixId() == key).findFirst().get();
                solverResult.IsOptimal = distances.get(key) <= bestResults.get(key);
            }

            var groups = solverResults.stream().collect(Collectors.groupingBy(BenchmarkResult::CitiesCount));

            for(var group: groups.keySet()){
                var groupResults = groups.get(group);

                var total = groupResults.size();
                var optimalHitRate = (double) groupResults.stream().filter(x -> !x.Timeout).filter(x -> x.IsOptimal).count() / total;
                var timeoutHitRate = (double) groupResults.stream().filter(x -> x.Timeout).count() / total;
                var totalTimes = groupResults.stream().filter(x -> !x.Timeout).mapToDouble(x -> x.TotalTime).average();
                var averageTime = totalTimes.isEmpty() ? 0 : totalTimes.getAsDouble();
                var aggregatedResult = new BenchmarkAggregatedResult(solver, group, averageTime, optimalHitRate, timeoutHitRate);
                aggregatedResults.add(aggregatedResult);
            }
        }

        return aggregatedResults;
    }

    private Map<Integer, Integer> bestResults(){
        var optimalResults = new HashMap<Integer, Integer>();

        for(var i = 0; i < Results.values().stream().findFirst().get().size(); i++){
            optimalResults.put(i, Integer.MAX_VALUE);
        }

        for(var result : Results.values()){
            for(var i = 0; i < optimalResults.keySet().size(); i++){
                var testResult = result.get(i).TotalDistance;
                if (testResult == 0){
                    continue;
                }
                var shortestDistance = Math.min(result.get(i).TotalDistance, optimalResults.get(result.get(i).DistanceMatrixId()));
                optimalResults.put(result.get(i).DistanceMatrixId(), shortestDistance);
            }
        }

        return optimalResults;
    }
}
