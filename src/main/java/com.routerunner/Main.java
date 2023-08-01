package main.java.com.routerunner ;

import main.java.com.routerunner.graph.Graph ;

class Main {
    public static void main(String[] args) throws Exception {


        Graph g = Graph.buildFromOSM("./src/main/resources/saarland.osm") ;
        
        System.out.println("Hello, World!");

    }
}