package com.routerunner ;

import com.routerunner.graph.Graph ;

class Main {
    public static void main(String[] args) throws Exception {

        long startTime = System.currentTimeMillis();
        Graph g = Graph.buildFromOSM("./src/main/resources/baden-wuerttemberg.osm") ;
        g.reduceToLargestConnectedComponent();
        long endTime = System.currentTimeMillis();

        System.out.println((endTime - startTime) / 1000);
        System.out.println(g.getNumNodes()) ;
        System.out.println(g.getNumEdges()) ;

    }
}