package com.routerunner.algorithms;

import com.routerunner.graph.Graph;
import com.routerunner.graph.HighWay;
import com.routerunner.graph.Node;
import com.routerunner.graph.Path;

import java.util.ArrayList;
import java.util.Collections;

import static com.routerunner.graph.Way.getCost;

public class AStar extends Dijkstra {

    public AStar(Graph graph) {
        super(graph);
    }

    /**
     * computeShortestPath first sets heuristics and then calls super
     * method in Dijkstra
     */
    public int computeShortestPath(int sourceNodeId, int targetNodeId) {
        setHeuristic(getHaversineHeuristic(targetNodeId));
        return super.computeShortestPath(sourceNodeId, targetNodeId);
    }

    /**
     * @return heuristics based on haversine distance to a targetNode
     */
    private ArrayList<Integer> getHaversineHeuristic(int targetNodeId) {
        ArrayList<Integer> heuristics = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0));
        Node targetNode = graph.getNode(targetNodeId);
        for (int i = 0; i < graph.getNumNodes(); i++) {
            int cost = getCost(graph.getNode(i).toPoint(), targetNode.toPoint(), HighWay.MOTORWAY);
            heuristics.set(i, cost);
        }
        return heuristics;
    }

}