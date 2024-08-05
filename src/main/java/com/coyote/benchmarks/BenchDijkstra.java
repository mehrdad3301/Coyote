package com.coyote.benchmarks;

import com.coyote.algorithms.Dijkstra;
import com.coyote.graph.Graph;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

import static com.coyote.util.Random.getPositiveRandom;

public class BenchDijkstra {

    @State(Scope.Benchmark)
    public static class GraphState {
        @Param ({"baden-wuerttemberg", "saarland"})
        public static String city;
        public Graph graph ;

        private void buildGraph(String fileAddress) throws Exception {
            graph = Graph.buildFromOSM(fileAddress) ;
            graph.reduceToLargestConnectedComponent();
        }

        @Setup
        public void initState(BenchmarkParams params) throws Exception {
            switch (city) {
                case "baden-wuerttemberg" ->
                        buildGraph("benchmark/src/main/resources/baden-wuerttemberg.osm");
                case "saarland" -> buildGraph("benchmark/src/main/resources/saarland.osm");
            }
        }
    }

    @Benchmark
    public void Dijkstra(GraphState state) {
        int max = state.graph.getNumNodes() ;
        int source = getPositiveRandom(max) ;
        int target = getPositiveRandom(max) ;
        new Dijkstra(state.graph).computeShortestPath(source, target) ;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchDijkstra.class.getSimpleName())
                .forks(1)
                .mode(Mode.AverageTime)
                .warmupIterations(0)
                .measurementIterations(20)
                .timeUnit(TimeUnit.MILLISECONDS)
                .build();

        new Runner(opt).run();

    }

}