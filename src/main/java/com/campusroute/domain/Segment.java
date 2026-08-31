package com.campusroute.domain;

import java.util.Objects;

/**
 * Tramo elemental de una ruta dentro del campus.
 */
public class Segment {

    private final String origin;
    private final String destination;
    private final int distanceMeters;
    private final int estimatedTimeMinutes;
    private final int securityLevel;
    private final boolean hasStairs;
    private final boolean isAvailable;

    public Segment(String origin, String destination, int distanceMeters, int estimatedTimeMinutes,
                    int securityLevel, boolean hasStairs, boolean isAvailable) {
        this.origin = Objects.requireNonNull(origin, "origin");
        this.destination = Objects.requireNonNull(destination, "destination");
        this.distanceMeters = distanceMeters;
        this.estimatedTimeMinutes = estimatedTimeMinutes;
        this.securityLevel = securityLevel;
        this.hasStairs = hasStairs;
        this.isAvailable = isAvailable;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    public int getDistanceMeters() {
        return distanceMeters;
    }

    public int getEstimatedTimeMinutes() {
        return estimatedTimeMinutes;
    }

    public int getSecurityLevel() {
        return securityLevel;
    }

    public boolean hasStairs() {
        return hasStairs;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
