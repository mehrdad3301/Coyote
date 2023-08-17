package com.routerunner.algorithms;


import com.routerunner.graph.Graph;

import java.util.*;

/**
 * CC stands for connected components. It uses dijkstra
 * to compute connected components.
 */
public class LCC {

    private final Dijkstra dijkstra ;
    private final Graph graph ;

    private final ArrayList<ArrayList<Integer>> components ;
    public LCC(Graph graph) {
        this.graph = graph ;
        this.dijkstra = new Dijkstra(graph) ;
        this.components = new ArrayList<>() ;
    }

    /**
     * reduceToLargestConnectedComponent keeps only the largest connected component
     * in the graph. Road networks mostly have one component that contains most nodes.
     */
    public void reduceToLargestComponent() {
        getAllComponents() ;
        reduceToComponent(getLargestId()) ;
    }

    /**
     * adds every connected component to member variable components by running
     * dijkstra until every node in the graph is visited
     *
     */
    private void getAllComponents() {
        ArrayList<Integer> seen = new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        for (int i = 0; i < graph.getNumNodes(); i++) {
                if (seen.get(i) != 0)
                    continue ;
                dijkstra.computeShortestPath(i, -1);
                for (int ids: dijkstra.getSettledIds()) {
                    seen.set(ids, 1);
                }
            components.add(dijkstra.getSettledIds()) ;
        }
    }

    /**
     * reduces the graph to the component pointed by index
     */
    private void reduceToComponent(int index) {
        graph.reduceNodes(components.get(index)) ;
    }

    /**
     * @return the id of the largest connected component
     */
    private int getLargestId() {

        int max = 0 ;
        int id = 0 ;
        for (int i = 0 ; i < components.size() ; i++) {
            if (components.get(i).size() > max) {
                max = components.get(i).size() ;
                id = i ;
            }
        }

        return id ;
    }
}
