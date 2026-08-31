package com.campusroute.strategy;

import com.campusroute.domain.Route;
import com.campusroute.domain.Segment;
import com.campusroute.exception.NoRouteAvailableException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessibleRouteStrategyTest {

    private final RouteSelectionStrategy accessible = new AccessibleRouteStrategy();

    private Route routeWithDistance(String name, int distance, boolean hasStairs, boolean available) {
        return new Route(name, List.of(
                new Segment("Biblioteca", "Laboratorio H-301", distance, 5, 3, hasStairs, available)
        ));
    }

    @Test
    void discardsRouteThatHasStairs() {
        Route withStairs = routeWithDistance("Ruta A", 300, true, true);
        Route withoutStairs = routeWithDistance("Ruta B", 420, false, true);

        Route best = accessible.selectBest(List.of(withStairs, withoutStairs));

        assertEquals("Ruta B", best.getName());
    }

    @Test
    void selectsValidRouteWithLowestDistance() {
        Route withStairs = routeWithDistance("Ruta A", 300, true, true);
        Route longer = routeWithDistance("Ruta B", 420, false, true);
        Route shorter = routeWithDistance("Ruta C", 380, false, true);

        Route best = accessible.selectBest(List.of(withStairs, longer, shorter));

        assertEquals("Ruta C", best.getName());
    }

    @Test
    void throwsWhenNoAccessibleRouteExists() {
        Route withStairs = routeWithDistance("Ruta con escaleras", 300, true, true);
        Route blocked = routeWithDistance("Ruta bloqueada", 200, false, false);

        assertThrows(NoRouteAvailableException.class,
                () -> accessible.selectBest(List.of(withStairs, blocked)));
    }
}
