package com.routerunner.graph;

import com.routerunner.geo.Point;

/**
 * Node is an Open Street Map data structure. it will represent
 * vertices in our graph model.
 * <a href="https://wiki.openstreetmap.org/wiki/Node">...</a>
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

    public Point toPoint() {
        return new Point(lat, lon) ;
    }
}
