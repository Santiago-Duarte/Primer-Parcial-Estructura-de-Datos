# Sistema de Administración de Cine

Sistema de gestión para administración de funciones de cine, desarrollado en Java vanilla (sin frameworks externos). Implementa 10 historias de usuario con estructuras de datos propias, jerarquía de excepciones personalizada y separación estricta entre UI y lógica de negocio.

---

## Descripción general

El sistema permite:
- Registrar funciones de cine (código, película, hora de inicio y puestos)
- Visualizar el mapa de puestos de cada función
- Comprar entradas para un puesto específico
- Iniciar labores reproduciendo todas las funciones y mostrando su estado

---

## Requisitos

- Java 8 o superior
- No requiere dependencias externas

---

## Estructura del proyecto

```
src/cine/
├── Main.java                          # Punto de entrada
├── modelo/
│   ├── EstadoPuesto.java              # Enum: DISPONIBLE, VENDIDO
│   ├── Puesto.java                    # Clase de puesto individual
│   ├── MovieSession.java              # Función de cine con array de puestos
│   ├── NodoMovieSession.java          # Nodo de la lista enlazada (package-private)
│   └── ListaMovieSession.java         # Lista enlazada simple de funciones
├── servicio/
│   ├── GestorMovieSession.java        # CRUD: registrar, buscar, listar
│   ├── ServicioVenta.java             # Lógica de venta de entradas
│   └── ServicioReproduccion.java      # Lógica de reproducción de funciones
├── ui/
│   ├── ConsolaIO.java                 # Entrada/salida robusta por consola
│   ├── VistaPuestos.java              # Formato visual del grid de puestos
│   └── MenuPrincipal.java             # Menú interactivo y orquestación
└── excepciones/
    ├── CineException.java             # Excepción base (checked)
    ├── PuestoInvalidoException.java
    ├── PuestoOcupadoException.java
    ├── MovieSessionDuplicadaException.java
    ├── FuncionNoEncontradaException.java
    ├── ListaVaciaException.java
    └── EntradaInvalidaException.java
```

---

## Historias de usuario implementadas

| HU | Descripción | Clases principales |
|----|-------------|-------------------|
| HU-01 | Crear función con código, película, hora y puestos | `Puesto`, `MovieSession`, `EstadoPuesto` |
| HU-02 | Almacenar funciones en lista enlazada simple | `NodoMovieSession`, `ListaMovieSession` |
| HU-03 | Jerarquía de excepciones personalizadas | 7 clases en `cine.excepciones` |
| HU-04 | Entrada robusta con validación y retry | `ConsolaIO` |
| HU-05 | Gestión CRUD de funciones | `GestorMovieSession` |
| HU-06 | Registrar función desde el menú | `MenuPrincipal.registrarFuncion()` |
| HU-07 | Visualizar mapa de puestos | `VistaPuestos` |
| HU-08 | Comprar entrada con selección de puesto | `ServicioVenta`, `MenuPrincipal.comprarEntrada()` |
| HU-09 | Iniciar labores mostrando todas las funciones | `ServicioReproduccion`, `MenuPrincipal.iniciarLabores()` |
| HU-10 | Menú principal con cierre limpio del Scanner | `MenuPrincipal.run()`, `Main` |

---

## Ejecución

1. Compilar todos los archivos `.java` dentro de `src/`:
   ```bash
   javac -d out src/cine/**/*.java
   ```
2. Ejecutar:
   ```bash
   java -cp out cine.Main
   ```

### Menú interactivo

```
=== SISTEMA CINE ===
1. Registrar funcion
2. Comprar entrada
3. Iniciar labores
0. Salir
```

---

## Resolución conceptual — Punto 6

Las siguientes preguntas se responden en términos de funciones de cine, usando la lista enlazada simple implementada en `ListaMovieSession` (HU-02).

### 1. ¿Cómo se recorrería la estructura para buscar una función por su código?

El recorrido se realiza de forma secuencial desde el nodo `primero` hasta llegar a `null`. En cada paso se compara el código de la función almacenada en el nodo actual con el código buscado. Si se encuentra una coincidencia, se retorna la función; si se llega al final de la lista sin encontrarla, se retorna `null`.

En `ListaMovieSession.buscarPorCodigo()` (línea 42-53), el algoritmo es:

```
actual = primero
mientras actual no sea null:
    si actual.getMovieSession().getCodigo() == codigoBuscado:
        retornar actual.getMovieSession()
    actual = actual.getSiguiente()
retornar null
```

La complejidad temporal es O(n), ya que en el peor caso se recorren todos los nodos de la lista. Dado que no hay acceso directo por índice (como en un arreglo), no existe forma de acortar la búsqueda salvo que el código buscado esté en el primer nodo.

---

### 2. ¿Qué dificultades traería usar una lista simplemente enlazada si se necesitara recorrerla en sentido contrario o encontrar la función anterior a la actual?

La lista simplemente enlazada solo almacena, en cada nodo, una referencia al siguiente nodo (`siguiente`), pero no al anterior. Esto genera dos dificultades concretas:

**Recorrido inverso:** No es posible recorrer la lista de final a inicio con un simple apuntador. Para obtener el orden inverso habría que recurrir a estrategias auxiliares, como apilar los elementos en un stack durante el recorrido hacia adelante y luego desapilarlos, o bien implementar una lista doblemente enlazada (con referencia `anterior` en cada nodo).

**Encontrar la función anterior:** Si se está parado en un nodo dado y se necesita acceder al nodo anterior, no se puede hacer en O(1). Hay que recorrer la lista desde `primero` hasta encontrar el nodo cuyo `getSiguiente()` apunta al nodo actual, lo cual tiene un costo de O(n) adicional. Si se necesitara hacer esta operación frecuentemente (por ejemplo, para eliminar un nodo sin recorrer la lista completa), la lista simplemente enlazada se vuelve ineficiente comparada con una lista doblemente enlazada.

---

### 3. ¿Qué debe tenerse en cuenta al eliminar una función de la estructura para no romper el recorrido ni las referencias a sus puestos?

Al eliminar un nodo de una lista simplemente enlazada hay que considerar tres casos según la posición del nodo:

**Si se elimina el primer nodo (`primero`):** El apuntador `primero` debe actualizarse para apuntar al segundo nodo (`primero = primero.getSiguiente()`). Si no se hace esto, la lista queda apuntando a un nodo que ya no pertenece a ella.

**Si se elimina un nodo intermedio:** El nodo anterior al eliminado debe apuntar, con su referencia `siguiente`, al nodo que viene después del eliminado (`anterior.setSiguiente(eliminado.getSiguiente())`). Si no se realiza este enlace, el nodo eliminado queda aislado y los nodos posteriores se pierden del recorrido.

**Si se elimina el último nodo:** El nodo anterior debe apuntar a `null` con su referencia `siguiente`, marcando el nuevo fin de la lista.

**Respecto a las referencias externas:** Cuando se elimina una función, las instancias de `ServicioVenta` o `ServicioReproduccion` que ya tengan una referencia al objeto `MovieSession` no se ven afectadas, porque `buscarPorCodigo()` retorna el objeto directamente, no una posición. Sin embargo, si algún servicio consulta la lista después de la eliminación, la función ya no aparecerá en el recorrido `forEach()`.

**El atributo `tamaño`** debe decrementarse en 1 para mantener la consistencia del contador.

**La función eliminada** se libera de la lista, pero el objeto `MovieSession` en sí se libera automáticamente por el recolector de basura de Java cuando no existen más referencias activas hacia él. No es necesario destruirlo explícitamente.

**Importante:** La lista actual no implementa un método `eliminar()`. Solo permite agregar y buscar. Agregar esta funcionalidad implicaría manejar los tres casos descritos y decrementar el tamaño.
