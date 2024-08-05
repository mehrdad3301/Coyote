package com.coyote.algorithms;

import com.coyote.graph.Arc;
import com.coyote.graph.Graph;
import com.coyote.util.Random;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;


/**
 * implementing contraction hierarchies algorithm
 * <p>
 * <a href="http://algo2.iti.uka.de/schultes/hwy/contract.pdf">Original Papaer</a>
 * <a href="https://ad-wiki.informatik.uni-freiburg.de/teaching/EfficientRoutePlanningSS2012">Freiburg lectures</a>
 */

public class CH {

    //The road network graph.
    //NOTE: the contraction hierarchies algorithm *modifies* this graph by adding
    //be aware that by using this class, the original graph will be modified.
    Graph graph;

    // Object for the various execution of  Dijkstra's algorithm on the augmented
    // graph.
    Dijkstra dijkstra;

    // The ordering of the nodes. This is simply a permutation of {0, ..., n-1},
    // where n is the number of nodes; nodeOrdering[i] simply contains the index
    // of the i-th node in the ordering, for i = 0, ..., n-1.
    ArrayList<Integer> nodeOrdering;

    // maximum number of settled nodes while performing dijkstra in contraction step
    private final int maxSettledNodes = 30 ;

    public CH(Graph graph) {
        this.graph = graph;
        this.dijkstra = new Dijkstra(graph);
        computeRandomNodeOrdering();
    }

    /**
     * Compute random node ordering.
     * IMPLEMENTATION NOTE: simply compute a random permutation of the set {0,
     * ..., n-1} as follows. Initialize nodeOrdering with 0, 1, ..., n - 1. Swap
     * nodeOrdering[0] with a random entry to the right. Then swap nodeOrdering[1]
     * with a random entry on the right. And so on. Whatever you do, make sure
     * your implementation takes only linear time.
     */
    void computeRandomNodeOrdering() {
        nodeOrdering = Random.permutation(graph.getNumNodes()) ;
    }

    /**
     * Central contraction routine: contract the i-th node in the ordering,
     * ignoring nodes 1, ..., i - 1 in the ordering and their adjacent arcs.
     * IMPLEMENTATION NOTE: To ignore nodes (and their adjacent arcs), you can
     * simply use the arcFlag member of the Arc class. Initially, set all arcFlags
     * to 1, and as you go along and contract nodes, simply set the flags of the
     * arcs adjacent to contracted nodes to 0. And make sure to call
     * setConsiderArcFlags(true) on the dijkstra  object below.
     */
    public void contractNode(int id) {
        dijkstra.filterNodes(true);
        dijkstra.setAccessNode(id, false);
        for(Arc arc: graph.getAdjacent(id)) {
            if (dijkstra.getNodeAccess(arc.getHeadNodeId())) {
                addShortcuts(id, arc);
            }
        }
    }

    /**
     * takes `id` of the contracting node and adds necessary shortcuts
     * for node pointed to by arc.headNodeId. It first computes max cost
     * of traveling through arc to nodes adjacent to `id`, and sets upper bound
     * search cost for dijkstra. After that runs the dijkstra and if it didn't
     * find a path shorter than path through `id` it will add a shortcut.
     * @param id id of the node being contracted
     * @param arc arc to the node for which we might add shortcuts
     */
    private void addShortcuts(int id, Arc arc) {
        int maxWeight = -1;
        for (Arc adjArc: graph.getAdjacent(id)) {
            if (dijkstra.getNodeAccess(adjArc.getHeadNodeId())) {
                if (adjArc.getHeadNodeId() != arc.getHeadNodeId()) {
                    if (maxWeight < adjArc.getCost() + arc.getCost()) {
                        maxWeight = adjArc.getCost() + arc.getCost();
                    }
                }
            }
        }
        dijkstra.setCostUpperBound(maxWeight);
        dijkstra.setMaxSettledNodes(maxSettledNodes);
        dijkstra.computeShortestPath(arc.getHeadNodeId(), -1) ;
        for (Arc adjArc: graph.getAdjacent(id)) {
            if (dijkstra.getNodeAccess(adjArc.getHeadNodeId())) {
                if (adjArc.getHeadNodeId() != arc.getHeadNodeId()) {
                    int cost = arc.getCost() + adjArc.getCost();
                    if (dijkstra.distances.get(adjArc.getHeadNodeId()) > cost) {
                        graph.addEdge(arc.getHeadNodeId(), adjArc.getHeadNodeId(), cost);
                    }
                }
            }
        }
    }

    /**
     * performs contraction step in CH algorithm. It inserts node
     * based on nodeOrdering into a priority queue and then calls contractNode.
     */
    public void contractNodes() {
        dijkstra.filterNodes(true);
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.order));
        for (int i = 0 ; i < graph.getNumNodes() ; i++) {
            pq.add(new Pair(i, nodeOrdering.get(i))) ;
        }
        while (!pq.isEmpty()) {
            Pair p = pq.poll() ;
            contractNode(p.nodeId) ;
        }
    }

    public void setNodeOrdering(ArrayList<Integer> nodeOrdering) {
        this.nodeOrdering = nodeOrdering;
    }

    static class Pair {
        Integer nodeId;
        Integer order ;
        public Pair(Integer nodeId, Integer order) {
            this.nodeId = nodeId;
            this.order = order;
        }
    }

}










