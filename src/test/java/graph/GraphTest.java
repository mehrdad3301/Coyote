package test.java.graph;

import main.java.com.routerunner.graph.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void TestOSMParser() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/graph_test_1.osm") ;
        assertEquals(graph.toString(), """
                Graph{
                numEdges=7,
                numNodes=6,
                adjecency matrix= {
                	0= {
                		4:8
                		1:3
                        
                	1= {
                		0:3
                		2:6
                        
                	2= {
                		5:11
                		3:6
                		1:6
                        
                	3= {
                		5:6
                		2:6
                        
                	4= {
                		0:8
                		5:7
                        
                	5= {
                		2:11
                		3:6
                		4:7
                		
                }""");

        graph = Graph.buildFromOSM("./src/test/resources/graph_test_2.osm") ;
        assertEquals(graph.toString(), """
                Graph{
                numEdges=10,
                numNodes=7,
                adjecency matrix= {
                	0= {
                		4:8
                                 
                	1= {
                		4:5
                		5:10
                		3:8
                		2:6
                                 
                	2= {
                		1:6
                		5:11
                		3:6
                		4:10
                		6:8
                                 
                	3= {
                		1:8
                		2:6
                		6:13
                                 
                	4= {
                		0:8
                		1:5
                		2:10
                                 
                	5= {
                		1:10
                		2:11
                                 
                	6= {
                		2:8
                		3:13
                                 
                }""") ;

    }
}
