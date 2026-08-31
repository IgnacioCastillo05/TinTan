package com.campusroute.strategy;

import com.campusroute.domain.TravelPreference;

import java.util.EnumMap;
import java.util.Map;

/**
 * Resuelve la {@link RouteSelectionStrategy} correspondiente a una
 * {@link TravelPreference}.
 *
 * Incorporar una nueva preferencia (por ejemplo COVERED o SCENIC)
 * solo requiere crear una nueva clase que implemente
 * RouteSelectionStrategy y registrarla aqui: las estrategias
 * existentes no se modifican.
 */
public class RouteSelectionStrategyFactory {

    private final Map<TravelPreference, RouteSelectionStrategy> strategiesByPreference;

    public RouteSelectionStrategyFactory() {
        this(defaultStrategies());
    }

    public RouteSelectionStrategyFactory(Map<TravelPreference, RouteSelectionStrategy> strategiesByPreference) {
        this.strategiesByPreference = new EnumMap<>(strategiesByPreference);
    }

    private static Map<TravelPreference, RouteSelectionStrategy> defaultStrategies() {
        Map<TravelPreference, RouteSelectionStrategy> strategies = new EnumMap<>(TravelPreference.class);
        strategies.put(TravelPreference.FASTEST, new FastestRouteStrategy());
        strategies.put(TravelPreference.ACCESSIBLE, new AccessibleRouteStrategy());
        strategies.put(TravelPreference.SAFE, new SafeRouteStrategy());
        return strategies;
    }

    public RouteSelectionStrategy strategyFor(TravelPreference preference) {
        RouteSelectionStrategy strategy = strategiesByPreference.get(preference);
        if (strategy == null) {
            throw new IllegalArgumentException("No hay estrategia registrada para la preferencia: " + preference);
        }
        return strategy;
    }
}
