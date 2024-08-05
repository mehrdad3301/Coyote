package com.coyote.geo;

public class GeoMath {

    public static final float EarthRadiusKM = 6371.009F;

    /**
     * @return haversine distance between two points in kilometers.
     */
    public static double getHaversineDistance(Point start, Point end) {

        double dLat = Math.toRadians(end.lat - start.lat) ;
        double dLon = Math.toRadians(end.lon - start.lon) ;
        double a = Math.pow(Math.sin(dLat/2), 2) + Math.pow(Math.sin(dLon/2), 2) *
                Math.cos(Math.toRadians(start.lat)) * Math.cos(Math.toRadians(end.lat)) ;
        return 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)) * EarthRadiusKM ;
    }


}

