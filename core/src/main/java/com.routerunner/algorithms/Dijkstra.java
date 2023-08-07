package com.routerunner.algorithms;

import com.routerunner.geo.Point;
import com.routerunner.graph.Arc;
import com.routerunner.graph.Graph;
import com.routerunner.graph.Node;
import com.routerunner.graph.Path;

import java.net.Inet4Address;
import java.util.*;

public class Dijkstra {

    final Graph graph ;

    // indicate whether a node was visited by last call to dijkstra
    final ArrayList<Integer> visited ;
    // parent pointers computed by last call to dijkstra
    final ArrayList<Integer> parents ;
    // distance from source to all settled nodes in dijkstra
    final ArrayList<Integer> distances ;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        visited = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        parents = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), -1)) ;
        distances = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), Integer.MAX_VALUE)) ;
    }


    public Path computeShortestPath(int sourceNodeId, int targetNodeId) {
        clearLists();
        distances.set(sourceNodeId, 0) ;
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
            Pair e = pq.poll() ;
            if (e.id == targetNodeId) {
                visited.set(e.id, 1) ;
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
            visited.set(e.id, 1) ;
        }

        return getPath(targetNodeId) ;
    }

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
        visited.replaceAll(ignored -> 0);
        distances.replaceAll(ignored -> Integer.MAX_VALUE);
        parents.replaceAll(ignored -> -1);
    }

    public int getNumVisitedNodes() {
        return visited.size() - Collections.frequency(visited, 0) ;
    }

    static class Pair {
        int id ;
        int distance ;

        public Pair(int id, int distance) {
            this.id = id;
            this.distance = distance;
        }
    }
}
