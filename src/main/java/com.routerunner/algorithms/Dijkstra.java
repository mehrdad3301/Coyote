package com.routerunner.algorithms;

import com.routerunner.graph.Arc;
import com.routerunner.graph.Graph;

import java.util.*;

public class Dijkstra {

    final Graph graph;
    int mark;
    ArrayList<Integer> visited;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        this.mark = 1;
    }

    /**
     * @return cost of the shortest path from sourceNodeId to targetNodeId,
     * if targetNodeId is -1, it finds the shortest path to all nodes and
     * then returns -1
     */
    public int getShortestPath(int sourceNodeId, int targetNodeId) {
        clearVisited();
        return computeShortestPath(sourceNodeId, targetNodeId);
    }

    /**
     * computeShortestPath is used by other algorithms, for example LCC. It's
     * callers responsibility to take care of visited array and handle it as they wish.
     */
    protected int computeShortestPath(int sourceNodeId, int targetNodeId) {
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance)) ;
        pq.add(new Pair(sourceNodeId, 0)) ;
        while (!pq.isEmpty()) {
            Pair e = pq.poll() ;
            if (e.id == targetNodeId) {
                return e.distance ;
            }
            for (Arc arc : graph.getAdjacent(e.id)) {
                if (visited.get(arc.getHeadNodeId()) != 0)
                    continue ;
                pq.add(new Pair(arc.getHeadNodeId(), e.distance + arc.getCost()));
            }
            visited.set(e.id, mark) ;
        }
        return -1 ;
    }


    public void clearVisited() {
        visited = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0));
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
