package com.routerunner.graph;

import org.junit.Test;

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

    }
}
