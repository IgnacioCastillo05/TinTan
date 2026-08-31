# Parcial DOSW — CampusRoute

## 1. Contexto

La Escuela Colombiana de Ingeniería quiere iniciar el desarrollo de **CampusRoute**, una aplicación que ayude a estudiantes, profesores, visitantes y personal administrativo a desplazarse dentro del campus.

Actualmente, cuando una persona necesita ir de un punto a otro debe decidir por su cuenta qué camino tomar. En la mayoría de los casos se elige la ruta más corta, pero esa decisión no siempre es la mejor.

Imagine las siguientes situaciones:

- Un estudiante sale de clase en el Edificio A y tiene menos de diez minutos para llegar al laboratorio.
- Una persona con movilidad reducida necesita desplazarse evitando escaleras.
- Un visitante llega al campus por primera vez y desconoce la ubicación de los edificios.
- Una persona sale de una actividad nocturna y prefiere caminar por zonas iluminadas y cercanas a puntos de vigilancia.
- Durante una obra de mantenimiento algunos caminos se encuentran temporalmente cerrados.
- Un evento institucional genera alta congestión en determinados sectores del campus.

CampusRoute busca que el usuario pueda indicar un **origen**, un **destino** y una **preferencia de desplazamiento**, y que el sistema pueda recomendar la alternativa más conveniente entre las rutas disponibles.

En una primera versión, la aplicación trabajará con información previamente registrada sobre caminos y segmentos del campus. No es necesario conectarse a mapas reales ni calcular rutas mediante algoritmos de grafos.

---

# 2. Información disponible

Una ruta está formada por uno o más segmentos.

Por ejemplo:

```text
Ruta 1
Biblioteca → Edificio A → Laboratorio H-301

Ruta 2
Biblioteca → Cafetería → Plazoleta → Laboratorio H-301

Ruta 3
Biblioteca → Edificio C → Laboratorio C1-205A
```

Cada segmento contiene como mínimo:

```text
origin
destination
distanceMeters
estimatedTimeMinutes
securityLevel
hasStairs
isAvailable
```

Donde:

- `distanceMeters`: distancia del segmento en metros.
- `estimatedTimeMinutes`: tiempo estimado para recorrerlo.
- `securityLevel`: valor entre 1 y 5.
- `hasStairs`: indica si el segmento tiene escaleras.
- `isAvailable`: indica si el segmento se encuentra actualmente habilitado.

Una ruta que contenga al menos un segmento no disponible se considera temporalmente bloqueada.

---

# 3. Análisis inicial

Antes de implementar, analice CampusRoute como producto de software.

## 3.1 Modelo de Contexto C4

Construya el **Diagrama de Contexto C4** del sistema.

El diagrama debe permitir entender:

- quién utiliza CampusRoute;
- qué espera obtener cada actor;
- cuáles sistemas externos podrían interactuar con la aplicación;
- cuáles son las relaciones principales.

Incluya como mínimo:

- CampusRoute;
- actores relevantes;
- al menos un sistema externo;
- relaciones claramente nombradas.

Guarde el resultado en:

```text
docs/context.png
```

---

# 4. Requerimientos

A partir del contexto presentado, identifique los requerimientos que considere necesarios para una primera versión del producto.

Debe documentar:

- **4 historias de usuario**;
- mínimo **2 criterios de aceptación por cada historia**;
- **1 requerimiento no funcional**.

Utilice el formato:

```text
Como [tipo de usuario]
Quiero [funcionalidad]
Para [beneficio]
```

Los criterios de aceptación deben ser verificables.

Guarde los requerimientos en:

```text
docs/requirements.md
```

## Importante

Dentro de las historias identificadas debe aparecer obligatoriamente una historia relacionada con la **recomendación de rutas según una preferencia de desplazamiento**.

La redacción puede variar, pero su intención debe corresponder con la funcionalidad obligatoria descrita en la siguiente sección.

---

# 5. Funcionalidad obligatoria

De todas las funcionalidades que CampusRoute podría tener, en este parcial únicamente deberá implementar la siguiente:

## HU — Recomendar la mejor ruta según la preferencia del usuario

**Como** usuario de CampusRoute  
**Quiero** solicitar una ruta indicando mi preferencia de desplazamiento  
**Para** recibir la alternativa que mejor se ajuste a mi necesidad.

El sistema recibe:

```text
- origen
- destino
- preferencia
- colección de rutas disponibles
```

y debe retornar la mejor ruta posible.

Para esta versión deben existir tres preferencias.

---

## FASTEST

El usuario quiere llegar lo más rápido posible.

La aplicación debe:

1. descartar las rutas que contengan segmentos no disponibles;
2. calcular el tiempo total de cada ruta;
3. seleccionar la ruta con el menor tiempo total.

Ejemplo:

```text
Ruta A = 12 minutos
Ruta B = 8 minutos
Ruta C = 10 minutos
```

Resultado esperado:

```text
Ruta B
```

---

## ACCESSIBLE

El usuario necesita una ruta que pueda ser recorrida sin utilizar escaleras.

La aplicación debe:

1. descartar las rutas que contengan segmentos no disponibles;
2. descartar cualquier ruta que tenga al menos un segmento con escaleras;
3. entre las rutas válidas, seleccionar la de menor distancia total.

Ejemplo:

```text
Ruta A = 300 m, contiene escaleras
Ruta B = 420 m, sin escaleras
Ruta C = 380 m, sin escaleras
```

Resultado esperado:

```text
Ruta C
```

---

## SAFE

El usuario desea priorizar las zonas consideradas más seguras del campus.

Cada segmento tiene un `securityLevel` entre 1 y 5.

La aplicación debe:

1. descartar las rutas que contengan segmentos no disponibles;
2. calcular el promedio de seguridad de cada ruta;
3. seleccionar la ruta con el promedio de seguridad más alto;
4. si existe empate, seleccionar la ruta de menor distancia total.

Ejemplo:

```text
Ruta A = seguridad promedio 4.0 / 350 m
Ruta B = seguridad promedio 4.5 / 500 m
Ruta C = seguridad promedio 4.5 / 420 m
```

Resultado esperado:

```text
Ruta C
```

---

# 6. Regla de evolución del sistema

CampusRoute se encuentra apenas en su primera versión.

El equipo de producto ya anticipó que en futuras iteraciones podrían aparecer nuevas preferencias, por ejemplo:

```text
COVERED
```

Priorizar caminos cubiertos cuando esté lloviendo.

```text
LOW_CONGESTION
```

Evitar zonas con alta concentración de personas.

```text
ENERGY_SAVING
```

Preferir rutas que requieran menor esfuerzo físico.

```text
SCENIC
```

Priorizar zonas verdes y espacios abiertos.

Su diseño debe permitir incorporar nuevas formas de seleccionar una ruta **sin tener que reescribir o alterar la lógica de las preferencias que ya funcionan**.

Piense cuidadosamente cómo organizar las responsabilidades de las clases.

---

# 7. Planeación Agile

La historia que se va a desarrollar debe ser planificada antes de finalizar la implementación.

En Jira registre como mínimo:

- la historia de usuario obligatoria;
- prioridad;
- Story Points;
- mínimo 3 tareas técnicas.

Las tareas deben representar el trabajo que va a realizar.

Por ejemplo, pueden estar relacionadas con:

- pruebas;
- modelo del dominio;
- lógica de selección;
- refactorización;
- documentación.

No es obligatorio utilizar exactamente esas tareas.

Incluya el enlace al proyecto Jira en el `README.md`.

---

# 8. Proyecto Maven

La solución debe desarrollarse como un proyecto backend en:

```text
Java + Maven
```

No se requiere:

- Spring Boot;
- interfaz gráfica;
- API REST;
- base de datos;
- algoritmos de grafos.

El foco se encuentra en el diseño, la lógica de negocio y el proceso de desarrollo.

Una estructura posible es:

```text
campus-route/
├── pom.xml
├── README.md
├── docs/
│   ├── context.png
│   └── requirements.md
└── src/
    ├── main/
    │   └── java/
    └── test/
        └── java/
```

Antes de entregar deben ejecutar correctamente:

```bash
mvn clean compile
mvn test
mvn clean package
```

---

# 9. TDD

La funcionalidad deberá desarrollarse aplicando **Test Driven Development**.

La historia del repositorio debe permitir observar el proceso:

```text
RED → GREEN → REFACTOR
```

No basta con escribir todas las pruebas al finalizar.

Los commits deben permitir identificar que al menos parte de la funcionalidad fue construida siguiendo este ciclo.

Como mínimo deben existir pruebas automatizadas para los siguientes casos:

### Caso 1

`FASTEST` selecciona la ruta cuyo tiempo total es menor.

### Caso 2

Una ruta bloqueada no puede ser seleccionada por `FASTEST`.

### Caso 3

`ACCESSIBLE` descarta una ruta que contenga escaleras.

### Caso 4

`ACCESSIBLE` selecciona la ruta válida de menor distancia.

### Caso 5

`SAFE` selecciona la ruta con mayor promedio de seguridad.

### Caso 6

`SAFE` utiliza la distancia para resolver un empate.

### Caso 7

Una ruta bloqueada no puede ser seleccionada.

Puede implementar pruebas adicionales si lo considera necesario.

---

# 10. Diseño de software

Analice la siguiente característica del problema:

```text
FASTEST
ACCESSIBLE
SAFE
```

son formas diferentes de resolver el mismo problema:

> seleccionar la mejor ruta dentro de una colección de rutas.

La solución debe aplicar principios de orientación a objetos y utilizar un **patrón de diseño apropiado**.

No se indica explícitamente cuál debe utilizar.

Identificarlo correctamente hace parte del parcial.

Al finalizar agregue al `README.md`:

```text
## Patrón de diseño utilizado

Nombre:

Justificación:

Clases que participan:
```

La justificación debe explicar por qué el diseño permite incorporar nuevas preferencias sin modificar las existentes.

---

# 11. Git Flow

El repositorio debe trabajar con:

```text
main
  └── develop
       └── feature/*
```

La funcionalidad obligatoria debe desarrollarse desde una rama creada a partir de `develop`.

Ejemplo:

```text
feature/route-recommendation
```

o

```text
feature/HU-route-recommendation
```

No debe desarrollar directamente sobre:

```text
main
```

ni sobre:

```text
develop
```

Los commits deberán reflejar el avance del trabajo y permitir comprender la evolución de la solución.

Evite commits genéricos como:

```text
cambios
avance
final
cosas
```

---

# 12. Integración

Cuando termine la funcionalidad:

1. integre la rama `feature/*` en `develop`;
2. asegúrese de que el proyecto compile;
3. ejecute las pruebas;
4. verifique:

```bash
mvn clean test
```

Después cree un Pull Request:

```text
develop → main
```

---

# 13. Pull Request final

El Pull Request deberá contener una descripción breve indicando:

- qué funcionalidad fue desarrollada;
- qué pruebas fueron implementadas;
- qué patrón de diseño fue utilizado;
- cualquier decisión técnica que considere importante;
- enlace al proyecto Jira.

## Importante

El Pull Request:

```text
develop → main
```

debe quedar **OPEN**.

No realice el merge.

El profesor revisará la solución directamente desde este PR.

---

# 14. README final

Antes de entregar, complete este mismo archivo agregando al final:

```text
# Solución

## Integrante

Nombre:

## Patrón de diseño utilizado

Nombre:

Justificación:

Clases participantes:

## Evidencia TDD

Explique brevemente dónde se evidencia:

RED → GREEN → REFACTOR

## Jira

URL:

## Pull Request

URL:
```

---

# 15. Entrega

Entregue únicamente:

1. URL del repositorio GitHub.
2. URL del Pull Request `develop → main`.
3. URL del proyecto Jira.

El repositorio será la evidencia principal del parcial.

Se revisará especialmente:

- calidad de los requerimientos;
- claridad del modelo C4;
- organización del proyecto Maven;
- pruebas automatizadas;
- evidencia de TDD;
- diseño orientado a objetos;
- identificación y aplicación del patrón;
- uso correcto de Git Flow;
- planeación en Jira;
- calidad del Pull Request.

---

# Condiciones generales

- El trabajo es individual.
- No se requiere interfaz gráfica.
- No se requiere base de datos.
- No se requiere Spring Boot.
- No se requiere API REST.
- No se requiere implementar búsqueda de caminos ni algoritmos de grafos.
- Las rutas pueden construirse directamente desde las pruebas o mediante objetos Java.
- La funcionalidad principal consiste en **evaluar rutas existentes y seleccionar la más adecuada**.
- El Pull Request `develop → main` debe permanecer abierto para revisión.
- El proyecto debe ejecutar correctamente:

```bash
mvn test
```

El objetivo no es producir una aplicación grande, sino demostrar que puede transformar un problema en requerimientos, planificarlo, diseñar una solución mantenible, desarrollarla mediante pruebas y gestionar correctamente su ciclo de integración.
