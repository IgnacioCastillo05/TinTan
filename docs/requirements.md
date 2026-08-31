# Requerimientos — CampusRoute

## Historias de usuario

### HU-01 — Recomendar la mejor ruta según la preferencia del usuario (obligatoria)

```text
Como usuario de CampusRoute
Quiero solicitar una ruta indicando mi preferencia de desplazamiento
Para recibir la alternativa que mejor se ajuste a mi necesidad.
```

**Criterios de aceptación:**

1. Dado un origen, un destino, una preferencia (`FASTEST`, `ACCESSIBLE` o `SAFE`) y una colección de rutas disponibles, el sistema retorna la ruta que mejor cumple esa preferencia entre las rutas que conectan ese origen con ese destino.
2. Toda ruta que contenga al menos un segmento con `isAvailable = false` se considera bloqueada y nunca es retornada como recomendación, sin importar qué tan bien puntúe en el criterio de la preferencia elegida.
3. Si ninguna ruta entre el origen y el destino indicados cumple la preferencia (por ejemplo, todas están bloqueadas), el sistema informa que no hay una ruta disponible en lugar de retornar un resultado inválido.

### HU-02 — Priorizar rutas sin escaleras para personas con movilidad reducida

```text
Como persona con movilidad reducida
Quiero pedir rutas que no incluyan escaleras
Para poder desplazarme por el campus sin barreras físicas.
```

**Criterios de aceptación:**

1. Al solicitar una ruta con preferencia `ACCESSIBLE`, ninguna ruta que contenga un segmento con `hasStairs = true` es retornada como resultado.
2. Entre las rutas sin escaleras y disponibles, el sistema retorna la de menor distancia total.

### HU-03 — Consultar rutas priorizando la seguridad

```text
Como usuario que se desplaza en horario nocturno o por zonas poco concurridas
Quiero pedir la ruta con mejor nivel de seguridad promedio
Para sentirme más seguro durante el trayecto.
```

**Criterios de aceptación:**

1. Al solicitar una ruta con preferencia `SAFE`, el sistema retorna la ruta disponible con el mayor promedio de `securityLevel` entre sus segmentos.
2. Si dos o más rutas empatan en seguridad promedio, el sistema retorna la de menor distancia total entre las empatadas.

### HU-04 — Ver rutas bloqueadas por mantenimiento

```text
Como usuario de CampusRoute
Quiero que el sistema descarte automáticamente los caminos cerrados por mantenimiento
Para no recibir una recomendación que no puedo recorrer en la práctica.
```

**Criterios de aceptación:**

1. Cuando un segmento se marca como `isAvailable = false` (por ejemplo, por una obra de mantenimiento), toda ruta que lo contenga queda excluida de cualquier recomendación, sin importar la preferencia solicitada.
2. Si todas las rutas registradas entre un origen y un destino quedan bloqueadas, el sistema no retorna una ruta y lo comunica explícitamente en lugar de fallar silenciosamente.

## Requerimiento no funcional

**RNF-01 — Extensibilidad de preferencias:** el diseño del módulo de selección de rutas debe permitir agregar una nueva preferencia de desplazamiento (por ejemplo `COVERED`, `LOW_CONGESTION`, `ENERGY_SAVING` o `SCENIC`) implementando una única clase nueva, sin modificar el código fuente de las preferencias ya existentes (`FASTEST`, `ACCESSIBLE`, `SAFE`) ni sus pruebas automatizadas.
