package guzzolm.tutorial.tsp.model;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ProblemDefinition {

    public final String Name;
    public final String[] Cities;
    public final Map<String, Integer> Distances;

    public ProblemDefinition(String definition){
         Name = definition.split(",")[0];
         Cities = definition.split(",")[1].split("");
         Distances = Arrays.stream(definition.split(","))
                 .skip(2)
                 .map(x -> x.split(":"))
                 .collect(Collectors.toMap(
                         parts -> parts[0],
                         parts -> Integer.parseInt(parts[1])
                 ));
    }

    public int GetDistance(String city1, String city2){
        var id = city1.compareTo(city2) < 0 ? city1 + city2 : city2 + city1;

        return Distances.get(id);
    }
}
