package com.campusroute.exception;

/**
 * Se lanza cuando ninguna ruta de la coleccion cumple la preferencia
 * de desplazamiento solicitada (por ejemplo, todas estan bloqueadas).
 */
public class NoRouteAvailableException extends RuntimeException {

    public NoRouteAvailableException(String message) {
        super(message);
    }
}
