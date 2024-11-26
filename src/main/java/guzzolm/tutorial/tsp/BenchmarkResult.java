package guzzolm.tutorial.tsp;

public class BenchmarkResult {
    public final double TotalTime;
    public final int TotalDistance;
    public final int CitiesCount;
    private final int DistanceMatrixId;
    public boolean IsOptimal = false;
    public final boolean Timeout;

    public BenchmarkResult(long startTime, long endTime, int[] route, int[][] distances, int distanceMatrixId){
        DistanceMatrixId = distanceMatrixId;
        TotalTime = (endTime - startTime) / 1_000_000.0;
        CitiesCount = distances.length;
        Timeout = route == null;
        TotalDistance = route == null ? 0 : DistanceCalculator.calculateTotalDistance(route, distances);
    }

    public int CitiesCount() {
        return CitiesCount;
    }

    public int DistanceMatrixId() {
        return DistanceMatrixId;
    }

    public void IsOptimal(boolean optimal) {
        IsOptimal = optimal;
    }
}
