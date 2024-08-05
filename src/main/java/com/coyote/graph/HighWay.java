package com.coyote.graph;

import java.util.HashMap;
import java.util.Map;

/**
 * HighWay is an Open Street Map feature. ways are
 * tagged with HighWay.
 * <a href="https://wiki.openstreetmap.org/wiki/Map_features#Highway">...</a>
 */

public enum HighWay {
    MOTORWAY("motorway", 110),
    TRUNK("trunk", 110),
    PRIMARY("primary", 70),
    SECONDARY("secondary", 60),
    TERTIARY("tertiary", 50),
    MOTORWAY_LINK("motorway_link", 50),
    TRUNK_LINK("trunk_link", 50),
    PRIMARY_LINK("primary_link", 50),
    SECONDARY_LINK("secondary_link", 50),
    ROAD("road", 40),
    UNCLASSIFIED("unclassified", 40),
    RESIDENTIAL("residential", 30),
    UNSURFACED("unsurfaced", 30),
    LIVING_STREET("living_street", 10),
    SERVICE("service", 5);

    private final String type;
    private final int speedLimit;

    private static final Map<String, HighWay> BY_NAME = new HashMap<>();

    // static block to initialize map during class loading
    static {
        for (HighWay way : values()) {
            BY_NAME.put(way.type, way);
        }
    }

    HighWay(String name, int speedLimit) {
        this.type = name;
        this.speedLimit = speedLimit;
    }

    public int getSpeedLimit() {
        return this.speedLimit;
    }

    /**
     * @return return HighWay by looking up the type.
     * it returns UNCLASSIFIED is type if not found.
     */
    public static HighWay getByName(String type) {
        if (BY_NAME.get(type) != null)
            return BY_NAME.get(type) ;
        return null;
    }
}
