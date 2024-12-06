package guzzolm.tutorial.tsp;

import guzzolm.tutorial.tsp.impl.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
class TspApplicationTests {

    private static int[][] distanceMatrix = {
            {0, 10, 15, 20, 5},
            {10, 0, 35, 25, 20},
            {15, 35, 0, 30, 25},
            {20, 25, 30, 0, 30},
            {5, 20, 25, 30, 0},
    };

    private static int[] optimalRoute = new int[] {0, 1, 3, 2, 4, 0};
    private static int optimalDistance = DistanceCalculator.calculateTotalDistance(optimalRoute, distanceMatrix);

    @Test
    void bruteForce() {
        var solver = new BruteForceSolver();
        var result = solver.solve(distanceMatrix);
        var resultingDistance = DistanceCalculator.calculateTotalDistance(result, distanceMatrix);
        Assertions.assertEquals(optimalDistance, resultingDistance);
    }

    @Test
    void heldKarp() {
        var solver = new HeldKarpSolver();
        var result = solver.solve(distanceMatrix);
        var resultingDistance = DistanceCalculator.calculateTotalDistance(result, distanceMatrix);
        Assertions.assertEquals(optimalDistance, resultingDistance);
    }

    @Test
    void branchAndBound() {
        var solver = new BranchAndBoundSolver();
        var result = solver.solve(distanceMatrix);
        var resultingDistance = DistanceCalculator.calculateTotalDistance(result, distanceMatrix);
        Assertions.assertEquals(optimalDistance, resultingDistance);
    }

    @Test
    void nearestNeighbor() {
        var solver = new NearestNeighborSolver();
        var result = solver.solve(distanceMatrix);
        Assertions.assertEquals(optimalRoute.length, result.length);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[result.length - 1]);
        Assertions.assertEquals(optimalRoute.length - 1, Arrays.stream(result).distinct().count());
    }

    @Test
    void cheapestInsert() {
        var solver = new CheapestInsertionSolver();
        var result = solver.solve(distanceMatrix);
        Assertions.assertEquals(optimalRoute.length, result.length);
        Assertions.assertEquals(0, result[0]);
        Assertions.assertEquals(0, result[result.length - 1]);
        Assertions.assertEquals(optimalRoute.length - 1, Arrays.stream(result).distinct().count());
    }

}
