package com.routerunner.algorithms;

import com.routerunner.geo.Point;
import com.routerunner.graph.*;

import java.util.*;

/**
 * this class implements dijkstra algorithm. It also serves as
 * a base class for AStar. It might have been better to define
 * an abstract class from which both AStar and Dijkstra inherit
 */
public class Dijkstra {

    public final Graph graph ;
    // Indicator which node was settled by a particular run of Dijkstra. Useful
    // for computing the connected components
    final ArrayList<Integer> settled;
    // The ids of the nodes visited by the last call of
    // the computeShortestPath method above.
    ArrayList<Integer> visitedNodeIds ;
    // parent pointers computed by last call to dijkstra
    final ArrayList<Integer> parents ;
    // distance from source to all settled nodes in dijkstra
    final ArrayList<Integer> distances ;
    // heuristics used in cost function, see AStar
    ArrayList<Integer> heuristic ;
    // stop dijkstra after this number of nodes are settled
    // Default is MAX_INT
    int maxSettledNodes ;
    // stop dijkstra when a node with greater cost than this is settled
    // Default is MAX_INT
    int costUpperBound ;
    // whether to use accessNodes
    boolean filterNodes;

    // whether it's possible for dijkstra to access a node or not
    ArrayList<Boolean> accessNodes;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        settled = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        parents = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), -1)) ;
        distances = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), Integer.MAX_VALUE)) ;
        heuristic = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        accessNodes = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), true)) ;
        visitedNodeIds = new ArrayList<>() ;
        costUpperBound = Integer.MAX_VALUE ;
        maxSettledNodes = Integer.MAX_VALUE ;
        filterNodes = false ;
    }

    public int computeShortestPath(Point p1, Point p2) {
        return computeShortestPath(graph.getClosestNode(p1), graph.getClosestNode(p2)) ;
    }

    public int computeShortestPath(int sourceNodeId, int targetNodeId) {
        clearLists();
        return getShortestPath(sourceNodeId, targetNodeId) ;
    }

    /**
     * computes the shortest path between a source and a destination
     * this function doesn't clear lists which can be useful when we
     * want to run dijkstra multiple times. see LLC for a use case
     */
    protected int getShortestPath(int sourceNodeId, int targetNodeId) {
        int numSettledNodes = 0 ;
        distances.set(sourceNodeId, 0) ;
        visitedNodeIds.add(sourceNodeId) ;
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
            Pair e = pq.poll() ;
            if (settled.get(e.id) != 0)
                continue ;
            settled.set(e.id, 1) ;
            numSettledNodes ++ ;
            // return when reached the target
            if (e.id == targetNodeId) {
                visitedNodeIds.add(targetNodeId) ;
                break ;
            }
            // if cost upper bound has been reached
            if (distances.get(e.id) > costUpperBound)
                break ;
            // if the number of settled nodes has reached the maximum
            if (numSettledNodes >= maxSettledNodes)
                break ;
            // adding adjacent nodes
            for (Arc arc : graph.getAdjacent(e.id)) {
                if (filterNodes && !accessNodes.get(arc.getHeadNodeId()))
                   continue ;
                int dst = arc.getHeadNodeId() ;
                int cost = distances.get(e.id) + arc.getCost() ;
                // ignore the node if already settled
                if (settled.get(dst) != 0)
                    continue ;
                // ignore the node if there is already a better path
                if (distances.get(dst) < cost)
                    continue ;
                parents.set(dst, e.id) ;
                // relax the cost
                distances.set(dst, cost) ;
                visitedNodeIds.add(dst) ;
                pq.add(new Pair(dst, cost + heuristic.get(dst)));
            }
        }

        if (targetNodeId != -1)
            return distances.get(targetNodeId) ;
        return -1;
    }

    /**
     * constructs the Path from one run of dijkstra. It extracts
     * the path from arrays filled by the algorithm
     */
    public Path getPath(int targetNodeId) {
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

    public Path getPath(Point point) {
        return getPath(graph.getClosestNode(point)) ;
    }

    private void clearLists() {
        for (int id: visitedNodeIds) {
            settled.set(id, 0) ;
            distances.set(id, Integer.MAX_VALUE) ;
            parents.set(id, -1) ;
        }
        visitedNodeIds = new ArrayList<>() ;
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
        return visitedNodeIds ;
    }
    public int getNumSettledNodes() {
        int cnt = 0 ;
        for (int i: settled)
            if (i == 1)
                cnt ++ ;
        return cnt ;
    }

    public void setMaxSettledNodes(int maxSettledNodes) {
        this.maxSettledNodes = maxSettledNodes;
    }

    public void setCostUpperBound(int costUpperBound) {
        this.costUpperBound = costUpperBound;
    }

    public void filterNodes(boolean filterNodes) {
        this.filterNodes = filterNodes;
    }

    public void setAccessNode(int id, boolean access) {
        this.accessNodes.set(id, access) ;
    }

    public boolean getNodeAccess(int id) {
        return  accessNodes.get(id) ;
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
