package guzzolm.tutorial.tsp;

public class DistanceCalculator {
    private DistanceCalculator() {}

    public static int calculateTotalDistance(int[] route, int[][] distances){
        var totalDistance = 0;

        for (int i = 0; i < route.length - 1; i++) {
            var city = route[i];
            var nextCity = route[i + 1];
            totalDistance += distances[city][nextCity];
        }

        return totalDistance;
    }
}
