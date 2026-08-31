# Planeación Agile — HU-01: Recomendación de rutas

> Nota: este documento contiene el contenido que debe replicarse en un
> proyecto de Jira (ver sección "Jira" en el `README.md`). Se generó aquí
> porque esta sesión de trabajo no tuvo un conector/credenciales de Jira
> disponibles para crear el proyecto directamente.

## Historia de usuario

**Título:** Recomendar la mejor ruta según la preferencia del usuario

```text
Como usuario de CampusRoute
Quiero solicitar una ruta indicando mi preferencia de desplazamiento
Para recibir la alternativa que mejor se ajuste a mi necesidad.
```

- **Tipo de issue:** Historia (Story)
- **Prioridad:** Alta (es la única funcionalidad obligatoria del parcial)
- **Story Points:** 8
- **Épica sugerida:** Recomendación de rutas (CampusRoute v1)

### Criterios de aceptación

1. Dado un origen, un destino, una preferencia (`FASTEST`, `ACCESSIBLE` o `SAFE`) y una colección de rutas, el sistema retorna la ruta que mejor cumple esa preferencia entre las rutas que conectan ese origen con ese destino.
2. Ninguna ruta bloqueada (con al menos un segmento `isAvailable = false`) puede ser retornada, sin importar la preferencia.
3. Si no existe ninguna ruta válida, el sistema lo informa explícitamente en lugar de retornar un resultado inconsistente.

## Tareas técnicas

| # | Tarea | Tipo | Estimación |
|---|-------|------|------------|
| 1 | Modelar el dominio `Segment` y `Route` (distancia, tiempo, seguridad, disponibilidad, escaleras) con pruebas unitarias | Modelo de dominio | 2 pts |
| 2 | Implementar `RouteSelectionStrategy` + estrategias `FASTEST`, `ACCESSIBLE` y `SAFE` siguiendo TDD (RED → GREEN → REFACTOR) | Lógica de selección / Pruebas | 3 pts |
| 3 | Implementar `RouteSelectionStrategyFactory` y `RouteRecommendationService` para resolver la estrategia según la preferencia y filtrar por origen/destino | Lógica de selección | 1.5 pts |
| 4 | Escribir pruebas automatizadas de los 7 casos obligatorios (FASTEST, ACCESSIBLE, SAFE, rutas bloqueadas) | Pruebas | 1 pt |
| 5 | Redactar `docs/requirements.md`, el diagrama C4 (`docs/context.png`) y documentar el patrón de diseño en el `README.md` | Documentación | 0.5 pts |

## Definition of Done

- `mvn clean test` ejecuta sin errores y sin pruebas deshabilitadas.
- Los 7 casos de prueba obligatorios del enunciado están cubiertos y en verde.
- El historial de commits evidencia el ciclo RED → GREEN → REFACTOR.
- El diseño permite agregar una nueva preferencia (`COVERED`, `LOW_CONGESTION`, `ENERGY_SAVING`, `SCENIC`) sin modificar las estrategias existentes.
- La rama `feature/HU-route-recommendation` fue integrada a `develop` y el PR `develop → main` está abierto.
