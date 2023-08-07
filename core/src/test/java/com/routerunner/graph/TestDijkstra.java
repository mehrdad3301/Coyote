package com.routerunner.graph;

import com.routerunner.algorithms.Dijkstra;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDijkstra {
    @Test
    public void DijkstraSimple() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_1.osm");
        Dijkstra dij = new Dijkstra(graph);
        assertEquals(dij.computeShortestPath(1, 3).getCost(), 730);
        assertEquals(dij.computeShortestPath(4, 3).getCost(), 828);
        assertEquals(dij.computeShortestPath(4, 2).getCost(), 1049 );

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_2.osm");
        dij = new Dijkstra(graph);
        assertEquals(dij.computeShortestPath(4, 2).getCost(), 616);
        Path path = dij.computeShortestPath(4, 3) ;
        assertEquals(path.toString(), "Path 104 - 324 > 101 - 528 > 103 - 0 > ");
        assertEquals(dij.computeShortestPath(2, 3).getCost(), 360);
        path = dij.computeShortestPath(0, 6) ;
        assertEquals(path.toString(), "Path 100 - 485 > 104 - 616 > 102 - 536 > 106 - 0 > ");
        assertEquals(path.getCost(), 1637);
    }

    @Test
    public void LargestConnectedComponent() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/lcc_test_1.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """
                Graph{
                numEdges=3,
                numNodes=4,
                adjecency matrix= {
                	0= {
                		2:485
                                
                	1= {
                		2:324
                		3:616
                                
                	2= {
                		0:485
                		1:324
                                
                	3= {
                		1:616
                                
                }""");

        graph = Graph.buildFromOSM("./src/test/resources/lcc_test_2.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """ 
                Graph{
                numEdges=4,
                numNodes=5,
                adjecency matrix= {
                	0= {
                		2:485
                                
                	1= {
                		2:324
                		3:616
                		4:823
                                
                	2= {
                		0:485
                		1:324
                                
                	3= {
                		1:616
                                
                	4= {
                		1:823
                                
                }""") ;

        graph = Graph.buildFromOSM("./src/test/resources/lcc_test_3.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """
                Graph{
                numEdges=3,
                numNodes=4,
                adjecency matrix= {
                	0= {
                		1:324
                		2:616
                		3:823
                                
                	1= {
                		0:324
                                
                	2= {
                		0:616
                                
                	3= {
                		0:823
                                
                }""") ;
    }
}

