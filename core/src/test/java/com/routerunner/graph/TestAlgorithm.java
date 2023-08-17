package com.routerunner.graph;

import com.routerunner.algorithms.AStar;
import com.routerunner.algorithms.ATL;
import com.routerunner.algorithms.Dijkstra;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TestAlgorithm {
    @Test
    public void TestDijkstra() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_1.osm");
        Dijkstra dij = new Dijkstra(graph);
        assertEquals(dij.computeShortestPath(1, 3), 730);
        assertEquals(dij.computeShortestPath(4, 3), 828);
        assertEquals(dij.computeShortestPath(4, 2), 1049 );

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_2.osm");
        dij = new Dijkstra(graph);

        assertEquals(dij.computeShortestPath(4, 2), 616);
        assertEquals(dij.computeShortestPath(2, 3), 360);

        int cost = dij.computeShortestPath(4, 3) ;
        assertEquals(dij.getPath(3).toString(), "Path 104 - 324 > 101 - 528 > 103 ");
        assertEquals(dij.getNumSettledNodes(), 5);

        cost = dij.computeShortestPath(0, 6) ;
        assertEquals(dij.getPath(6).toString(), "Path 100 - 485 > 104 - 616 > 102 - 536 > 106 ");
        assertEquals(cost, 1637);
        assertEquals(dij.getNumSettledNodes(), 7);

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_3.osm");
        dij = new Dijkstra(graph);
        cost = dij.computeShortestPath(0, 7) ;
        assertEquals(dij.getPath(7).toString(), "Path 101 - 61211 > 113 - 19731 > 112 - 9765 > 111 - 15043 > 109 - 31120 > 108 ");
        assertEquals(cost,136870);
        assertEquals(dij.getNumSettledNodes(), 13);

        cost = dij.computeShortestPath(3, 10) ;
        assertEquals(dij.getPath(10).toString(), "Path 104 - 16118 > 102 - 14509 > 101 - 61211 > 113 - 19731 > 112 - 9765 > 111 ") ;
        assertEquals(cost, 121334);
        assertEquals(dij.getNumSettledNodes(), 10);
    }

    @Test
    public void TestAStar() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_3.osm");
        Dijkstra dij = new AStar(graph);
        int cost = dij.computeShortestPath(0, 11) ;
        assertEquals(dij.getPath(11).toString(), "Path 101 - 61211 > 113 - 19731 > 112 ") ;
        assertEquals(cost,80942);
        assertEquals(dij.getNumSettledNodes(),8);

        cost = dij.computeShortestPath(6, 11) ;
        assertEquals(dij.getPath(11).toString(), "Path 107 - 60190 > 108 - 31120 > 109 - 15043 > 111 - 9765 > 112 ") ;
        assertEquals(cost, 116118);
        assertEquals(dij.getNumSettledNodes(),8);
    }
    @Test
    public void TestATL() throws Exception {

        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_3.osm");
        ATL atl = new ATL(graph, 1);
        ArrayList<Integer> landmarks = new ArrayList<>() ;
        landmarks.add(2) ;
        landmarks.add(7) ;
        landmarks.add(12) ;
        atl.setLandmarks(landmarks) ;
        int cost = atl.computeShortestPath(4, 11) ;
        assertEquals(atl.getPath(11).toString(), "Path 105 - 10834 > 103 - 14815 > 101 - 61211 > 113 - 19731 > 112 ") ;
        assertEquals(cost, 106591);

        // case where source is a landmark
        cost = atl.computeShortestPath(2, 8) ;
        assertEquals(atl.getPath(8).toString(), "Path 103 - 14815 > 101 - 61211 > 113 - 19731 > 112 - 9765 > 111 - 15043 > 109 ") ;
    }

    @Test
    public void maxSettled() {
        Graph graph = buildSampleGraph() ;
        Dijkstra dij = new Dijkstra(graph);
        dij.setMaxSettledNodes(5);
        dij.computeShortestPath(0, 7) ;
        assertEquals(dij.getSettledIds().size(), 5) ;
    }

    @Test
    public void maxCost() {
        Graph graph = buildSampleGraph() ;
        Dijkstra dij = new Dijkstra(graph);
        dij.setCostUpperBound(20);
        dij.computeShortestPath(0, 7) ;
        assertEquals(dij.getSettledIds().size(), 5) ;
    }

    @Test
    public void TestArcFlags() {
        Graph graph = buildSampleGraph() ;
        Dijkstra dij = new Dijkstra(graph);
        graph.setAdjacentArcFlags(11, false);
        dij.setUseArcFlags(true);
        int cost = dij.computeShortestPath(0, 7) ;
        assertEquals(cost, 64);
        assertEquals(dij.getPath(7).toString(), "Path 0 - 4 > 2 - 6 > 3 - 7 > 4 - 7 > 5 - 20 > 6 - 20 > 7 ");

        dij.setUseArcFlags(false);
        cost = dij.computeShortestPath(0, 7) ;
        assertEquals(cost, 60);
        assertEquals(dij.getPath(7).toString(), "Path 0 - 30 > 11 - 15 > 9 - 5 > 8 - 10 > 7 ") ;

        graph.setAdjacentArcFlags(11, true);
        dij.setUseArcFlags(true);

        cost = dij.computeShortestPath(0, 7) ;
        assertEquals(cost, 60);
        assertEquals(dij.getPath(7).toString(), "Path 0 - 30 > 11 - 15 > 9 - 5 > 8 - 10 > 7 ") ;

    }

    @Test
    public void contractNode() {

    }
    @Test
    public void contractNodes() {
    }


    @Test
    public void TestLargestConnectedComponent() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/lcc_test_1.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """
                Graph{
                numEdges=3,
                numNodes=4,
                adjecency matrix= {
                	0= {
                		1:485
    
                	1= {
                		0:485
                		2:324
    
                	2= {
                		1:324
                		3:616
    
                	3= {
                		2:616
    
                }""");

        graph = Graph.buildFromOSM("./src/test/resources/lcc_test_2.osm");
        graph.reduceToLargestConnectedComponent();
        assertEquals(graph.toString(), """ 
                Graph{
                numEdges=4,
                numNodes=5,
                adjecency matrix= {
                	0= {
                		1:485
                                
                	1= {
                		0:485
                		2:324
                                
                	2= {
                		1:324
                		3:616
                		4:823
                                
                	3= {
                		2:616
                                
                	4= {
                		2:823
                                
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

    //this is similar to graph_test_3.osm, but easier to test without haversine costs
    public Graph buildSampleGraph() {

        Graph graph = new Graph() ;
        graph.addNode(new Node(0, 10, 10)) ;
        graph.addNode(new Node(1, 10, 10)) ;
        graph.addNode(new Node(2, 10, 10)) ;
        graph.addNode(new Node(3, 10, 10)) ;
        graph.addNode(new Node(4, 10, 10)) ;
        graph.addNode(new Node(5, 10, 10)) ;
        graph.addNode(new Node(6, 10, 10)) ;
        graph.addNode(new Node(7, 10, 10)) ;
        graph.addNode(new Node(8, 10, 10)) ;
        graph.addNode(new Node(9, 10, 10)) ;
        graph.addNode(new Node(10, 10, 10)) ;
        graph.addNode(new Node(11, 10, 10)) ;
        graph.addEdge(0, 1, 3) ;
        graph.addEdge(0, 2, 4) ;
        graph.addEdge(0, 11, 30) ;
        graph.addEdge(1, 2, 5) ;
        graph.addEdge(1, 3, 10) ;
        graph.addEdge(2, 3, 6) ;
        graph.addEdge(3, 5, 20) ;
        graph.addEdge(3, 4, 7) ;
        graph.addEdge(4, 5, 7) ;
        graph.addEdge(5, 6, 20) ;
        graph.addEdge(6, 7, 20) ;
        graph.addEdge(8, 7, 10) ;
        graph.addEdge(9, 8, 5) ;
        graph.addEdge(10, 8, 4) ;
        graph.addEdge(11, 9, 15) ;
        graph.addEdge(11, 10, 20) ;

        return graph ;
    }
}

