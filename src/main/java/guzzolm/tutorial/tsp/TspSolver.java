package guzzolm.tutorial.tsp;

public interface TspSolver {
    String getName();
    int[] solve(int[][] distanceMatrix);
}
