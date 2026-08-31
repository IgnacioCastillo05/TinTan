package com.campusroute.service;

import com.campusroute.domain.Route;
import com.campusroute.domain.Segment;
import com.campusroute.domain.TravelPreference;
import com.campusroute.exception.NoRouteAvailableException;
import com.campusroute.strategy.RouteSelectionStrategyFactory;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RouteRecommendationServiceTest {

    private final RouteRecommendationService service =
            new RouteRecommendationService(new RouteSelectionStrategyFactory());

    @Test
    void recommendsFastestRouteBetweenGivenOriginAndDestination() {
        Route routeA = new Route("Ruta A", List.of(
                new Segment("Biblioteca", "Edificio A", 200, 12, 3, false, true)
        ));
        Route routeB = new Route("Ruta B", List.of(
                new Segment("Biblioteca", "Edificio A", 150, 8, 3, false, true)
        ));

        Route best = service.recommendRoute("Biblioteca", "Edificio A", TravelPreference.FASTEST,
                List.of(routeA, routeB));

        assertEquals("Ruta B", best.getName());
    }

    @Test
    void blockedRouteIsNeverRecommendedEvenIfItWouldOtherwiseWin() {
        Route blockedButFastest = new Route("Ruta bloqueada", List.of(
                new Segment("Biblioteca", "Edificio A", 200, 3, 3, false, false)
        ));
        Route availableSlower = new Route("Ruta disponible", List.of(
                new Segment("Biblioteca", "Edificio A", 200, 9, 3, false, true)
        ));

        Route best = service.recommendRoute("Biblioteca", "Edificio A", TravelPreference.FASTEST,
                List.of(blockedButFastest, availableSlower));

        assertEquals("Ruta disponible", best.getName());
    }

    @Test
    void ignoresRoutesThatDoNotMatchOriginOrDestination() {
        Route matching = new Route("Ruta correcta", List.of(
                new Segment("Biblioteca", "Edificio A", 200, 6, 3, false, true)
        ));
        Route otherDestination = new Route("Ruta a otro destino", List.of(
                new Segment("Biblioteca", "Cafeteria", 50, 2, 3, false, true)
        ));

        Route best = service.recommendRoute("Biblioteca", "Edificio A", TravelPreference.FASTEST,
                List.of(matching, otherDestination));

        assertEquals("Ruta correcta", best.getName());
    }

    @Test
    void throwsWhenNoRouteMatchesOriginAndDestination() {
        Route otherDestination = new Route("Ruta a otro destino", List.of(
                new Segment("Biblioteca", "Cafeteria", 50, 2, 3, false, true)
        ));

        assertThrows(NoRouteAvailableException.class, () ->
                service.recommendRoute("Biblioteca", "Edificio A", TravelPreference.FASTEST,
                        List.of(otherDestination)));
    }
}
