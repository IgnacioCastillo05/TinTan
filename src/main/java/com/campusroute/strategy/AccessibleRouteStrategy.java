package com.campusroute.strategy;

import com.campusroute.domain.Route;
import com.campusroute.exception.NoRouteAvailableException;

import java.util.Comparator;
import java.util.List;

/**
 * Preferencia ACCESSIBLE: descarta rutas bloqueadas y rutas con
 * escaleras, y selecciona entre las restantes la de menor distancia.
 */
public class AccessibleRouteStrategy implements RouteSelectionStrategy {

    @Override
    public Route selectBest(List<Route> candidateRoutes) {
        return candidateRoutes.stream()
                .filter(route -> !route.isBlocked())
                .filter(route -> !route.hasStairs())
                .min(Comparator.comparingInt(Route::totalDistanceMeters))
                .orElseThrow(() -> new NoRouteAvailableException(
                        "No hay rutas accesibles disponibles para la preferencia ACCESSIBLE"));
    }
}
