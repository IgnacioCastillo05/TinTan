package com.campusroute.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteTest {

    @Test
    void calculatesTotalDistanceAsSumOfSegments() {
        Route route = new Route("Ruta 1", List.of(
                new Segment("Biblioteca", "Edificio A", 150, 3, 4, false, true),
                new Segment("Edificio A", "Laboratorio H-301", 100, 2, 4, false, true)
        ));

        assertEquals(250, route.totalDistanceMeters());
    }

    @Test
    void calculatesTotalTimeAsSumOfSegments() {
        Route route = new Route("Ruta 1", List.of(
                new Segment("Biblioteca", "Edificio A", 150, 3, 4, false, true),
                new Segment("Edificio A", "Laboratorio H-301", 100, 2, 4, false, true)
        ));

        assertEquals(5, route.totalTimeMinutes());
    }

    @Test
    void calculatesAverageSecurityLevel() {
        Route route = new Route("Ruta 2", List.of(
                new Segment("Biblioteca", "Cafeteria", 80, 2, 4, false, true),
                new Segment("Cafeteria", "Plazoleta", 60, 1, 5, false, true)
        ));

        assertEquals(4.5, route.averageSecurityLevel());
    }

    @Test
    void routeWithUnavailableSegmentIsBlocked() {
        Route route = new Route("Ruta bloqueada", List.of(
                new Segment("Biblioteca", "Edificio A", 150, 3, 4, false, true),
                new Segment("Edificio A", "Laboratorio H-301", 100, 2, 4, false, false)
        ));

        assertTrue(route.isBlocked());
    }

    @Test
    void routeWithAllSegmentsAvailableIsNotBlocked() {
        Route route = new Route("Ruta disponible", List.of(
                new Segment("Biblioteca", "Edificio A", 150, 3, 4, false, true)
        ));

        assertFalse(route.isBlocked());
    }

    @Test
    void routeHasStairsWhenAnySegmentHasStairs() {
        Route route = new Route("Ruta con escaleras", List.of(
                new Segment("Biblioteca", "Edificio A", 150, 3, 4, true, true),
                new Segment("Edificio A", "Laboratorio H-301", 100, 2, 4, false, true)
        ));

        assertTrue(route.hasStairs());
    }
}
