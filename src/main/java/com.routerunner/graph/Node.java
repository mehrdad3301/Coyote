package main.java.com.routerunner.graph;

import main.java.com.routerunner.util.Point;

/**
Node represents a vertex in road network graph
 */
public class Node {
    long osmId ;
    float lat ;
    float lon ;

    public Node(long osrmId, float lat, float lon) {
        this.osmId = osrmId;
        this.lat = lat;
        this.lon = lon;
    }

    public long getOsmId() {
        return osmId;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public Point toPoint() {
        return new Point(lat, lon) ;
    }
}
