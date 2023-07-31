package main.com.routerunner.graph;

public class Arc {
    int headNodeId ;
    int cost ;

    public Arc(int headNodeId, int cost) {
        this.headNodeId = headNodeId;
        this.cost = cost;
    }
}
