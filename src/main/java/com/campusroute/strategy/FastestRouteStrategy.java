package com.campusroute.strategy;

import com.campusroute.domain.Route;
import com.campusroute.exception.NoRouteAvailableException;

import java.util.Comparator;
import java.util.List;

/**
 * Preferencia FASTEST: descarta rutas bloqueadas y selecciona
 * la de menor tiempo total.
 */
public class FastestRouteStrategy implements RouteSelectionStrategy {

    @Override
    public Route selectBest(List<Route> candidateRoutes) {
        return candidateRoutes.stream()
                .filter(route -> !route.isBlocked())
                .min(Comparator.comparingInt(Route::totalTimeMinutes))
                .orElseThrow(() -> new NoRouteAvailableException(
                        "No hay rutas disponibles para la preferencia FASTEST"));
    }
}
