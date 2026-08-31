# Solución — CampusRoute

Este documento explica, en un solo lugar, **cómo está organizado el repositorio**, **qué contiene cada carpeta/archivo**, **qué se hizo paso a paso** para resolver el parcial y **cómo continuar** a partir de lo entregado (ejecutar el proyecto, correr las pruebas, llevar la planeación a Jira, etc.).

No reemplaza al `README.md` (que contiene el enunciado del parcial y el resumen final de la solución): este archivo es una guía más detallada, pensada para orientarse rápido dentro del proyecto.

---

## 1. Estructura de directorios

```text
TinTan/
├── README.md
├── pom.xml
├── .gitignore
├── docs/
│   ├── context.png
│   ├── requirements.md
│   ├── agile-planning.md
│   └── solucion.md
└── src/
    ├── main/java/com/campusroute/
    │   ├── domain/
    │   │   ├── Segment.java
    │   │   ├── Route.java
    │   │   └── TravelPreference.java
    │   ├── strategy/
    │   │   ├── RouteSelectionStrategy.java
    │   │   ├── FastestRouteStrategy.java
    │   │   ├── AccessibleRouteStrategy.java
    │   │   ├── SafeRouteStrategy.java
    │   │   └── RouteSelectionStrategyFactory.java
    │   ├── service/
    │   │   └── RouteRecommendationService.java
    │   └── exception/
    │       └── NoRouteAvailableException.java
    └── test/java/com/campusroute/
        ├── domain/
        │   └── RouteTest.java
        ├── strategy/
        │   ├── FastestRouteStrategyTest.java
        │   ├── AccessibleRouteStrategyTest.java
        │   ├── SafeRouteStrategyTest.java
        │   └── RouteSelectionStrategyFactoryTest.java
        └── service/
            └── RouteRecommendationServiceTest.java
```

> `target/` también existe localmente (lo genera Maven al compilar) pero está en `.gitignore` y no se versiona.

---

## 2. Qué hay en cada carpeta y por qué importa

### `README.md`
Es el enunciado original del parcial **más** la sección final `# Solución` (integrante, patrón de diseño usado, evidencia de TDD, enlace a Jira y enlace al Pull Request). Es el punto de entrada que el profesor revisará primero.

### `pom.xml`
Define el proyecto Maven: `groupId`/`artifactId`, Java 17, la dependencia de **JUnit 5** (`junit-jupiter`) en scope `test`, y los plugins de compilación (`maven-compiler-plugin`) y de ejecución de pruebas (`maven-surefire-plugin`). Es lo que permite correr `mvn compile`, `mvn test` y `mvn package` sin ninguna configuración adicional.

### `.gitignore`
Evita versionar la carpeta `target/` (artefactos compilados) y archivos propios de IDEs. Mantiene el repositorio limpio, mostrando solo código fuente y documentación.

### `docs/context.png`
El **Diagrama de Contexto C4** (nivel 1, System Context) pedido en la sección 3.1 del enunciado. Muestra:
- **CampusRoute** como el sistema en alcance.
- Los **actores**: estudiantes/profesores/visitantes/personal administrativo (quienes piden rutas) y el coordinador de infraestructura y seguridad (quien actualiza el estado de los caminos).
- Dos **sistemas externos**: el sistema de mantenimiento del campus y el sistema de eventos institucionales.
- Las **relaciones nombradas** entre todos ellos.

Es importante porque exige pensar el problema desde "afuera hacia adentro" antes de programar: quién usa el sistema, qué espera obtener, y con qué otros sistemas podría conversar en el futuro.

### `docs/requirements.md`
Contiene las **4 historias de usuario** exigidas (sección 4 del enunciado), cada una con formato `Como / Quiero / Para` y al menos 2 criterios de aceptación verificables, más **1 requerimiento no funcional** (extensibilidad de preferencias). La primera historia (HU-01) es la obligatoria de recomendación de rutas; las otras tres (accesibilidad, seguridad, bloqueo por mantenimiento) surgen directamente de las situaciones descritas en el contexto del parcial.

Es importante porque conecta el contexto del negocio (sección 2 del enunciado) con lo que realmente se construyó en código: cada criterio de aceptación de HU-01 se corresponde 1 a 1 con una prueba automatizada.

### `docs/agile-planning.md`
Es el contenido de planeación Agile que normalmente iría en Jira (sección 7 del enunciado): la historia obligatoria con su prioridad, story points, criterios de aceptación y **tareas técnicas** (modelo de dominio, lógica de selección, pruebas, documentación). Se escribió aquí porque esta sesión de desarrollo no tuvo acceso a un conector de Jira; en la sección 5 de este documento se explica paso a paso cómo migrarlo a un proyecto real de Jira.

### `docs/solucion.md`
Este mismo archivo: la guía operativa del repositorio.

### `src/main/java/com/campusroute/domain/`
El **modelo de dominio**, sin ninguna dependencia de frameworks:

- **`Segment.java`** — un `record` de Java (inmutable) con los 7 atributos mínimos que pide el enunciado: `origin`, `destination`, `distanceMeters`, `estimatedTimeMinutes`, `securityLevel`, `hasStairs`, `isAvailable`. Es el tramo elemental de una ruta.
- **`Route.java`** — una ruta compuesta por una o más `Segment`. Expone los cálculos que todas las estrategias necesitan: `totalDistanceMeters()`, `totalTimeMinutes()`, `averageSecurityLevel()`, `hasStairs()` e `isBlocked()` (verdadero si algún segmento no está disponible).
- **`TravelPreference.java`** — el `enum` con las tres preferencias soportadas en esta versión: `FASTEST`, `ACCESSIBLE`, `SAFE`.

Es el corazón del sistema: si el dominio está mal modelado, ninguna estrategia puede calcularse bien. Por eso fue lo primero que se construyó, con TDD, antes de tocar ninguna regla de negocio.

### `src/main/java/com/campusroute/strategy/`
Aquí vive la **funcionalidad obligatoria** (sección 5 del enunciado) y el **patrón de diseño** (sección 10):

- **`RouteSelectionStrategy.java`** — la interfaz del patrón *Strategy*: define un único método `selectBest(List<Route>)`.
- **`FastestRouteStrategy.java`** — implementa `FASTEST`: descarta rutas bloqueadas y elige la de menor `totalTimeMinutes()`.
- **`AccessibleRouteStrategy.java`** — implementa `ACCESSIBLE`: descarta rutas bloqueadas y con escaleras, y elige la de menor `totalDistanceMeters()` entre las que quedan.
- **`SafeRouteStrategy.java`** — implementa `SAFE`: descarta rutas bloqueadas, elige la de mayor `averageSecurityLevel()`, y si hay empate desempata por menor distancia.
- **`RouteSelectionStrategyFactory.java`** — resuelve qué `RouteSelectionStrategy` concreta usar según el `TravelPreference` recibido. Es la única clase que "conoce" a todas las estrategias.

Cada estrategia es independiente y no sabe nada de las otras. Esto es justamente lo que exige la sección 6 del enunciado ("regla de evolución del sistema"): una preferencia futura (`COVERED`, `SCENIC`, etc.) se agrega creando una clase nueva y registrándola en la factory, sin tocar el código de `FastestRouteStrategy`, `AccessibleRouteStrategy` ni `SafeRouteStrategy`.

### `src/main/java/com/campusroute/service/RouteRecommendationService.java`
El punto de entrada de la funcionalidad: recibe `origin`, `destination`, `preference` y la colección de rutas disponibles, filtra las rutas que efectivamente van de ese origen a ese destino, y delega la selección final en la estrategia que le entrega la factory. Es el "cliente" del patrón Strategy.

### `src/main/java/com/campusroute/exception/NoRouteAvailableException.java`
Excepción de negocio que se lanza cuando ninguna ruta cumple la preferencia solicitada (por ejemplo, todas están bloqueadas, o ninguna conecta el origen con el destino pedido). Evita que el sistema retorne un resultado inválido o silencioso.

### `src/test/java/com/campusroute/...`
Las pruebas automatizadas, organizadas en el mismo paquete que la clase que prueban (convención estándar de Maven/JUnit):

| Archivo | Qué verifica |
|---|---|
| `domain/RouteTest.java` | Cálculos de distancia, tiempo, seguridad promedio, bloqueo y escaleras de una `Route`. |
| `strategy/FastestRouteStrategyTest.java` | **Caso 1** (menor tiempo total) y **Caso 2** (ruta bloqueada descartada) de `FASTEST`. |
| `strategy/AccessibleRouteStrategyTest.java` | **Caso 3** (descarta escaleras) y **Caso 4** (menor distancia entre válidas) de `ACCESSIBLE`. |
| `strategy/SafeRouteStrategyTest.java` | **Caso 5** (mayor seguridad promedio) y **Caso 6** (desempate por distancia) de `SAFE`. |
| `strategy/RouteSelectionStrategyFactoryTest.java` | Que la factory resuelva la estrategia correcta para cada preferencia. |
| `service/RouteRecommendationServiceTest.java` | Filtrado por origen/destino y **Caso 7** (una ruta bloqueada nunca es recomendada, incluso si hubiera ganado en otro criterio). |

En total son **22 pruebas**, todas en verde, y cubren los 7 casos obligatorios pedidos en la sección 9 del enunciado.

---

## 3. Paso a paso de lo que se hizo

1. **Lectura del enunciado y aclaración de bloqueos.** Se identificaron dos limitaciones de la sesión de trabajo: no había conector de Jira disponible, y la sesión estaba preconfigurada para trabajar sobre una rama distinta a las que pide el Git Flow del parcial. Se decidió (con el usuario) documentar la planeación Agile en `docs/agile-planning.md` en vez de crear un Jira real, y seguir el Git Flow tal como lo pide el enunciado.
2. **Git Flow inicial:** se crearon `develop` (desde `main`) y `feature/HU-route-recommendation` (desde `develop`).
3. **Proyecto Maven base:** `pom.xml` con Java 17 + JUnit 5, estructura de paquetes (`domain`, `strategy`, `service`, `exception`), y verificación de que `mvn compile` funcionara antes de escribir una sola línea de lógica.
4. **Modelo de dominio con TDD:** se escribió primero `RouteTest.java` (falla en RED porque `Route`/`Segment` no existían), luego se implementaron `Segment` y `Route` (GREEN), y después se refactorizó `Segment` a `record` de Java manteniendo los tests en verde (REFACTOR).
5. **`FASTEST` con TDD:** se agregó `TravelPreference`, la excepción `NoRouteAvailableException` y la interfaz `RouteSelectionStrategy`; luego se escribió `FastestRouteStrategyTest` (RED) y se implementó `FastestRouteStrategy` (GREEN) cubriendo los casos 1 y 2.
6. **`ACCESSIBLE` con TDD:** mismo ciclo — `AccessibleRouteStrategyTest` (RED) → `AccessibleRouteStrategy` (GREEN), casos 3 y 4.
7. **`SAFE` con TDD:** mismo ciclo — `SafeRouteStrategyTest` (RED, usando el ejemplo exacto del enunciado para el desempate) → `SafeRouteStrategy` (GREEN), casos 5 y 6.
8. **Factory + Service con TDD:** `RouteSelectionStrategyFactoryTest` y `RouteRecommendationServiceTest` (RED) → `RouteSelectionStrategyFactory` y `RouteRecommendationService` (GREEN), cubriendo el caso 7 y el filtrado por origen/destino.
9. **Documentación:** se escribieron `docs/requirements.md` (historias + criterios + NFR), `docs/context.png` (diagrama C4 generado a partir de un SVG propio) y `docs/agile-planning.md` (planeación Agile).
10. **README final:** se agregó la sección `## Patrón de diseño utilizado` (Strategy + Factory, con justificación de por qué permite extender sin modificar) y la sección `# Solución` completa (integrante, patrón, evidencia TDD, Jira, Pull Request).
11. **Integración:** se corrió `mvn clean test` en verde, se hizo `git merge --no-ff feature/HU-route-recommendation` dentro de `develop`, se volvió a correr `mvn clean test` sobre `develop`, y se hizo push de `develop` y `feature/HU-route-recommendation` a GitHub.
12. **Pull Request:** se abrió el PR `develop → main` (permanece **abierto**, sin merge) con la descripción de la funcionalidad, las pruebas, el patrón de diseño y las decisiones técnicas, y se actualizó el README con su URL.

Todo el detalle exacto (archivo por archivo, con los mensajes `test(RED)`, `feat(GREEN)`, `refactor:`) queda en el historial de commits de `feature/HU-route-recommendation`, visible con `git log --oneline feature/HU-route-recommendation`.

---

## 4. Cómo ejecutar el proyecto

Requisitos: **Java 17+** y **Maven 3.9+** instalados (`java -version`, `mvn -version`).

Desde la raíz del repositorio:

```bash
# Compilar el código fuente
mvn clean compile

# Ejecutar toda la suite de pruebas (22 tests)
mvn test

# Compilar y empaquetar en un .jar (incluye correr los tests)
mvn clean package
```

Si `mvn test` termina con `BUILD SUCCESS`, todas las pruebas —incluidos los 7 casos obligatorios del enunciado— están pasando.

### Usar `RouteRecommendationService` desde código

No hay interfaz gráfica ni API REST (no se requieren, según el enunciado). Para usar la funcionalidad, se instancia el servicio y se le pasan objetos de dominio directamente, por ejemplo:

```java
RouteRecommendationService service =
        new RouteRecommendationService(new RouteSelectionStrategyFactory());

Route ruta1 = new Route("Ruta 1", List.of(
        new Segment("Biblioteca", "Edificio A", 150, 3, 4, false, true),
        new Segment("Edificio A", "Laboratorio H-301", 100, 2, 4, false, true)
));

Route ruta2 = new Route("Ruta 2", List.of(
        new Segment("Biblioteca", "Laboratorio H-301", 300, 6, 5, false, true)
));

Route mejorRuta = service.recommendRoute(
        "Biblioteca", "Laboratorio H-301",
        TravelPreference.FASTEST,
        List.of(ruta1, ruta2)
);

System.out.println(mejorRuta.getName());
```

### Revisar el reporte de pruebas

Después de `mvn test`, Maven genera reportes en `target/surefire-reports/` (uno por clase de prueba, con el detalle de cada método y si pasó o falló). Esa carpeta no se versiona (está en `.gitignore`), se regenera cada vez que se corren los tests.

---

## 5. Cómo continuar a partir de lo entregado

### 5.1 Clonar y moverse por las ramas

```bash
git clone https://github.com/IgnacioCastillo05/TinTan.git
cd TinTan

git checkout develop                          # rama de integración
git checkout feature/HU-route-recommendation   # rama donde se desarrolló con TDD
git log --oneline feature/HU-route-recommendation   # ver el ciclo RED -> GREEN -> REFACTOR
```

### 5.2 Migrar `docs/agile-planning.md` a un proyecto real de Jira

`docs/agile-planning.md` ya trae todo el contenido listo para copiar. Pasos sugeridos:

1. **Crear el proyecto** en Jira (tipo *Scrum* o *Kanban*, como prefieras) — por ejemplo `CAMPUSROUTE`.
2. **Crear la historia** (`Story`):
   - Título: *Recomendar la mejor ruta según la preferencia del usuario*.
   - Descripción: pegar el bloque `Como / Quiero / Para` de la sección "Historia de usuario" del archivo.
   - Prioridad: `Alta` (según lo indicado en el archivo).
   - Story Points: `8` (campo "Story point estimate", si el proyecto es Scrum).
   - Agregar los 3 criterios de aceptación como una lista dentro de la descripción o en un campo de "Acceptance Criteria" si el proyecto lo tiene habilitado.
3. **Crear las tareas técnicas** como *Sub-tasks* de esa historia (o issues de tipo `Task` enlazados con "relates to"), una por cada fila de la tabla "Tareas técnicas" del archivo (modelo de dominio, estrategias + TDD, factory/service, pruebas de los 7 casos, documentación). Copiar la estimación de cada una al campo correspondiente.
4. **Agregar la Definition of Done** del archivo como checklist de la historia, o como descripción del criterio de "Done" del tablero.
5. **Enlazar el Pull Request** de GitHub a la historia de Jira (si tienes la integración Jira-GitHub activada, esto se hace automático al mencionar la clave del issue, p. ej. `CAMPUSROUTE-1`, en un commit o en el PR; si no, se pega el enlace manualmente en un comentario).
6. **Copiar la URL del proyecto/la historia** y pegarla en la sección `## Jira` del `README.md` (reemplazando el texto "pendiente").

### 5.3 Revisar y, si corresponde, mergear el Pull Request

El PR `develop → main` está intencionalmente **abierto** (no se hace merge) porque el enunciado pide que el profesor lo revise directamente:

- URL: https://github.com/IgnacioCastillo05/TinTan/pull/1

Si en algún momento quieres cerrarlo (por ejemplo, después de que lo revisen), desde GitHub: **Merge pull request** (o, localmente, `git checkout main && git merge --no-ff develop`), y luego correr de nuevo `mvn clean test` sobre `main` para confirmar que todo sigue en verde.

### 5.4 Agregar una nueva preferencia de desplazamiento (ejemplo: `SCENIC`)

Para comprobar que el diseño realmente permite extender sin romper lo existente:

1. Agregar el valor al enum: `TravelPreference.SCENIC`.
2. Crear `src/main/java/com/campusroute/strategy/ScenicRouteStrategy.java` implementando `RouteSelectionStrategy`, con la lógica de selección deseada.
3. Registrar la nueva estrategia en `RouteSelectionStrategyFactory` (una línea en `defaultStrategies()`).
4. Escribir `ScenicRouteStrategyTest.java` siguiendo el mismo patrón RED → GREEN que las estrategias existentes.
5. Correr `mvn test`: las pruebas de `FastestRouteStrategy`, `AccessibleRouteStrategy` y `SafeRouteStrategy` deben seguir pasando sin haber sido tocadas — esa es la evidencia de que el patrón Strategy cumple su objetivo.

### 5.5 Seguir desarrollando bajo el mismo flujo de trabajo

Para cualquier funcionalidad nueva:

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nombre-de-la-funcionalidad

# ... TDD: RED -> GREEN -> REFACTOR, con commits descriptivos ...

mvn clean test          # confirmar que todo pasa
git checkout develop
git merge --no-ff feature/nombre-de-la-funcionalidad
git push origin develop
```

Y abrir un nuevo Pull Request cuando corresponda integrar `develop` a `main`.
