package com.routerunner.algorithms;

import com.routerunner.graph.Graph;
import com.routerunner.graph.Path;

import java.util.ArrayList;

import static com.routerunner.util.Random.generateRandom;
import static java.lang.Math.abs;

public class Landmarks extends Dijkstra {

    // The set of landmarks. Each entry in the array is a node id
    private ArrayList<Integer> landmarks ;

    /**
     * Precomputed distances (shorted path costs in seconds) to and from these
     * landmarks. This is one array of size #nodes per landmark.
     * NOTE: since our graphs are undirected (or rather, for each arc u,v we also
     * have an arc v,u with the same cost) we have dist(u, l) = dist(l, u) and it
     * suffices to store one distance array per landmark. For arbitrary directed
     * graphs we would need one array for the distances *to* the landmark and one
     * array for the distances *from* the landmark.
     */
    private ArrayList<ArrayList<Integer>> landmarkDistances;

    public Landmarks(Graph graph) {
        super(graph);
        int defaultNumLandmarks = 42;
        setLandmarks(defaultNumLandmarks); ;
    }
    public Landmarks(Graph graph, int numLandmarks) {
        super(graph) ;
        setLandmarks(numLandmarks);
        precomputeLandmarkDistance();
    }

    /**
     * TODO replace with greedy landmark selection
     * sets landmarks to be used in algorithm. It chooses landmarks
     * at random. A better approach would be greedy landmark selection
     * The difference is in the number of landmarks. What can be achieved
     * with random landmark selection can probably be done with less number
     * of nodes in greedy selection methods.
     * @param num specifies number of landmarks
     */
    void setLandmarks(int num) {
        landmarks = generateRandom(num, 0, graph.getNumNodes()) ;
    }

    /**
     * Precompute the distances to and from the selected landmarks.
     * NOTE: For our undirected / symmetric graphs, the distances *from* the
     * landmarks are enough, see Array<Array<int>> landmarkDistances below.
     */
    void precomputeLandmarkDistance() {
        landmarkDistances = new ArrayList<>(landmarks.size()) ;
        for (int nodeId : landmarks) {
            computeShortestPath(nodeId, -1);
            landmarkDistances.add(distances);
        }
    }

    /**
    * Compute the shortest paths from the given source to the given target node,
    * using A* with the landmark heuristic.
    * NOTE: this algorithm only works in point-to-point mode, so the option
    * targetNodeId == -1 does not make sense here.
    */
    public Path computeShortestPath(int sourceNodeId, int targetNodeId) {
        setHeuristic(calcHueristics(targetNodeId));
        return super.computeShortestPath(sourceNodeId, targetNodeId) ;
    }

    /**
     * this computes heuristics for each node for every query which is
     * obviously unnecessary TODO create an interface for heuristic
     * @return as array of integers to be set as heuristics in dijkstra
     */
    private ArrayList<Integer> calcHueristics(int targetNodeId) {
        ArrayList<Integer> heuristics = new ArrayList<>() ;
        for (int i = 0 ; i < graph.getNumNodes() ; i++) {
            int max = -1 ;
            for (int l = 0 ; l < landmarks.size() ; l++) {
                int dist = abs(landmarkDistances.get(l).get(i) - landmarkDistances.get(l).get(targetNodeId));
                if (max < dist) {
                    max = dist ;
                }
            }
            heuristics.add(max) ;
        }
        return heuristics ;
    }
}

