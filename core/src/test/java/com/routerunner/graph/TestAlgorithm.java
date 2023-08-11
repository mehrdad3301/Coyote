package com.routerunner.graph;

import com.routerunner.algorithms.AStar;
import com.routerunner.algorithms.Dijkstra;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TestAlgorithm {
    @Test
    public void Dijkstra() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_1.osm");
        Dijkstra dij = new Dijkstra(graph);
        assertEquals(dij.computeShortestPath(1, 3).getCost(), 730);
        assertEquals(dij.computeShortestPath(4, 3).getCost(), 828);
        assertEquals(dij.computeShortestPath(4, 2).getCost(), 1049 );

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_2.osm");
        dij = new Dijkstra(graph);

        assertEquals(dij.computeShortestPath(4, 2).getCost(), 616);
        assertEquals(dij.computeShortestPath(2, 3).getCost(), 360);

        Path path = dij.computeShortestPath(4, 3) ;
        assertEquals(path.toString(), "Path 104 - 324 > 101 - 528 > 103 ");
        assertEquals(dij.getNumVisitedNodes(), 5);

        path = dij.computeShortestPath(0, 6) ;
        assertEquals(path.toString(), "Path 100 - 485 > 104 - 616 > 102 - 536 > 106 ");
        assertEquals(path.getCost(), 1637);
        assertEquals(dij.getNumVisitedNodes(), 7);

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_3.osm");
        dij = new Dijkstra(graph);
        path = dij.computeShortestPath(0, 7) ;
        assertEquals(path.toString(), "Path 101 - 61211 > 113 - 19731 > 112 - 9765 > 111 - 15043 > 109 - 31120 > 108 ");
        assertEquals(dij.getNumVisitedNodes(), 13);
        assertEquals(path.getCost(),136870);

        path = dij.computeShortestPath(3, 10) ;
        assertEquals(path.toString(), "Path 104 - 16118 > 102 - 14509 > 101 - 61211 > 113 - 19731 > 112 - 9765 > 111 ") ;
        assertEquals(path.getCost(), 121334);
        assertEquals(dij.getNumVisitedNodes(), 10);
    }

    @Test
    public void AStar() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_3.osm");
        Dijkstra dij = new AStar(graph);
        Path path = dij.computeShortestPath(0, 11) ;
        assertEquals(dij.getNumVisitedNodes(),8);
        assertEquals(path.toString(), "Path 101 - 61211 > 113 - 19731 > 112 ") ;
        assertEquals(path.getCost(),80942);

        path = dij.computeShortestPath(6, 11) ;
        assertEquals(path.toString(), "Path 107 - 60190 > 108 - 31120 > 109 - 15043 > 111 - 9765 > 112 ") ;
        assertEquals(dij.getNumVisitedNodes(),8);
        assertEquals(path.getCost(), 116118);
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

