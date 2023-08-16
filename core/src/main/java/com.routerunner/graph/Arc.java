package com.routerunner.graph;

/**
 * Arc is used as an edge in road network graph.
 * cost corresponds to edge weight and headNodeId is id of the node
 * the arc is pointing to. for example in A -------> B, B is headNode.
 */
public class Arc {
    int headNodeId;
    int cost;

    boolean arcFlag ;

    public Arc(int headNodeId, int cost) {
        this.headNodeId = headNodeId;
        this.cost = cost;
        this.arcFlag = true ;
    }

    public int getHeadNodeId() {
        return headNodeId;
    }

    public int getCost() {
        return cost;
    }

    public boolean getArcFlag() {
        return arcFlag;
    }

    public void setArcFlag(boolean flag) {
            this.arcFlag = flag ;
    }
}
