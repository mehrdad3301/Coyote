package main.java.com.routerunner.util;

import main.java.com.routerunner.graph.Node;

public class GeoMath {

    public static final float EarthRadiusKM = 6378.137F;


    public static int getHaversineDistance(Point start, Point end) {

        double dLat = end.lat * Math.PI - start.lat * Math.PI ;
        double dLon = end.lon * Math.PI - start.lon * Math.PI ;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(start.lat * Math.PI / 180) * Math.cos(end.lat * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = EarthRadiusKM * c;
        return (int) d * 1000; // meters
    }

}

