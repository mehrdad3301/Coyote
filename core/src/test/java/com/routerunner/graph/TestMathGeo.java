package com.routerunner.graph;

import com.routerunner.geo.Point;
import org.junit.Test;

import static com.routerunner.geo.GeoMath.getHaversineDistance;
import static org.junit.Assert.assertEquals;

public class TestMathGeo {

    @Test
    public void TestHaversineDistance() {
        Point p1 = new Point(35.77763F, 50.98885F) ;
        Point p2 = new Point(35.64366F, 51.37153F) ;
        assertEquals(getHaversineDistance(p1, p2), 37.625663F, 0.001) ;
    }
}