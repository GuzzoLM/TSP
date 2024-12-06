package guzzolm.tutorial.tsp.impl;

import guzzolm.tutorial.tsp.TspSolver;

public class NearestNeighborSolver implements TspSolver {
    @Override
    public String getName() {
        return "NearestNeighbor";
    }

    @Override
    public int[] solve(int[][] distanceMatrix) {
        var numberOfCities = distanceMatrix.length;
        var visited = new boolean[numberOfCities];
        var route = new int[numberOfCities + 1];

        var currentCity = 0;
        route[0] = currentCity;
        visited[currentCity] = true;

        for (var i = 1; i < numberOfCities; i++) {
            var nearestCity = -1;
            var shortestDistance = Integer.MAX_VALUE;

            for (int nextCity = 0; nextCity < numberOfCities; nextCity++) {
                if (!visited[nextCity] && distanceMatrix[currentCity][nextCity] < shortestDistance) {
                    nearestCity = nextCity;
                    shortestDistance = distanceMatrix[currentCity][nextCity];
                }
            }

            route[i] = nearestCity;
            visited[nearestCity] = true;
            currentCity = nearestCity;
        }

        return route;
    }
}
