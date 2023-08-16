package com.routerunner.algorithms;

import com.routerunner.geo.Point;
import com.routerunner.graph.*;

import java.util.*;

import static com.routerunner.graph.Way.getCost;

/**
 * this class implements dijkstra algorithm. It also serves as
 * a base class for AStar. It might have been better to define
 * an abstract class from which both AStar and Dijkstra inherit
 */
public class Dijkstra {

    public final Graph graph ;
    // Indicator which node was visited by a particular run of Dijkstra. Useful
    // for computing the connected components
    final ArrayList<Integer> visited ;
    // parent pointers computed by last call to dijkstra
    final ArrayList<Integer> parents ;
    // distance from source to all settled nodes in dijkstra
    final ArrayList<Integer> distances ;
    // heuristics used in cost function, see AStar
    ArrayList<Integer> heuristic ;
    int mark ;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        visited = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        parents = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), -1)) ;
        distances = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), Integer.MAX_VALUE)) ;
        heuristic = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        mark = 1 ;
    }

    public Path computeShortestPath(Point p1, Point p2) {
        return computeShortestPath(graph.getClosestNode(p1), graph.getClosestNode(p2)) ;
    }

    public Path computeShortestPath(int sourceNodeId, int targetNodeId) {
        clearLists();
        return getShortestPath(sourceNodeId, targetNodeId) ;
    }

    /**
     * computes the shortest path between a source and a destination
     * this function doesn't clear lists which can be useful when we
     * want to run dijkstra multiple times. see LLC for a use case
     */
    protected Path getShortestPath(int sourceNodeId, int targetNodeId) {
        distances.set(sourceNodeId, 0) ;
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
            Pair e = pq.poll() ;
            if (visited.get(e.id) != 0) {
                continue ;
            }
            if (e.id == targetNodeId) {
                visited.set(e.id, mark) ;
                break ;
            }
            for (Arc arc : graph.getAdjacent(e.id)) {
                if (!arc.getArcFlag())
                    continue ;
                int dst = arc.getHeadNodeId() ;
                int cost = distances.get(e.id) + arc.getCost() ;
                if (visited.get(dst) != 0)
                    continue ;
                if (distances.get(dst) < cost)
                    continue ;
                parents.set(dst, e.id) ;
                distances.set(dst, cost) ;
                pq.add(new Pair(dst, cost + heuristic.get(dst)));
            }
            visited.set(e.id, mark) ;
        }

       if (targetNodeId != -1)
           return getPath(targetNodeId) ;
       return null ;
    }

    /**
     * constructs the Path from one run of dijkstra. It extracts
     * the path from arrays filled by the algorithm
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
        Collections.fill(visited, 0);
        Collections.fill(distances, Integer.MAX_VALUE);
        Collections.fill(parents, -1);
    }
    public void setHeuristic(ArrayList<Integer> heuristic) {
        this.heuristic = heuristic;
    }

    /**
     * @return a list of ids that shows every node settled during
     * dijkstra, that means nodes for which we know the cost of shortest path
     * after running dijkstra
     */
    public ArrayList<Integer> getSettledIds() {
        ArrayList<Integer> ids = new ArrayList<>() ;
        for (int i = 0 ; i < visited.size() ; i++) {
            if (visited.get(i) != 0) {
                ids.add(i);
            }
        }
        return ids;
    }
    public int getNumVisitedNodes() {
        return visited.size() - Collections.frequency(visited, 0) ;
    }

    /**
     * Pair represents an element in the priority queue. distance shows
     * cost for reaching that node from source in dijkstra algorithm
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
