package com.routerunner.algorithms;


import com.routerunner.graph.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * CC stands for connected components. It uses dijkstra
 * to compute connected components.
 */
public class LCC {

    private final Dijkstra dijkstra ;
    private final Graph graph ;

    public LCC(Graph graph) {
        this.dijkstra = new Dijkstra(graph) ;
        this.graph = graph ;
    }

    /**
     * reduceToLargestConnectedComponent keeps only the largest connected component
     * in road network. Road networks mostly have one component that contains most nodes.
     */
    public void reduceToLargestComponent() {
        findAllComponents() ;
        int maxId = getMaxComponentId() ;
        removeExtraNodes(maxId); ;
    }

    /**
     * finaAllComponents initializes visited array. It will then call dijkstra till every
     * node is visited. Each time it increments mark. the resulting visited array shows
     * every component in the dijkstra.graph. for instance in [1, 1, 1, 1, 2, 3, 2, 2] the connected
     * components are (0, 1, 2, 3) - (4, 6, 7) - (5)
     */
    private void findAllComponents() {
        dijkstra.clearVisited();
        for (int i = 0; i < graph.getNumNodes(); i++) {
            if (dijkstra.visited.get(i) == 0) {
                dijkstra.computeShortestPath(i, -1);
                dijkstra.mark++;
            }
        }
    }

    /**
     * removes nodes not present in largest connected component
     */
    private void removeExtraNodes(int maxId) {
        ArrayList<Integer> mask = new ArrayList<>(dijkstra.visited.size()) ;
        for (int num: dijkstra.visited) {
           if (num == maxId) {
              mask.add(1) ;
              continue ;
           }
            mask.add(0) ;
        }
        graph.removeNodes(mask) ;
    }

    private int getMaxComponentId() {
        HashMap<Integer, Integer> countMap = new HashMap<>();
        for (int num : dijkstra.visited) {
            countMap.put(num, countMap.getOrDefault(num, 0) + 1);
        }

        int maxKey = -1 ;
        int maxValue = 0 ;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue().compareTo(maxValue) > 0) {
                maxKey = entry.getKey();
                maxValue = entry.getValue();
            }
        }
        return maxKey ;
    }


}
