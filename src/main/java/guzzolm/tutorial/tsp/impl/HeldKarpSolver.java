package guzzolm.tutorial.tsp.impl;

import guzzolm.tutorial.tsp.TspSolver;

import java.util.Arrays;

public class HeldKarpSolver implements TspSolver {
    @Override
    public String getName() {
        return "Held-Karp";
    }

    @Override
    public int[] solve(int[][] distanceMatrix) {
        var n = distanceMatrix.length; // Number of cities
        var dp = new int[1 << n][n]; // The dynamic programming table
        var parent = new int[1 << n][n]; // Stores the optimal end city from previous subsets

        // Initialize DP table with infinity
        for (var i = 0; i < (1 << n); i++) {
            Arrays.fill(dp[i], Integer.MAX_VALUE);
        }

        // Base case: city 0 is the starting point and the total distance of this subset is 0.
        dp[1][0] = 0;

        // Iterate over all subsets of cities, where mask is the bitmask of the subset
        for (int mask = 1; mask < (1 << n); mask++) {

            // Since we always start from city 0, we only consider subsets where it is present.
            if ((mask & 1) == 0) continue;

            // Iterate over all possible final cities from this subset to calculate subset distances
            // Since city 0 will never be the final city of the subset, we start fromm city 1
            for (var city = 1; city < n; city++) {

                // Check if city is in subset
                if ((mask & 1 << city) == 0) continue;

                // Gets a subset of the current subset where the current city is not present
                int prevMask = mask ^ (1 << city);

                // Iterate over all possible final cities from previous subset to find the best final city
                // Since previous subset could also be just the city 0, we start this iteration from 0
                for (var lastCity = 0; lastCity < n; lastCity++) {
                    if ((prevMask & 1 << lastCity) == 0) continue;

                    var distance = dp[prevMask][lastCity] == Integer.MAX_VALUE ?
                            Integer.MAX_VALUE :
                            dp[prevMask][lastCity] + distanceMatrix[lastCity][city];

                    if (distance < dp[mask][city]){
                        dp[mask][city] = distance;
                        parent[mask][city] = lastCity;
                    }
                }
            }
        }

        // Find the minimum cost of returning to the starting city
        int[] bestRoute = new int[n + 1];
        int mask = (1 << n) - 1;
        int lastCity = 0;
        int shortestDistance = Integer.MAX_VALUE;

        for (int i = 1; i < n; i++) {
            int routeCost = dp[mask][i] + distanceMatrix[i][0];

            if (routeCost < shortestDistance) {
                shortestDistance = routeCost;
                lastCity = i;
            }
        }

        // Reconstruct the path moving backwards
        bestRoute[n] = 0;
        for (int i = n - 1; i > 0; i--) {
            bestRoute[i] = lastCity;
            lastCity = parent[mask][lastCity];

            // After adding last city to the route, we get the subset where it is missing
            mask ^= (1 << bestRoute[i]);
        }

        bestRoute[0] = 0;

        return bestRoute;
    }
}

