package com.routerunner.algorithms;

import com.routerunner.graph.Arc;
import com.routerunner.graph.Graph;

import java.net.Inet4Address;
import java.util.*;

public class Dijkstra {

    final Graph graph ;

    // indicate wether a node was visited by last call to dijkstra
    ArrayList<Integer> visited ;
    // parent pointers computed by last call to dijkstra
    ArrayList<Integer> parents ;
    // distance from source to all settled nodes in dijkstra
    ArrayList<Integer> distances ;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        visited = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        parents = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), -1)) ;
        distances = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), Integer.MAX_VALUE)) ;
    }


    public int computeShortestPath(int sourceNodeId, int targetNodeId) {
        clearLists();
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
            Pair e = pq.poll() ;
            if (e.id == targetNodeId) {
                return e.distance ;
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
        return -1 ;
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
