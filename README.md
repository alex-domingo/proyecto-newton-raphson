# Proyecto Newton-Raphson

Aplicación de escritorio (JavaFX) que encuentra raíces de funciones de una variable mediante el **método numérico de Newton-Raphson**. Permite ingresar una función `f(x)`, un valor inicial, una tolerancia y un número máximo de iteraciones, y muestra el proceso completo: una tabla iteración por iteración, la raíz aproximada, el estado de convergencia y una gráfica de la función con los puntos de cada iteración.

## Características

- Resolución por el método de Newton-Raphson con derivada calculada automáticamente (numérica, vía mXparser).
- Tabla de iteraciones con `xᵢ`, `f(xᵢ)`, `f'(xᵢ)`, `xᵢ₊₁` y el error `δ`.
- Doble criterio de paro: cambio entre iteraciones (`δ < tol`) y residuo (`|f(x)| < tol`).
- Estado de convergencia explícito (convergió, máximo de iteraciones, derivada cero, valor inválido, función inválida).
- Gráfica interactiva de `f(x)` con los puntos de iteración y la raíz marcada (zoom y exportación a PNG incluidos).
- Validación de entradas y manejo de casos borde (NaN/infinitos, división por cero, sintaxis incorrecta).
- Versión de consola incluida además de la interfaz gráfica.

## Requisitos

- **Java 21 o superior** (JDK para compilar; JRE/JDK para ejecutar el JAR).
- **Maven 3.8+** (solo si vas a compilar desde el código fuente).

> El JAR empaquetado incluye JavaFX y todas las librerías, pero **no** el propio Java: la máquina donde se ejecute necesita tener Java 21+ instalado.

## Tecnologías

- [JavaFX 21](https://openjfx.io/) — interfaz gráfica.
- [MathParser.org-mXparser 5.0.5](https://mathparser.org/) — evaluación de expresiones y derivada numérica.
- [JFreeChart 1.5.6](https://www.jfree.org/jfreechart/) + [JFreeChart-FX 2.0.2](https://github.com/jfree/jfreechart-fx) — gráfica embebida en JavaFX.

## Ejecutar

### Opción A — Usar el JAR (presentable)

Descarga el JAR `*-jar-with-dependencies.jar` desde la sección de [Releases](../../releases) y ejecútalo:

```bash
java -jar ProyectoNewtonRaphson-1.0-SNAPSHOT-jar-with-dependencies.jar
```

En Windows también puedes hacer doble clic sobre el archivo.

### Opción B — Desde el código fuente

Clona el repositorio y lánzalo con el plugin de JavaFX:

```bash
git clone https://github.com/<usuario>/proyecto-newton-raphson.git
cd proyecto-newton-raphson
mvn clean javafx:run
```

### Generar el JAR con dependencias

```bash
mvn clean package
```

El resultado queda en `target/ProyectoNewtonRaphson-1.0-SNAPSHOT-jar-with-dependencies.jar`.

> El JAR empaqueta las librerías nativas de JavaFX del sistema operativo donde se compila. Para distribuirlo a otro SO, hay que regenerarlo en ese sistema.

## Uso

Completa los cuatro campos y pulsa **Calcular** (o presiona Enter):

| Campo | Descripción | Ejemplo |
|-------|-------------|---------|
| `f(x)` | Función a resolver | `x^2-x-2` |
| `x₀` | Valor inicial | `1.5` |
| Iteraciones | Máximo de iteraciones | `10` |
| Tolerancia | Error admisible | `0.0001` |

### Sintaxis de la función

La función se interpreta con mXparser. Algunas reglas:

- La variable **debe** ser `x`.
- La multiplicación es **explícita**: `3*x`, no `3x`.
- Potencias con `^`: `x^2`, `x^3`.
- Funciones disponibles: `sin(x)`, `cos(x)`, `tan(x)`, `e^x`, `ln(x)`, `sqrt(x)`, etc.

### Funciones de prueba

| Función | x₀ | Resultado esperado |
|---------|-----|--------------------|
| `x^2-x-2` | `1.5` | Raíz en `2.0` |
| `x^2-x-2` | `-1.5` | Raíz en `-1.0` |
| `x^3-x-2` | `1.5` | Raíz ≈ `1.52138` |
| `cos(x)-x` | `0.5` | Raíz ≈ `0.73909` |
| `e^x-2` | `1` | Raíz ≈ `0.69315` |
| `x^2+1` | `2` | No converge (sin raíz real) |
| `cos(x)` | `0` | Derivada cero en x₀ |
| `y^2-4` | — | Función inválida (variable incorrecta) |

## Estructura del proyecto

```
src/main/java/com/matapli3/proyectonewtonraphson/
├── App.java                  # Punto de entrada JavaFX
├── Launcher.java             # Lanzador (evita el error de módulos de JavaFX)
├── ProyectoNewtonRaphson.java# Versión de consola
├── PrimaryController.java     # Controlador de la interfaz
├── Logica/
│   ├── NewtonRaphson.java     # Algoritmo del método
│   ├── Iteracion.java         # Datos de una iteración
│   ├── Resultado.java         # Resultado del cálculo
│   └── EstadoConvergencia.java# Estados de convergencia
└── Graficos/
    └── Graficador.java        # Construcción de la gráfica

src/main/resources/com/matapli3/proyectonewtonraphson/
├── primary.fxml               # Diseño de la interfaz
└── styles.css                 # Estilos de la tabla
```

## Estados de convergencia

| Estado | Significado |
|--------|-------------|
| Convergió (paso) | El cambio entre iteraciones quedó por debajo de la tolerancia |
| Convergió (residuo) | `\|f(x)\|` quedó por debajo de la tolerancia |
| Máximo de iteraciones | Se agotaron las iteraciones sin converger |
| Derivada cero | La derivada se hizo prácticamente cero (tangente horizontal) |
| Valor inválido | Apareció un NaN o infinito durante el cálculo |
| Función inválida | No se pudo interpretar `f(x)` o su derivada |

## Créditos y licencias

Proyecto académico desarrollado para un curso de métodos numéricos.

Este software utiliza:
- **mXparser** bajo su licencia dual; este proyecto se acoge al uso **no comercial**.
- **JFreeChart** y **JFreeChart-FX** bajo licencia LGPL.
