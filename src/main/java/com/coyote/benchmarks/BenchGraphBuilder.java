package com.coyote.benchmarks;

import com.coyote.graph.Graph;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


public class BenchGraphBuilder {

    @Benchmark
    public void buildBadenWuGraph() throws Exception {
        Graph.buildFromOSM("baden-wuerttemberg.osm") ;
    }
    @Benchmark
    public void buildSaarlandGraph() throws Exception {
        Graph.buildFromOSM("saarland.osm") ;
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(BenchGraphBuilder.class.getSimpleName())
                .forks(1)
                .mode(Mode.AverageTime)
                .warmupIterations(0)
                .measurementIterations(3)
                .build();

        new Runner(opt).run();
    }
}

