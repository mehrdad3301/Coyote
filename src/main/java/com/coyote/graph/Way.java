package com.coyote.graph;

import com.coyote.geo.Point;

import java.util.ArrayList;

import static com.coyote.geo.GeoMath.getHaversineDistance;

/**
 * way is an Open Street Map data structures. it will be used
 * to add arcs in road network's adjacency list.
 * <a href="https://wiki.openstreetmap.org/wiki/Way">...</a>
 */
public class Way {

    ArrayList<Integer> nodesIds ;
    HighWay type;

    public Way(ArrayList<Integer> nodeIds, HighWay type) {
        this.nodesIds = nodeIds;
        this.type = type;
    }

    /**
     * @return the cost of traversing from start to end in our model.
     * it is calculated by dividing haversine distance by speed limits,
     * which roughly estimates travel time in seconds.
     * this will be used as weight in our graph.
     */
    public int getCost(Node start, Node end) {
        return getCost(start.toPoint(), end.toPoint(), type) ;
    }

    public static int getCost(Point p1, Point p2, HighWay type) {
        double distance = getHaversineDistance(p1, p2) ;
        return (int) (distance / (1.00 * type.getSpeedLimit()) * 60 * 60) ;
    }

}
