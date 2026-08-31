package com.campusroute.strategy;

import com.campusroute.domain.Route;
import com.campusroute.domain.Segment;
import com.campusroute.exception.NoRouteAvailableException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FastestRouteStrategyTest {

    private final RouteSelectionStrategy fastest = new FastestRouteStrategy();

    private Route routeWithTime(String name, int minutes, boolean available) {
        return new Route(name, List.of(
                new Segment("Biblioteca", "Laboratorio H-301", 100, minutes, 3, false, available)
        ));
    }

    @Test
    void selectsRouteWithLowestTotalTime() {
        Route routeA = routeWithTime("Ruta A", 12, true);
        Route routeB = routeWithTime("Ruta B", 8, true);
        Route routeC = routeWithTime("Ruta C", 10, true);

        Route best = fastest.selectBest(List.of(routeA, routeB, routeC));

        assertEquals("Ruta B", best.getName());
    }

    @Test
    void blockedRouteCannotBeSelectedByFastest() {
        Route blockedFast = routeWithTime("Ruta bloqueada", 5, false);
        Route available = routeWithTime("Ruta disponible", 9, true);

        Route best = fastest.selectBest(List.of(blockedFast, available));

        assertEquals("Ruta disponible", best.getName());
    }

    @Test
    void throwsWhenAllRoutesAreBlocked() {
        Route blocked = routeWithTime("Ruta bloqueada", 5, false);

        assertThrows(NoRouteAvailableException.class, () -> fastest.selectBest(List.of(blocked)));
    }
}
