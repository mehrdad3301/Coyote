package com.routerunner.graph;

import com.routerunner.geo.Point;
import org.junit.Test;

import static com.routerunner.graph.Way.getCost;
import static org.junit.Assert.assertEquals;

public class TestGraph {

    @Test
    public void TestOSMParser() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_1.osm") ;
        assertEquals(graph.toString(), """
                Graph{
                numEdges=7,
                numNodes=6,
                adjecency matrix= {
                	0= {
                		4:485
                		1:194
                                
                	1= {
                		0:194
                		2:370
                                
                	2= {
                		5:668
                		3:360
                		1:370
                                
                	3= {
                		5:384
                		2:360
                                
                	4= {
                		0:485
                		5:444
                                
                	5= {
                		2:668
                		3:384
                		4:444
                                
                }""");

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_2.osm") ;
        assertEquals(graph.toString(), """
                Graph{
                numEdges=10,
                numNodes=7,
                adjecency matrix= {
                	0= {
                		4:485
                                
                	1= {
                		4:324
                		5:616
                		3:528
                		2:370
                                
                	2= {
                		1:370
                		5:668
                		3:360
                		4:616
                		6:536
                                
                	3= {
                		1:528
                		2:360
                		6:838
                                
                	4= {
                		0:485
                		1:324
                		2:616
                                
                	5= {
                		1:616
                		2:668
                                
                	6= {
                		2:536
                		3:838
                                
                }""") ;
        graph = Graph.buildFromOSM("./src/test/resources/graph_test_3.osm") ;
        assertEquals(graph.toString(), """
                Graph{
                numEdges=18,
                numNodes=13,
                adjecency matrix= {
                	0= {
                		1:14509
                		2:14815
                		12:61211
                                
                	1= {
                		0:14509
                		2:20352
                		3:16118
                                
                	2= {
                		0:14815
                		1:20352
                		3:21659
                		4:10834
                                
                	3= {
                		1:16118
                		2:21659
                		5:17835
                                
                	4= {
                		2:10834
                		5:17140
                		6:70024
                                
                	5= {
                		3:17835
                		4:17140
                                
                	6= {
                		4:70024
                		7:60190
                                
                	7= {
                		6:60190
                		8:31120
                                
                	8= {
                		7:31120
                		9:14353
                		10:15043
                                
                	9= {
                		8:14353
                		10:17924
                		11:11228
                                
                	10= {
                		8:15043
                		9:17924
                		11:9765
                                
                	11= {
                		9:11228
                		10:9765
                		12:19731
                                
                	12= {
                		0:61211
                		11:19731
                                
                }""") ;
    }

    @Test
    public void TestCost() {

        Point p1 = new Point(35.77763F, 50.98885F) ;
        Point p2 = new Point(35.64366F, 51.37153F) ;
        assertEquals(getCost(p1, p2, HighWay.ROAD), 3386.0F, 0.5) ;

        p1 = new Point(35.1419F, 50.7701F) ;
        p2 = new Point(36.8792F, 36.8792F) ;
        assertEquals(getCost(p1, p2, HighWay.ROAD), 113679.9F, 0.5) ;
    }
}
