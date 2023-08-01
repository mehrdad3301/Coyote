package test.java.graph;

import main.java.com.routerunner.graph.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GraphTest {

    @Test
    public void TestOSMParser() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/test.osm") ;
        assertEquals(graph.toString(), "Graph{\n" +
                "numEdges=10,\n" +
                "numNodes=4,\n" +
                "adjecency matrix= {\n" +
                "\t0= 1,2,3,\n" +
                "\t1= 0,2,\n" +
                "\t2= 0,1,3,\n" +
                "\t3= 0,2,\n" +
                "}");
    }
}
