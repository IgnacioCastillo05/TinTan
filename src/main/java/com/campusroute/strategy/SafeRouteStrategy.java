package com.campusroute.strategy;

import com.campusroute.domain.Route;
import com.campusroute.exception.NoRouteAvailableException;

import java.util.Comparator;
import java.util.List;

/**
 * Preferencia SAFE: descarta rutas bloqueadas y selecciona la de
 * mayor seguridad promedio; en caso de empate, la de menor
 * distancia total.
 */
public class SafeRouteStrategy implements RouteSelectionStrategy {

    @Override
    public Route selectBest(List<Route> candidateRoutes) {
        return candidateRoutes.stream()
                .filter(route -> !route.isBlocked())
                .max(Comparator.comparingDouble(Route::averageSecurityLevel)
                        .thenComparing(Comparator.comparingInt(Route::totalDistanceMeters).reversed()))
                .orElseThrow(() -> new NoRouteAvailableException(
                        "No hay rutas disponibles para la preferencia SAFE"));
    }
}
