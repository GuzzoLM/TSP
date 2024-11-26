package guzzolm.tutorial.tsp;

public interface TspSolver {
    int[] solve(int[][] distanceMatrix);
    String getName();
}
