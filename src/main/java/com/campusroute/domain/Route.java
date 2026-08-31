package com.campusroute.domain;

import java.util.List;
import java.util.Objects;

/**
 * Ruta compuesta por uno o mas segmentos entre un origen y un destino.
 */
public class Route {

    private final String name;
    private final List<Segment> segments;

    public Route(String name, List<Segment> segments) {
        this.name = Objects.requireNonNull(name, "name");
        if (segments == null || segments.isEmpty()) {
            throw new IllegalArgumentException("A route must contain at least one segment");
        }
        this.segments = List.copyOf(segments);
    }

    public String getName() {
        return name;
    }

    public List<Segment> getSegments() {
        return segments;
    }

    public String getOrigin() {
        return segments.get(0).getOrigin();
    }

    public String getDestination() {
        return segments.get(segments.size() - 1).getDestination();
    }

    public int totalDistanceMeters() {
        return segments.stream().mapToInt(Segment::getDistanceMeters).sum();
    }

    public int totalTimeMinutes() {
        return segments.stream().mapToInt(Segment::getEstimatedTimeMinutes).sum();
    }

    public double averageSecurityLevel() {
        return segments.stream().mapToInt(Segment::getSecurityLevel).average().orElse(0);
    }

    public boolean hasStairs() {
        return segments.stream().anyMatch(Segment::hasStairs);
    }

    public boolean isBlocked() {
        return segments.stream().anyMatch(segment -> !segment.isAvailable());
    }
}
