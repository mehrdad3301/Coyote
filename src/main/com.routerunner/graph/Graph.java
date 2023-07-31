package main.com.routerunner.graph;

import java.util.ArrayList;

/*
Graph represents undering road network. It uses an adjacency list
to capture relations between nodes.
 */
public class Graph {

    private long numEdges ;
    private long numNodes ;
    private final ArrayList<ArrayList<Arc>> adjacencyList ;
    private final ArrayList<Node> nodes ;

    public Graph() {
        this.numEdges = 0;
        this.numNodes = 0 ;
        this.nodes = new ArrayList<>();
        this.adjacencyList = new ArrayList<>();
    }
    public void addNode(long osmId, float lat, float lon) {
        Node node = new Node(osmId, lat, lon) ;
        this.nodes.add(node) ;
    }

    public void addEdge(int from, int to, int cost) {
        Arc arc = new Arc(to, cost) ;
        this.adjacencyList.get(from).add(arc) ;
    }

    public Graph buildFromOSM(String fileAddress) {
        Graph graph = new Graph() ;
        return graph ;
    }

}
