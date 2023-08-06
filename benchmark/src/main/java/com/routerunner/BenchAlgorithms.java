package com.routerunner;

import com.routerunner.algorithms.Dijkstra;
import com.routerunner.graph.Graph;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

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
                int source = ThreadLocalRandom.current().nextInt(0, max);
                int target = ThreadLocalRandom.current().nextInt(0, max);
                int cost = dij.getShortestPath(source, target) ;
                sumCost += cost ;
                sumSettledNodes += dij.getNumVisitedNodes();
            }
            System.out.printf("avg cost in seconds         : %f\n", 1.0 * sumCost / iterations);
            System.out.printf("avg number of settled nodes : %d\n", sumSettledNodes / iterations);
        }

    }
}
