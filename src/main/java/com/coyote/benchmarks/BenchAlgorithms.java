package com.coyote.benchmarks;

import com.coyote.algorithms.Dijkstra;
import com.coyote.graph.Graph;

import static com.coyote.util.Random.getPositiveRandom;

public class BenchAlgorithms {

    private enum City {

        SAARLAND("benchmark/src/main/resources/saarland.osm"),
        BadenWuerttemberg("benchmark/src/main/resources/baden-wuerttemberg.osm") ;
       private final String fileAddress ;
       City(String address) {
           this.fileAddress = address ;
       }
       String getAddress() {
            return this.fileAddress ;
       }
    }
    private static Graph graph ;
    private static final int iterations = 10;
    private static void buildGraph(String address) throws Exception {
        graph = Graph.buildFromOSM(address) ;
        graph.reduceToLargestConnectedComponent();
    }


    public static void main(String[] args) throws Exception {

        for (City city: City.values()) {
            buildGraph(city.getAddress()) ;
            Dijkstra dij = new Dijkstra(graph) ;
            int sumCost = 0 ;
            int sumSettledNodes = 0 ;
            for(int i = 0 ; i < iterations ; i++) {
                int max = graph.getNumNodes() ;
                int source = getPositiveRandom(max) ;
                int target = getPositiveRandom(max) ;
                int cost = dij.computeShortestPath(source, target) ;
                sumCost += cost ;
                sumSettledNodes += dij.getVisitedNodeIds().size();
            }
            System.out.printf("avg cost in seconds         : %f\n", 1.0 * sumCost / iterations);
            System.out.printf("avg number of settled nodes : %d\n", sumSettledNodes / iterations);
        }

    }
}
