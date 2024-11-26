package guzzolm.tutorial.tsp;

public record BenchmarkAggregatedResult(String solver, int key, double averageTime, double optimalRouteHitRate, double timeouts) {
}
