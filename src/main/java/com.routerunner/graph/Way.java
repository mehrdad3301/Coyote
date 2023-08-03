package com.routerunner.graph;

import java.util.ArrayList;

import static com.routerunner.geo.GeoMath.getHaversineDistance;

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
     * which roughly estimates travel time in minutes.
     * this will be used as weight in our graph.
     */
    public int getCost(Node start, Node end) {
        double distance = getHaversineDistance(start.toPoint(), end.toPoint()) ;
        return (int) (distance / (1.00 * type.getSpeedLimit()) * 60) ;
    }

}
