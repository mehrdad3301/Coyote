package com.routerunner.graph;

import com.routerunner.geo.Point;
import org.junit.Test;

import static com.routerunner.geo.GeoMath.getHaversineDistance;
import static com.routerunner.graph.Way.getCost;
import static org.junit.Assert.assertEquals;

public class TestWay {

    @Test
    public void TestCost() {

        Point p1 = new Point(35.77763F, 50.98885F) ;
        Point p2 = new Point(35.64366F, 51.37153F) ;
        assertEquals(getCost(p1, p2, HighWay.ROAD), 3386.0F, 0.5) ;

        p1 = new Point(35.1419F, 50.7701F) ;
        p2 = new Point(36.8792F, 36.8792F) ;
        assertEquals(getCost(p1, p2, HighWay.ROAD), 113679.9F, 0.5) ;
    }
}
