# Into The Darkness

RPG por turnos en Java (Paradigma Orientado a Objetos, UADE). Se elige una party de 3 personajes, se recorre un mapa de
nodos (batallas, batalla elite, fogatas y un jefe final) y se pelea por turnos. Hay inventario, experiencia y niveles,
efectos de estado y guardado de la partida en archivos.

## Cómo ejecutarlo
- JDK 21. Ejecutar la clase **`ui.VentanaPrincipal`** (es el único `main`) desde la **carpeta raíz del proyecto**:
  las imágenes se buscan con rutas relativas (`src/ui/imagenes/...`).
- Las partidas guardadas quedan en la carpeta `partidas/` (3 ranuras).

## Arquitectura (MVC)
| Capa | Paquete | Qué hace |
|---|---|---|
| Vista | `ui` | Pantallas Swing. Solo muestran DTOs y le piden cosas a los controladores. No importan nada del modelo. |
| Controlador | `controlador` | `ControladorJuego` (partida, mapa, preparación, guardado) y `ControladorCombate` (la pelea). Reciben lo que pide la vista, hablan con el modelo y devuelven DTOs. |
| DTO | `dto` | Datos listos para mostrar (`EntidadDTO`, `CombateDTO`, `MapaDTO`...). Solo dependen de `java.util`. |
| Modelo | `combate`, `partida`, `entidades`, `objetos`, `mapa` | La lógica del juego. No conoce a la vista ni a los controladores. |

Flujo de una acción: botón (`ui`) → `ControladorCombate.defender()` → `Batalla.defender()` → el controlador arma un `CombateDTO` → la pantalla se vuelve a dibujar.

## Cómo se aplican GRASP y SOLID
- **Controller**: `ControladorJuego` y `ControladorCombate` reciben los eventos de la interfaz y coordinan; no tienen reglas del juego.
- **Information Expert**: cada clase hace lo que sabe con sus datos (`Objetivo` elige sus objetivos, `Recompensas` reparte el botín, `OrdenDeTurnos` ordena los turnos).
- **Polimorfismo / Open-Closed**: sin `instanceof` ni `switch` por tipo. `Entidad.esAliado()`, `Item.guardarEn()`, `EventoDeNodo` (un evento por tipo de nodo), `Habilidad` y `Efecto` con sus subclases.
- **Alta cohesión / Responsabilidad única**: `Batalla` coordina la pelea; el orden (`OrdenDeTurnos` + `ComparadorDeTurnos`) y el botín (`Recompensas`) son clases aparte. El guardado se divide en `GuardadoDePartidas` (archivos) y `SerializadorDePartida` (pasar a texto).
- **Bajo acoplamiento / Protected Variations**: la persistencia no conoce las clases concretas: cada personaje, enemigo y objeto se identifica con `getClase()`, `getTipo()` o su id y se recrea con un `Map` de fábricas.
- **Pure Fabrication**: `Conversor` (arma los DTOs) y `SerializadorDePartida`.

## Tamaño
123 archivos .java, 8389 líneas: controlador (3 archivos), dto (12), ui (16), combate (7), partida (9), entidades (63), objetos (9), mapa (4).
