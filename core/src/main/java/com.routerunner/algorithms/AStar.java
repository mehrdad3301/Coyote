package com.routerunner.algorithms;

import com.routerunner.graph.Graph;
import com.routerunner.graph.HighWay;
import com.routerunner.graph.Node;
import com.routerunner.graph.Path;

import java.util.ArrayList;
import java.util.Collections;

import static com.routerunner.graph.Way.getCost;

/**
 * AStar implements A* algorithm by setting heuristics set by dijkstra
 */
public class AStar extends Dijkstra {

    private final Heuristic heuristicFunc ;

    public AStar(Graph graph) {
        super(graph);
        heuristicFunc = Heuristic.HAVERSINE_HEURISTIC ;
    }
    public AStar(Graph graph, Heuristic heuristic) {
        super(graph);
        heuristicFunc = heuristic ;
    }

    public Path computeShortestPath(int sourceNodeId, int targetNodeId) {
        setHeuristic(heuristicFunc.getHeuristics(graph, targetNodeId));
        return super.computeShortestPath(sourceNodeId, targetNodeId) ;
    }

    /**
     * Heuristic defines multiple heuristic functions. Anything that implements
     * getHeuristic can be regarded as a heuristic function.
     */
    public enum Heuristic {
        HAVERSINE_HEURISTIC {
            @Override
            public ArrayList<Integer> getHeuristics(Graph graph, int targetNodeId) {
                ArrayList<Integer> heuristics = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
                Node targetNode = graph.getNode(targetNodeId) ;
                for (int i = 0 ; i < graph.getNumNodes() ; i++) {
                    int cost = getCost(graph.getNode(i).toPoint(), targetNode.toPoint(), HighWay.MOTORWAY) ;
                    heuristics.set(i, cost) ;
                }
                return heuristics;
            }
        },
        EUCLIDEAN_HEURISTIC {
            @Override
            public ArrayList<Integer> getHeuristics(Graph graph, int targetNodeId) {
                // TODO Implement the euclidean heuristic here
                return new ArrayList<>();
            }
        };

        /**
         * getHeuristic returns an ArrayList which can be used as heuristic array in dijkstra
         * @param targetNodeId destination node id for which heuristics are computed
         */
        public abstract ArrayList<Integer> getHeuristics(Graph graph, int targetNodeId);
    }
}
