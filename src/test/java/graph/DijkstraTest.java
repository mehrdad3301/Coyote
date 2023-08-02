package test.java.graph;

import main.java.com.routerunner.algorithms.Dijkstra;
import main.java.com.routerunner.graph.Graph;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DijkstraTest {
    @Test
    public void DijkstraSimple() throws Exception {
        Graph graph = Graph.buildFromOSM("./src/test/resources/test.osm");
        Dijkstra dij = new Dijkstra(graph) ;
        assertEquals(dij.computeShortestPath(1, 3), 12) ;
        assertEquals(dij.computeShortestPath(4, 3), 13) ;
        assertEquals(dij.computeShortestPath(4, 2), 17) ;
    }
}
