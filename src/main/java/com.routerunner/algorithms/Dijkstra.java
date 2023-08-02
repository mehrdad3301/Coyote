package main.java.com.routerunner.algorithms;

import main.java.com.routerunner.graph.Arc;
import main.java.com.routerunner.graph.Graph;
import main.java.com.routerunner.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;

public class Dijkstra {

    private final Graph graph ;

    public Dijkstra(Graph graph) {
        this.graph = graph ;
    }

    public int computeShortestPath(int sourceNodeId, int targetNodeId) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        ArrayList<Boolean> visited = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), false));

        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
           Pair e = pq.poll() ;
           if (e.id == targetNodeId) {
               return e.distance ;
           }
           for (Arc arc : graph.getAdjacenct(e.id)) {
               if (visited.get(arc.getHeadNodeId()))
                   continue ;
               pq.add(new Pair(arc.getHeadNodeId(), e.distance + arc.getCost()));
           }
           visited.set(e.id, true) ;
        }
        return -1 ;
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
