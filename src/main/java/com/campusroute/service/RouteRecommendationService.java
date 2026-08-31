package com.campusroute.service;

import com.campusroute.domain.Route;
import com.campusroute.domain.TravelPreference;
import com.campusroute.exception.NoRouteAvailableException;
import com.campusroute.strategy.RouteSelectionStrategyFactory;

import java.util.List;

/**
 * Punto de entrada de la funcionalidad obligatoria: recibe origen,
 * destino, preferencia y la coleccion de rutas disponibles, y
 * retorna la mejor ruta posible.
 */
public class RouteRecommendationService {

    private final RouteSelectionStrategyFactory strategyFactory;

    public RouteRecommendationService(RouteSelectionStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    public Route recommendRoute(String origin, String destination, TravelPreference preference,
                                 List<Route> availableRoutes) {
        List<Route> matchingRoutes = availableRoutes.stream()
                .filter(route -> route.getOrigin().equals(origin) && route.getDestination().equals(destination))
                .toList();

        if (matchingRoutes.isEmpty()) {
            throw new NoRouteAvailableException(
                    "No hay rutas registradas entre " + origin + " y " + destination);
        }

        return strategyFactory.strategyFor(preference).selectBest(matchingRoutes);
    }
}
