package com.campusroute.strategy;

import com.campusroute.domain.Route;

import java.util.List;

/**
 * Estrategia de seleccion de la mejor ruta dentro de una coleccion,
 * segun un criterio especifico (Strategy pattern).
 *
 * Cada implementacion encapsula unicamente la logica de UNA
 * preferencia de desplazamiento, de forma que agregar una nueva
 * preferencia no requiere modificar las estrategias existentes.
 */
public interface RouteSelectionStrategy {

    /**
     * Selecciona la mejor ruta entre las candidatas.
     *
     * @throws com.campusroute.exception.NoRouteAvailableException si ninguna ruta es valida.
     */
    Route selectBest(List<Route> candidateRoutes);
}
