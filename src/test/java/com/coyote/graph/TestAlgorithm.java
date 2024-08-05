package com.coyote.graph;

import com.coyote.algorithms.AStar;
import com.coyote.algorithms.ATL;
import com.coyote.algorithms.CH;
import com.coyote.algorithms.Dijkstra;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

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
        assertEquals(cost, 116118);
        assertEquals(dij.getNumSettledNodes(),8);
        assertEquals(dij.getPath(11).toString(), "Path 107 - 60190 > 108 - 31120 > 109 - 15043 > 111 - 9765 > 112 ") ;
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
        atl.computeShortestPath(2, 8) ;
        assertEquals(atl.getPath(8).toString(), "Path 103 - 14815 > 101 - 61211 > 113 - 19731 > 112 - 9765 > 111 - 15043 > 109 ") ;
    }

    @Test
    public void TestMaxSettled() {
        Graph graph = buildGraphTestOne() ;
        Dijkstra dij = new Dijkstra(graph);
        dij.setMaxSettledNodes(5);
        dij.computeShortestPath(0, 7) ;
        assertEquals(dij.getNumSettledNodes(), 5) ;
    }

    @Test
    public void TestMaxCost() {
        Graph graph = buildGraphTestOne() ;
        Dijkstra dij = new Dijkstra(graph);
        dij.setCostUpperBound(20);
        dij.computeShortestPath(0, 7) ;
        assertEquals(dij.getNumSettledNodes(), 6) ;
    }

    @Test
    public void TestRestrictedNodes() {
    //    Graph graph = buildGraphTest1() ;
    //    Dijkstra dij = new Dijkstra(graph);
    //    graph.setAdjacentArcFlags(11, false);
    //    dij.setUseArcFlags(true);
    //    int cost = dij.computeShortestPath(0, 7) ;
    //    assertEquals(cost, 64);
    //    assertEquals(dij.getPath(7).toString(), "Path 0 - 4 > 2 - 6 > 3 - 7 > 4 - 7 > 5 - 20 > 6 - 20 > 7 ");

    //    dij.setUseArcFlags(false);
    //    cost = dij.computeShortestPath(0, 7) ;
    //    assertEquals(cost, 60);
    //    assertEquals(dij.getPath(7).toString(), "Path 0 - 30 > 11 - 15 > 9 - 5 > 8 - 10 > 7 ") ;

    //    graph.setAdjacentArcFlags(11, true);
    //    dij.setUseArcFlags(true);

    //    cost = dij.computeShortestPath(0, 7) ;
    //    assertEquals(cost, 60);
    //    assertEquals(dij.getPath(7).toString(), "Path 0 - 30 > 11 - 15 > 9 - 5 > 8 - 10 > 7 ") ;
    }

    @Test
    public void TestContractNode() {
        Graph graph = buildGraphTestTwo() ;
        CH ch = new CH(graph) ;

        int initialNumEdges = graph.getNumEdges() ;
        ch.contractNode(0);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;

        assertEquals(graph.edgeExists(10, 2, 3), false) ;
        assertEquals(graph.edgeExists(10, 3, 4), false) ;
        ch.contractNode(11);
        assertEquals(graph.edgeExists(10, 2, 3), true) ;
        assertEquals(graph.edgeExists(10, 3, 4), true) ;
        assertEquals(initialNumEdges + 2, graph.getNumEdges()) ;
        initialNumEdges += 2 ;

        assertEquals(graph.edgeExists(1, 9, 6), false) ;
        assertEquals(graph.edgeExists(7, 9, 3), false) ;
        ch.contractNode(12);
        assertEquals(graph.edgeExists(1, 9, 6), true) ;
        assertEquals(graph.edgeExists(7, 9, 3), true) ;
        assertEquals(initialNumEdges + 2, graph.getNumEdges()) ;
        initialNumEdges += 2 ;

        ch.contractNode(5);
        ch.contractNode(4);
        ch.contractNode(8);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;

        ch.contractNode(7);
        assertEquals(initialNumEdges + 1, graph.getNumEdges()) ;
        initialNumEdges += 1 ;

        ch.contractNode(1);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;

        ch.contractNode(9);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;

        assertEquals(graph.edgeExists(6, 2, 6), false) ;
        ch.contractNode(10);
        assertEquals(initialNumEdges + 1, graph.getNumEdges()) ;
        assertEquals(graph.edgeExists(6, 2, 6), true) ;
        initialNumEdges += 1 ;

        ch.contractNode(6);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;

        ch.contractNode(3);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;

        ch.contractNode(2);
        assertEquals(initialNumEdges, graph.getNumEdges()) ;
    }
    @Test
    public void TestContractNodes() {
        ArrayList<Integer> nodeOrdering = new ArrayList<>(Arrays.asList(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)); ;
        Graph graph = buildGraphTestTwo() ;
        int initialNumEdges = graph.getNumEdges() ;
        CH ch = new CH(graph) ;
        ch.setNodeOrdering(nodeOrdering);
        ch.contractNodes();
        assertEquals(initialNumEdges + 2, graph.getNumEdges());
        assertEquals(graph.edgeExists(12, 10, 2), true);
        assertEquals(graph.edgeExists(12, 11, 3), true);
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
    public Graph buildGraphTestOne() {

        Graph graph = new Graph() ;
        // we ignore latitude and longitudes when using this
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
    public Graph buildGraphTestTwo() {

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
        graph.addNode(new Node(12, 10, 10)) ;

        graph.addEdge(0, 1, 3) ;
        graph.addEdge(0, 12, 4) ;
        graph.addEdge(0, 5, 7) ;
        graph.addEdge(1, 12, 5) ;
        graph.addEdge(1, 7, 2) ;
        graph.addEdge(2, 11, 2) ;
        graph.addEdge(2, 3, 4) ;
        graph.addEdge(2, 4, 5) ;
        graph.addEdge(3, 6, 4) ;
        graph.addEdge(3, 11, 3) ;
        graph.addEdge(4, 5, 6) ;
        graph.addEdge(4, 10, 3) ;
        graph.addEdge(5, 9, 4) ;
        graph.addEdge(6, 8, 7) ;
        graph.addEdge(6, 10, 3) ;
        graph.addEdge(7, 12, 2) ;
        graph.addEdge(7, 8, 5) ;
        graph.addEdge(8, 9, 3) ;
        graph.addEdge(9, 10, 1) ;
        graph.addEdge(9, 12, 1) ;
        graph.addEdge(10, 11, 1) ;

        return graph ;
    }
}

