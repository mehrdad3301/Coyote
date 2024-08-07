package com.coyote.graph;

import java.util.LinkedList;

/**
 * Path represents the route returned by various
 * sp algorithms
 */
public class Path {
    LinkedList<Intersection> path ;

    public Path() {
        this.path = new LinkedList<>();
    }

    public void addIntersection(Node node, int weight) {
        Intersection intersection = new Intersection(node, weight);
        path.add(intersection) ;
    }

    public int getCost() {

        int cost = 0 ;
        for(Intersection intersection : path) {
            cost += intersection.weight ;
        }
        return cost ;
    }

    public LinkedList<Intersection> getPath() {
        return path;
    }

    public String toString() {
        StringBuilder s = new StringBuilder("Path ");
        for (Intersection intersection: path) {
            s.append(intersection.node.osmId).append(" - ").
                    append(intersection.weight).append(" > ") ;
        }
        return s.subSequence(0, s.length() - 6).toString() ;
    }

    /**
     * intersection is any node on a path
     * it has a member weight, which is the cost
     * from this node to the next on the path
     */
    public static class Intersection {
        public Node node ;
        public int weight ;

        public Intersection(Node node, int weight) {
            this.node = node;
            this.weight = weight;
        }
    }
}
