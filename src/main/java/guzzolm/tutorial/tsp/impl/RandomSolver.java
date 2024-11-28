package guzzolm.tutorial.tsp.impl;

import guzzolm.tutorial.tsp.TspSolver;

import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class RandomSolver implements TspSolver {
    @Override
    public int[] solve(int[][] distanceMatrix) {
        var cities = new ArrayList<Integer>();

        for (int i = 1; i < distanceMatrix.length; i++) {
            cities.add(i);
        }

        Collections.shuffle(cities, new Random());

        var route = new int[distanceMatrix.length + 1];
        for (int i = 0; i < cities.size(); i++) {
            route[i+1] = cities.get(i);
        }

        return route;
    }

    @Override
    public String getName() {
        return "RandomSolver";
    }
}
