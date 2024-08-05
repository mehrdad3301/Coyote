package com.coyote.benchmarks;

import com.coyote.graph.Graph;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.infra.BenchmarkParams;

public class BenchLLC {

    @State(Scope.Benchmark)
    public static class GraphState {
        @Param ({"baden-wuerttemberg", "saarland"})
        public static String city;
        public Graph graph ;

        @Setup
        public void initState(BenchmarkParams params) throws Exception {
            switch (city) {
                case "baden-wuerttemberg" ->
                        graph = Graph.buildFromOSM("baden-wuerttemberg.osm");
                case "saarland" -> graph = Graph.buildFromOSM("saarland.osm");
            }
        }
    }

    @Benchmark
    public void LLC(GraphState state) {
        state.graph.reduceToLargestConnectedComponent();
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchLLC.class.getSimpleName())
                .forks(1)
                .mode(Mode.AverageTime)
                .warmupIterations(0)
                .measurementIterations(10)
                .build();

        new Runner(opt).run();
    }

}
