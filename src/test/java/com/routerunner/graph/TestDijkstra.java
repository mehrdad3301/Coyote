package com.routerunner.graph;

import com.routerunner.algorithms.Dijkstra;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestDijkstra {
    @Test
    public void DijkstraSimple() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_1.osm");
        Dijkstra dij = new Dijkstra(graph);
        assertEquals(dij.getShortestPath(1, 3), 12);
        assertEquals(dij.getShortestPath(4, 3), 13);
        assertEquals(dij.getShortestPath(4, 2), 17);

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_2.osm");
        dij = new Dijkstra(graph);
        assertEquals(dij.getShortestPath(4, 2), 10);
        assertEquals(dij.getShortestPath(4, 3), 13);
        assertEquals(dij.getShortestPath(0, 6), 26);
        assertEquals(dij.getShortestPath(2, 3), 6);
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
                		2:8
                                
                	1= {
                		2:5
                		3:10
                                
                	2= {
                		0:8
                		1:5
                                
                	3= {
                		1:10
                                
                }""");

        graph = Graph.buildFromOSM("./src/test/resources/lcc_test_2.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """ 
                Graph{
                numEdges=4,
                numNodes=5,
                adjecency matrix= {
                	0= {
                		2:8
                        
                	1= {
                		2:5
                		3:10
                		4:13
                        
                	2= {
                		0:8
                		1:5
                        
                	3= {
                		1:10
                        
                	4= {
                		1:13
                        
                }""") ;

        graph = Graph.buildFromOSM("./src/test/resources/lcc_test_3.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """
                Graph{
                numEdges=3,
                numNodes=4,
                adjecency matrix= {
                	0= {
                		1:5
                		2:10
                		3:13
                        
                	1= {
                		0:5
                        
                	2= {
                		0:10
                        
                	3= {
                		0:13
                        
                }""") ;
    }
}

