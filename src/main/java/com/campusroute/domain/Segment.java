package com.campusroute.domain;

import java.util.Objects;

/**
 * Tramo elemental de una ruta dentro del campus.
 */
public record Segment(
        String origin,
        String destination,
        int distanceMeters,
        int estimatedTimeMinutes,
        int securityLevel,
        boolean hasStairs,
        boolean isAvailable
) {
    public Segment {
        Objects.requireNonNull(origin, "origin");
        Objects.requireNonNull(destination, "destination");
    }
}
