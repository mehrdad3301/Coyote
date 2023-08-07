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

    private ArrayList<ArrayList<Integer>> components ;
    public LCC(Graph graph) {
        this.dijkstra = new Dijkstra(graph) ;
        this.graph = graph ;
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
        for (int i = 0; i < graph.getNumNodes(); i++) {
                if (dijkstra.visited.get(i) != 0)
                    continue ;
                dijkstra.getShortestPath(i, -1);
                dijkstra.mark ++ ;
        }
        components = new ArrayList<>(dijkstra.mark) ;
        for (int i = 0 ; i < dijkstra.mark ; i++) {
            components.add(new ArrayList<>());
        }
        for (int i = 0 ; i < dijkstra.visited.size() ; i++ ) {
            components.get(dijkstra.visited.get(i) - 1).add(i) ;
        }
    }

    /**
     * reduces the graph to the component pointed by index
     */
    private void reduceToComponent(int index) {
        graph.removeNodes(components.get(index)) ;
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
