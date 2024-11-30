package guzzolm.tutorial.tsp.impl;

import guzzolm.tutorial.tsp.DistanceCalculator;
import guzzolm.tutorial.tsp.TspSolver;

import java.util.ArrayList;
import java.util.List;

public class BruteForceSolver implements TspSolver {
    @Override
    public String getName() {
        return "BruteForce";
    }

    @Override
    public int[] solve(int[][] distanceMatrix) {
        var cities = new int[distanceMatrix.length - 1];
        for (int i = 1; i < distanceMatrix.length; i++) {
            cities[i - 1] = i;
        }

        var routes = getRoutes(cities, new boolean[cities.length], new int[cities.length], 0);
        var bestRoute = new int[cities.length + 2];
        var currentRoute = new int[cities.length + 2];
        var shortestDistance = Integer.MAX_VALUE;

        for(var route : routes) {
            System.arraycopy(route, 0, currentRoute, 1, cities.length);
            var routeDistance = DistanceCalculator.calculateTotalDistance(currentRoute, distanceMatrix);
            if(routeDistance < shortestDistance) {
                shortestDistance = routeDistance;
                bestRoute = currentRoute.clone();
            }
        }

        return bestRoute;
    }

    private List<int[]> getRoutes(int[] cities, boolean[] visited, int[] route,  int start){
        var routes = new ArrayList<int[]>();

        if (start == cities.length) {
            routes.add(route.clone());
        }

        for (int i = 0; i < cities.length; i++) {
            if (visited[i]) {
                continue;
            }
            route[start] = cities[i];
            visited[i] = true;
            routes.addAll(getRoutes(cities, visited, route, start + 1));
            route[start] = 0;
            visited[i] = false;
        }

        return routes;
    }
}
