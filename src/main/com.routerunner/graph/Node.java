package main.com.routerunner.graph;

/*
Node represents a vertice in road network graph
 */
public class Node {
    long osrmId ;
    float lat ;
    float lon ;

    public Node(long osrmId, float lat, float lon) {
        this.osrmId = osrmId;
        this.lat = lat;
        this.lon = lon;
    }
}
