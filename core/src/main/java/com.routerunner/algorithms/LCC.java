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
        this.dijkstra = new Dijkstra(graph) ;
        this.components = new ArrayList<>() ;
        this.graph = graph ;
    }

    /**
     * reduceToLargestConnectedComponent keeps only the largest connected component
     * in road network. Road networks mostly have one component that contains most nodes.
     */
    public void reduceToLargestComponent() {
        getAllComponents() ;
        int index = getLargestComponent() ;
        reduceToComponent(index) ;
    }

    /**
     */
    private void getAllComponents() {
        ArrayList<Integer> seen =  new ArrayList<>(Collections.nCopies(graph.getNumNodes(), 0)) ;
        for (int i = 0; i < graph.getNumNodes(); i++) {
                if (seen.get(i) == 1)
                    continue ;
                dijkstra.computeShortestPath(i, -1);
                ArrayList<Integer> component = new ArrayList<>(dijkstra.getNumVisitedNodes()) ;
                for (int j = 0 ; j < dijkstra.visited.size() ; j++) {
                    if (dijkstra.visited.get(j) == 1) {
                        component.add(j);
                        seen.set(j, 1) ;
                    }
                }
                components.add(component) ;
        }
    }

    private void reduceToComponent(int index) {
        graph.removeNodes(components.get(index)) ;
    }

    private int getLargestComponent() {
        int max = 0 ;
        int index = 0 ;
        for (int i = 0 ; i < components.size() ; i++) {
            if (components.get(i).size() > max) {
                max = components.get(i).size() ;
                index = i ;
            }
        }
        return index ;
    }
}
