package guzzolm.tutorial.tsp.impl;

import guzzolm.tutorial.tsp.TspSolver;

import java.util.ArrayList;

public class CheapestInsertionSolver implements TspSolver {
    @Override
    public String getName() {
        return "CheapestInsertion";
    }

    @Override
    public int[] solve(int[][] distanceMatrix) {
        var numberOfCities = distanceMatrix.length;
        var route = new ArrayList<Integer>();
        var visited = new boolean[numberOfCities];

        // Start with an initial route (A → B → A)
        route.add(0);
        route.add(1);
        route.add(0);
        visited[0] = true;
        visited[1] = true;

        for (var i = 2; i < numberOfCities; i++) {
            var bestCity = -1;
            var bestPosition = -1;
            var minIncrease = Integer.MAX_VALUE;

            for (var city = 0; city < numberOfCities; city++) {
                if (visited[city]) continue;

                for (var position = 0; position < route.size() - 1; position++) {

                    // A -> B -> A => A -> C -> B -> A
                    // Cost Increase = A -> C + C -> B - A -> B
                    var costIncrease = distanceMatrix[route.get(position)][city] +
                            distanceMatrix[city][route.get(position + 1)] -
                            distanceMatrix[route.get(position)][route.get(position + 1)];

                    if (costIncrease < minIncrease) {
                        minIncrease = costIncrease;
                        bestCity = city;
                        bestPosition = position;
                    }
                }
            }

            route.add(bestPosition + 1, bestCity);
            visited[bestCity] = true;
        }

        return route.stream().mapToInt(i -> i).toArray();
    }
}
