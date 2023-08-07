package com.routerunner.algorithms;

import com.routerunner.geo.Point;
import com.routerunner.graph.Arc;
import com.routerunner.graph.Graph;
import com.routerunner.graph.Node;
import com.routerunner.graph.Path;

import java.net.Inet4Address;
import java.util.*;

/**
 * Dijkstra implements dijkstra algorithm
 */
public class Dijkstra {

    final Graph graph ;

    // Indicator which node was visited by a particular run of Dijkstra. Useful
    // for computing the connected components
    final ArrayList<Integer> visited ;
    // parent pointers computed by last call to dijkstra
    final ArrayList<Integer> parents ;
    // distance from source to all settled nodes in dijkstra
    final ArrayList<Integer> distances ;
    // ids of nodes visited by a run of algorithm
    int mark ;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        visited = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        parents = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), -1)) ;
        distances = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), Integer.MAX_VALUE)) ;
        mark = 1 ;
    }

    public Path computeShortestPath(Point p1, Point p2) {
        return computeShortestPath(graph.getNode(p1), graph.getNode(p2)) ;
    }

    public Path computeShortestPath(int sourceNodeId, int targetNodeId) {
        clearLists();
        return getShortestPath(sourceNodeId, targetNodeId) ;
    }

    /**
     * computes the shortest path between a source and a destination
     * this function doesn't clear lists. to find out how this can be
     * useful, see LLC
     */
    protected Path getShortestPath(int sourceNodeId, int targetNodeId) {
        distances.set(sourceNodeId, 0) ;
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
            Pair e = pq.poll() ;
            if (e.id == targetNodeId) {
                visited.set(e.id, mark) ;
                break ;
            }
            for (Arc arc : graph.getAdjacent(e.id)) {
                int dst = arc.getHeadNodeId() ;
                if (visited.get(dst) != 0)
                    continue ;
                if (distances.get(dst) < e.distance + arc.getCost())
                    continue ;
                parents.set(dst, e.id) ;
                distances.set(dst, e.distance + arc.getCost()) ;
                pq.add(new Pair(arc.getHeadNodeId(), e.distance + arc.getCost()));
            }
            visited.set(e.id, mark) ;
        }

        if (targetNodeId != -1)
            return getPath(targetNodeId) ;
        return null ;
    }

    /**
     * constructs the Path from one run of dijkstra
     */
    private Path getPath(int targetNodeId) {
        ArrayList<Integer> nodeIds = new ArrayList<>() ;
        nodeIds.add(targetNodeId) ;
        int parent = parents.get(targetNodeId) ;
        while( parent != -1 ) {
            nodeIds.add(parent) ;
            parent = parents.get(parent) ;
        }

        Path path = new Path() ;
        Collections.reverse(nodeIds) ;
        for (int i = 0 ; i < nodeIds.size() - 1 ; i++) {
            int source = nodeIds.get(i) ;
            int dst = nodeIds.get(i + 1) ;

            int cost = distances.get(dst) - distances.get(source) ;
            path.addIntersection(graph.getNode(source), cost);
        }
        Node lastNode = graph.getNode(nodeIds.get(nodeIds.size() - 1)) ;
        path.addIntersection(lastNode, 0);
        return path ;
    }

    private void clearLists() {
        mark = 1 ;
        visited.replaceAll(ignored -> 0);
        distances.replaceAll(ignored -> Integer.MAX_VALUE);
        parents.replaceAll(ignored -> -1);
    }

    public int getNumVisitedNodes() {
        return visited.size() - Collections.frequency(visited, 0) ;
    }

    /**
     * Pair represents an element in the priority queue
     */
    static class Pair {
        int id ;
        int distance ;

        public Pair(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
    }
}
