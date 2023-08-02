package test.java.graph;

import main.java.com.routerunner.graph.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void TestOSMParser() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/test.osm") ;
        assertEquals(graph.toString(), """
                Graph{
                numEdges=16,
                numNodes=6,
                adjecency matrix= {
                	0= {
                		4:8
                		5:13
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
                		0:13
                		2:11
                		3:6
                		4:7
                        
                }""");
    }
}
