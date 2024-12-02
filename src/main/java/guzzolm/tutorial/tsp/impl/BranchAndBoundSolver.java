package guzzolm.tutorial.tsp.impl;

import guzzolm.tutorial.tsp.TspSolver;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

public class BranchAndBoundSolver implements TspSolver {
    @Override
    public String getName() {
        return "BranchAndBound";
    }

    @Override
    public int[] solve(int[][] distanceMatrix) {
        var cities = new int[distanceMatrix.length];
        for (int i = 0; i < distanceMatrix.length; i++) {
            cities[i] = i;
        }

        var route = new int[distanceMatrix.length + 1];
        var bestRoute = new int[distanceMatrix.length + 1];
        var visited = new boolean[distanceMatrix.length];
        visited[0] = true;

        branchAndBound(
                0,
                0,
                1,
                route,
                cities,
                visited,
                distanceMatrix,
                new AtomicInteger(Integer.MAX_VALUE),
                bestRoute);

        return bestRoute;
    }

    private void branchAndBound(
            int currentCity,
            int currentCost,
            int level,
            int[] currentPath,
            int[] cities,
            boolean[] visited,
            int[][] distanceMatrix,
            AtomicInteger minCost,
            int[] bestRoute) {

        if (level == cities.length) {
            // Complete the cycle back to the starting city
            int totalCost = currentCost + distanceMatrix[currentCity][0];
            if (totalCost < minCost.get()) {
                minCost.set(totalCost);
                System.arraycopy(currentPath, 0, bestRoute, 0, cities.length);
            }
            return;
        }

        for (var nextCity = 0; nextCity < cities.length; nextCity++) {
            if (!visited[nextCity]) {
                var newCost = currentCost + distanceMatrix[currentCity][nextCity];
                var lowerBound = getBound(nextCity, cities, visited, distanceMatrix);

                // Prune if the new lower bound is not promising
                if (newCost + lowerBound < minCost.get()) {
                    visited[nextCity] = true;
                    currentPath[level] = nextCity;
                    branchAndBound(nextCity, newCost, level + 1, currentPath, cities, visited, distanceMatrix, minCost, bestRoute);
                    visited[nextCity] = false; // Backtrack
                    currentPath[level] = 0;
                }
            }
        }

    }

    private double getBound(int candidateCity, int[] cities, boolean[] visited, int[][] distanceMatrix) {
        return (double) Arrays.stream(cities).filter(x -> !visited[x]).map(x -> getMinEdgeCost(x, candidateCity, visited, distanceMatrix)).sum() / 2;
    }

    private int getMinEdgeCost(int city, int candidateCity, boolean[] visited, int[][] distanceMatrix){
        var cost = IntStream.range(0, distanceMatrix.length)
                .filter(i -> i != city && (!visited[i] || i == 0) && i != candidateCity)
                .map(x -> distanceMatrix[city][x])
                .min();
        return cost.isPresent() ? cost.getAsInt() : 0;
    }
}
