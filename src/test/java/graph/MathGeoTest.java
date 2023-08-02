package test.java.graph;

import main.java.com.routerunner.util.Point;
import org.junit.Test;

import static main.java.com.routerunner.util.GeoMath.getHaversineDistance;
import static org.junit.Assert.assertEquals;

public class MathGeoTest {

    @Test
    public void TestHaversineDistance() {
        Point p1 = new Point(35.77763F, 50.98885F) ;
        Point p2 = new Point(35.64366F, 51.37153F) ;
        assertEquals(getHaversineDistance(p1, p2), 37.625663F, 0.001) ;
    }
}
