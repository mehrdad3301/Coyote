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
                numEdges=10,
                numNodes=4,
                adjecency matrix= {
                \t0= 1,2,3,
                \t1= 0,2,
                \t2= 0,1,3,
                \t3= 0,2,
                }""");
    }
}
