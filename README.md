# Into The Darkness

RPG por turnos para Paradigma Orientado a Objetos.

## Cómo ejecutar

**Juego (punto de entrada oficial):** `src/ui/VentanaPrincipal.java`
Abrir el archivo en VS Code y ejecutar con "Run". Las partidas guardadas quedan en la carpeta `partidas/`, adentro de la carpeta desde donde se ejecuta.

**Modo consola (solo para pruebas):** `src/consola/JuegoConsola.java`
Es el mismo juego sin la interfaz gráfica: se juega escribiendo el número de cada opción.

## Cómo se juega

1. Nueva partida: se eligen 3 personajes entre Mago, Guerrero, Curador, Arquero, Asesino, Caballero Sagrado y Cazador.
2. Se avanza por un mapa de nodos sin poder volver atrás: Batalla, Batalla Élite, Tesoro, Evento, Fogata y Herrero. Antes del jefe se elige entre Fogata y Herrero.
3. Antes de cada pelea se eligen arma, armadura y pociones para cada personaje (según su capacidad de inventario).
4. En cada turno: Atacar, Defender, usar una Habilidad, usar un Objeto o pasar el turno. El orden de los turnos va por velocidad.
5. Al ganar se reparten experiencia, oro y objetos; los personajes suben de nivel.
6. Al final espera un jefe al azar: Dragón, Golem o Rey Vampiro.
7. Se puede pausar y guardar en cualquier momento en una de las 3 ranuras.

## Paquetes

- `entidades`: Entidad, Personaje (7 clases), Enemigo (6 comunes y 3 jefes), efectos, pasivas y habilidades.
- `combate`: Batalla (turnos, daño, objetivos), PartyAliada y PartyEnemiga.
- `mapa`: generación y navegación del mapa de nodos.
- `partida`: Partida (el estado del juego), GuardadoDePartidas, CreadorDePersonajes y GeneradorDeEncuentros.
- `objetos`: Item, armas, armaduras, pociones y el Catálogo.
- `ui`: la interfaz gráfica.
- `consola`: el modo de prueba por consola.
