package com.campusroute.strategy;

import com.campusroute.domain.Route;
import com.campusroute.domain.Segment;
import com.campusroute.exception.NoRouteAvailableException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SafeRouteStrategyTest {

    private final RouteSelectionStrategy safe = new SafeRouteStrategy();

    private Route routeWithSecurityAndDistance(String name, int security, int distance) {
        return new Route(name, List.of(
                new Segment("Biblioteca", "Laboratorio H-301", distance, 5, security, false, true)
        ));
    }

    @Test
    void selectsRouteWithHighestAverageSecurity() {
        Route routeA = routeWithSecurityAndDistance("Ruta A", 4, 350);
        Route routeB = routeWithSecurityAndDistance("Ruta B", 5, 500);

        Route best = safe.selectBest(List.of(routeA, routeB));

        assertEquals("Ruta B", best.getName());
    }

    @Test
    void usesDistanceToBreakSecurityTie() {
        Route routeA = routeWithSecurityAndDistance("Ruta A", 4, 350);
        Route routeB = new Route("Ruta B", List.of(
                new Segment("Biblioteca", "Laboratorio H-301", 250, 5, 4, false, true),
                new Segment("Laboratorio H-301", "Plazoleta", 250, 5, 5, false, true)
        ));
        Route routeC = new Route("Ruta C", List.of(
                new Segment("Biblioteca", "Laboratorio H-301", 200, 5, 4, false, true),
                new Segment("Laboratorio H-301", "Plazoleta", 220, 5, 5, false, true)
        ));

        Route best = safe.selectBest(List.of(routeA, routeB, routeC));

        assertEquals("Ruta C", best.getName());
    }

    @Test
    void throwsWhenAllRoutesAreBlocked() {
        Route blocked = new Route("Ruta bloqueada", List.of(
                new Segment("Biblioteca", "Laboratorio H-301", 200, 5, 5, false, false)
        ));

        assertThrows(NoRouteAvailableException.class, () -> safe.selectBest(List.of(blocked)));
    }
}
