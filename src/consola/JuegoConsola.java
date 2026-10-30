package consola;
import combate.Batalla;
import combate.EstadoCombate;
import combate.PartyAliada;
import entidades.Entidad;
import entidades.enemigos.Enemigo;
import entidades.habilidades.Habilidad;
import entidades.personajes.Personaje;
import mapa.Nodo;
import objetos.Item;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import partida.CreadorDePersonajes;
import partida.GuardadoDePartidas;
import partida.Partida;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// El juego completo por consola, para probarlo sin la UI.
// Se juega escribiendo el número de la opción y Enter.
public class JuegoConsola {

    private static final int TAMANIO_PARTY = 3;

    private static Scanner teclado = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("===== INTO THE DARKNESS =====");
        while (true) {
            System.out.println("\n1) Nueva partida");
            System.out.println("2) Partidas guardadas");
            System.out.println("0) Salir");
            int opcion = leerOpcion(0, 2);
            if (opcion == 0) return;
            Partida partida = opcion == 1 ? nuevaPartida() : cargarPartida();
            if (partida != null) jugar(partida);
        }
    }

    // ---- Menú ----

    private static Partida nuevaPartida() {
        List<String> clases = CreadorDePersonajes.CLASES;
        List<String> elegidas = new ArrayList<>();
        while (elegidas.size() < TAMANIO_PARTY) {
            System.out.println("\nElegí el personaje " + (elegidas.size() + 1) + " de " + TAMANIO_PARTY + ":");
            for (int i = 0; i < clases.size(); i++) {
                System.out.println("  " + (i + 1) + ") " + clases.get(i));
            }
            elegidas.add(clases.get(leerOpcion(1, clases.size()) - 1));
        }
        Partida partida = new Partida(CreadorDePersonajes.crearParty(elegidas));
        System.out.println("\nLa party entra en la oscuridad: " + partida.getParty().getMiembrosJugador());
        return partida;
    }

    private static Partida cargarPartida() {
        mostrarRanuras();
        System.out.println("  0) Volver");
        int ranura = leerOpcion(0, GuardadoDePartidas.RANURAS);
        if (ranura == 0) return null;
        if (!GuardadoDePartidas.hayPartida(ranura)) {
            System.out.println("Esa ranura está vacía.");
            return null;
        }
        try {
            Partida partida = GuardadoDePartidas.cargar(ranura);
            System.out.println("Partida cargada.");
            return partida;
        } catch (IOException | RuntimeException e) {
            System.out.println("No se pudo cargar la partida: " + e.getMessage());
            return null;
        }
    }

    private static void mostrarRanuras() {
        for (int i = 1; i <= GuardadoDePartidas.RANURAS; i++) {
            System.out.println("  " + i + ") " + GuardadoDePartidas.describir(i));
        }
    }

    private static void guardar(Partida partida) {
        System.out.println("\n¿En qué ranura guardás?");
        mostrarRanuras();
        System.out.println("  0) Cancelar");
        int ranura = leerOpcion(0, GuardadoDePartidas.RANURAS);
        if (ranura == 0) return;
        if (GuardadoDePartidas.hayPartida(ranura)) {
            System.out.println("Esa ranura ya tiene una partida. ¿Sobrescribir? 1) Sí  0) No");
            if (leerOpcion(0, 1) == 0) return;
        }
        try {
            GuardadoDePartidas.guardar(partida, ranura);
            System.out.println("Partida guardada en la ranura " + ranura + ".");
            if (partida.estaEnBatalla()) System.out.println("(Al cargarla vas a volver al principio de esta pelea.)");
        } catch (IOException e) {
            System.out.println("No se pudo guardar: " + e.getMessage());
        }
    }

    // ---- La partida ----

    private static void jugar(Partida partida) {
        while (true) {
            mostrar(partida.tomarMensajes());
            boolean seguir;
            switch (partida.getEstado()) {
                case EN_MAPA: seguir = enElMapa(partida); break;
                case PREPARANDO_BATALLA: seguir = prepararYPelear(partida); break;
                case EN_FOGATA: seguir = enLaFogata(partida); break;
                case EN_HERRERO: seguir = enElHerrero(partida); break;
                case GANADA:
                    System.out.println("\n***** ¡VICTORIA! Derrotaste al jefe. *****");
                    return;
                case PERDIDA:
                    System.out.println("\n***** GAME OVER *****");
                    return;
                default: seguir = false; break;
            }
            if (!seguir) return;
        }
    }

    private static boolean enElMapa(Partida partida) {
        System.out.println("\n===== MAPA · piso " + partida.getNodoActual().getFila() + " =====");
        mostrarParty(partida.getParty());
        List<Nodo> caminos = partida.getCaminosPosibles();
        System.out.println("¿A dónde vas?");
        for (int i = 0; i < caminos.size(); i++) {
            Nodo nodo = caminos.get(i);
            System.out.println("  " + (i + 1) + ") " + nodo.getTpe() + " (piso " + nodo.getFila() + ")");
        }
        int guardar = caminos.size() + 1;
        System.out.println("  " + guardar + ") Guardar partida");
        System.out.println("  0) Salir al menú");
        int opcion = leerOpcion(0, guardar);
        if (opcion == 0) return false;
        if (opcion == guardar) {
            guardar(partida);
            return true;
        }
        partida.avanzarA(caminos.get(opcion - 1));
        return true;
    }

    private static boolean enLaFogata(Partida partida) {
        System.out.println("\n===== FOGATA =====");
        mostrarParty(partida.getParty());
        System.out.println("1) Descansar (recupera toda la vida y el maná, y revive a los caídos)");
        System.out.println("2) Guardar partida");
        System.out.println("0) Salir al menú");
        int opcion = leerOpcion(0, 2);
        if (opcion == 0) return false;
        if (opcion == 2) guardar(partida);
        else partida.descansar();
        return true;
    }

    private static boolean enElHerrero(Partida partida) {
        System.out.println("\n===== HERRERO · se puede mejorar una sola cosa =====");
        List<Personaje> personajes = partida.getParty().getMiembrosJugador();
        for (int i = 0; i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            System.out.println("  " + (i + 1) + ") " + p.getNombre() + " · arma: " + describirArma(p.getArma())
                    + " · armadura: " + describirArmadura(p.getArmadura()));
        }
        int guardar = personajes.size() + 1;
        int irse = personajes.size() + 2;
        System.out.println("  " + guardar + ") Guardar partida");
        System.out.println("  " + irse + ") Irse sin mejorar nada");
        System.out.println("  0) Salir al menú");
        int opcion = leerOpcion(0, irse);
        if (opcion == 0) return false;
        if (opcion == guardar) {
            guardar(partida);
        } else if (opcion == irse) {
            partida.salirDelHerrero();
        } else {
            Personaje elegido = personajes.get(opcion - 1);
            System.out.println("1) Mejorar el arma (+2)  2) Mejorar la armadura (+1 a cada defensa)  0) Volver");
            int mejora = leerOpcion(0, 2);
            if (mejora == 1) partida.mejorarArma(elegido);
            else if (mejora == 2) partida.mejorarArmadura(elegido);
        }
        return true;
    }

    // ---- Antes de pelear: arma, armadura y pociones ----

    private static boolean prepararYPelear(Partida partida) {
        System.out.println("\n===== ¡COMBATE! =====");
        for (Enemigo enemigo : partida.getEnemigosPendientes().getMiembrosEnemigos()) {
            System.out.println("  " + enemigo.getNombre() + " · " + enemigo.getPuntosDeSalud() + " HP");
        }
        System.out.println("1) Preparar a la party y pelear");
        System.out.println("2) Guardar partida");
        System.out.println("0) Salir al menú");
        int opcion = leerOpcion(0, 2);
        if (opcion == 0) return false;
        if (opcion == 2) {
            guardar(partida);
            return true;
        }
        PartyAliada party = partida.getParty();
        for (Personaje p : party.getMiembrosJugador()) {
            if (!p.estaVivo()) {
                System.out.println("\n" + p.getNombre() + " está caído y no pelea (revive en una fogata).");
                continue;
            }
            elegirArma(party, p);
            elegirArmadura(party, p);
            elegirPociones(party, p);
        }
        return pelear(partida, partida.comenzarBatalla());
    }

    private static void elegirArma(PartyAliada party, Personaje p) {
        List<Armas> armas = party.getInventario().getArmas();
        if (armas.isEmpty()) return;
        System.out.println("\nArma para " + p.getNombre() + " (ahora: " + describirArma(p.getArma()) + "):");
        for (int i = 0; i < armas.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + describirArma(armas.get(i)));
        }
        System.out.println("  0) Dejar como está");
        int opcion = leerOpcion(0, armas.size());
        if (opcion > 0) party.equiparArma(p, armas.get(opcion - 1));
    }

    private static void elegirArmadura(PartyAliada party, Personaje p) {
        List<Armaduras> armaduras = party.getInventario().getArmaduras();
        if (armaduras.isEmpty()) return;
        System.out.println("\nArmadura para " + p.getNombre() + " (ahora: " + describirArmadura(p.getArmadura()) + "):");
        for (int i = 0; i < armaduras.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + describirArmadura(armaduras.get(i)));
        }
        System.out.println("  0) Dejar como está");
        int opcion = leerOpcion(0, armaduras.size());
        if (opcion > 0) party.equiparArmadura(p, armaduras.get(opcion - 1));
    }

    private static void elegirPociones(PartyAliada party, Personaje p) {
        while (true) {
            if (p.mochilaLlena()) {
                System.out.println(p.getNombre() + " tiene la mochila llena (" + p.getEspaciosUsados() + "/" + p.getCapacidadInventario() + "). Listo.");
                return;
            }
            List<Consumibles> disponibles = party.getInventario().getConsumibles();
            System.out.println("\nObjetos para " + p.getNombre() + " · mochila " + p.getEspaciosUsados() + "/" + p.getCapacidadInventario()
                    + " " + nombres(p.getMochila()) + ":");
            for (int i = 0; i < disponibles.size(); i++) {
                Consumibles c = disponibles.get(i);
                System.out.println("  " + (i + 1) + ") " + c.getNombre() + " [" + c.getEspacios() + " espacio/s]");
            }
            int devolver = disponibles.size() + 1;
            if (!p.getMochila().isEmpty()) System.out.println("  " + devolver + ") Devolver todo al inventario");
            System.out.println("  0) Listo");
            int opcion = leerOpcion(0, p.getMochila().isEmpty() ? disponibles.size() : devolver);
            if (opcion == 0) return;
            if (opcion == devolver) {
                party.vaciarMochila(p);
            } else if (!party.darConsumible(p, disponibles.get(opcion - 1))) {
                System.out.println("No le entra en la mochila.");
            }
        }
    }

    // ---- La pelea ----

    // devuelve false si el jugador sale al menú
    private static boolean pelear(Partida partida, Batalla batalla) {
        while (batalla.getEstadoCombate() == EstadoCombate.EN_CURSO) {
            mostrar(batalla.tomarMensajes());
            if (!batalla.esTurnoDelJugador()) {
                batalla.turnoEnemigo();
                continue;
            }
            mostrarBatalla(batalla);
            Personaje actual = (Personaje) batalla.getTurnoActual();
            System.out.println("Turno de " + actual.getNombre() + ":");
            System.out.println("  1) Atacar (" + actual.getBasico() + ")  2) Defender  3) Habilidad  4) Objeto  5) Pasar turno  0) Pausa");
            int accion = leerOpcion(0, 5);
            if (accion == 0) {
                if (!pausa(partida, batalla)) return false;
            } else if (accion == 1) {
                usar(batalla, actual, actual.getBasico());
            } else if (accion == 2) {
                batalla.defender();
            } else if (accion == 3) {
                elegirHabilidad(batalla, actual);
            } else if (accion == 4) {
                elegirObjeto(batalla, actual);
            } else {
                batalla.pasarTurno();
            }
        }
        mostrar(batalla.tomarMensajes());
        partida.terminarBatalla();
        return true;
    }

    // devuelve false si el jugador sale al menú
    private static boolean pausa(Partida partida, Batalla batalla) {
        batalla.pausar();
        while (true) {
            System.out.println("\n===== PAUSA =====");
            System.out.println("1) Reanudar  2) Guardar  3) Guardar y salir al menú  4) Salir sin guardar");
            int opcion = leerOpcion(1, 4);
            if (opcion == 1) {
                batalla.reanudar();
                return true;
            }
            if (opcion == 2) guardar(partida);
            if (opcion == 3) {
                guardar(partida);
                return false;
            }
            if (opcion == 4) return false;
        }
    }

    private static void elegirHabilidad(Batalla batalla, Personaje actual) {
        List<Habilidad> habilidades = actual.getHabilidades();
        if (habilidades.isEmpty()) {
            System.out.println(actual.getNombre() + " no tiene habilidades.");
            return;
        }
        for (int i = 0; i < habilidades.size(); i++) {
            Habilidad h = habilidades.get(i);
            System.out.println("  " + (i + 1) + ") " + h + (h.estaDisponible() ? "" : " [en enfriamiento]"));
        }
        System.out.println("  0) Volver");
        int opcion = leerOpcion(0, habilidades.size());
        if (opcion == 0) return;
        Habilidad elegida = habilidades.get(opcion - 1);
        if (!elegida.estaDisponible()) {
            System.out.println("Todavía no se puede volver a usar.");
            return;
        }
        usar(batalla, actual, elegida);
    }

    private static void usar(Batalla batalla, Personaje actual, Habilidad habilidad) {
        Entidad objetivo = null;
        if (!habilidad.getObjetivo().esSobreAliados()) {
            // si está Encantado no puede atacar al vampiro que lo encantó: solo se ofrecen los demás
            List<Entidad> enemigos = batalla.enemigosDe(actual);
            enemigos.removeIf(enemigo -> !actual.puedeAtacarA(enemigo));
            if (enemigos.isEmpty()) {
                System.out.println(actual.getNombre() + " está encantado y no se anima a atacar a nadie."
                        + " Usá un objeto, una habilidad que no ataque o pasá el turno.");
                return;
            }
            if (habilidad.getObjetivo().hayQueElegirEnemigo()) objetivo = elegirDe("¿A quién atacás?", enemigos);
        } else if (habilidad.getObjetivo().hayQueElegirAliado()) {
            objetivo = elegirDe("¿Sobre quién?", batalla.aliadosDe(actual));
        }
        batalla.usarHabilidad(habilidad, objetivo);
    }

    private static void elegirObjeto(Batalla batalla, Personaje actual) {
        List<Consumibles> mochila = actual.getMochila();
        if (mochila.isEmpty()) {
            System.out.println(actual.getNombre() + " no lleva objetos.");
            return;
        }
        for (int i = 0; i < mochila.size(); i++) {
            System.out.println("  " + (i + 1) + ") " + mochila.get(i).getNombre());
        }
        System.out.println("  0) Volver");
        int opcion = leerOpcion(0, mochila.size());
        if (opcion == 0) return;
        Entidad objetivo = elegirDe("¿Sobre quién?", batalla.aliadosDe(actual));
        batalla.usarConsumible(mochila.get(opcion - 1), (Personaje) objetivo);
    }

    private static Entidad elegirDe(String pregunta, List<Entidad> opciones) {
        System.out.println(pregunta);
        for (int i = 0; i < opciones.size(); i++) {
            Entidad e = opciones.get(i);
            System.out.println("  " + (i + 1) + ") " + e.getNombre() + " (" + e.getPuntosDeSalud() + "/" + e.getPuntosDeSaludMaximos() + " HP)");
        }
        return opciones.get(leerOpcion(1, opciones.size()) - 1);
    }

    // ---- Mostrar cosas ----

    private static void mostrarBatalla(Batalla batalla) {
        System.out.println("\n--- Ronda " + batalla.getRonda() + " ---");
        for (Personaje p : batalla.getPartyAliada().getMiembrosJugador()) {
            System.out.println("  " + describir(p) + " · " + p.getRecurso() + "/" + p.getRecursoMaximo() + " maná");
        }
        System.out.println("  vs");
        for (Enemigo e : batalla.getPartyEnemiga().getMiembrosEnemigos()) {
            if (e.estaVivo()) System.out.println("  " + describir(e));
        }
    }

    private static void mostrarParty(PartyAliada party) {
        for (Personaje p : party.getMiembrosJugador()) {
            System.out.println("  " + p.getNombre() + " nv " + p.getNivel() + " (" + p.getExperiencia() + "/" + p.getExperienciaParaSubir() + " exp) · "
                    + p.getPuntosDeSalud() + "/" + p.getPuntosDeSaludMaximos() + " HP · " + p.getRecurso() + " maná"
                    + (p.estaVivo() ? "" : " · CAÍDO"));
        }
        System.out.println("  Oro: " + party.getOro() + " · Inventario: " + nombres(party.getInventario().getItems()));
    }

    private static String describir(Entidad e) {
        String texto = e.getNombre() + ": " + e.getPuntosDeSalud() + "/" + e.getPuntosDeSaludMaximos() + " HP";
        if (!e.getEfectos().isEmpty()) texto += " " + e.getEfectos();
        return e.estaVivo() ? texto : e.getNombre() + ": caído";
    }

    private static String describirArma(Armas arma) {
        if (arma == null) return "ninguna";
        return arma.getNombre() + " (+" + arma.getDanioFisico() + " fís, +" + arma.getDanioMagico() + " mág)";
    }

    private static String describirArmadura(Armaduras armadura) {
        if (armadura == null) return "ninguna";
        return armadura.getNombre() + " (+" + armadura.getDefensaFisica() + " fís, +" + armadura.getDefensaMagica() + " mág)";
    }

    private static String nombres(List<? extends Item> items) {
        List<String> nombres = new ArrayList<>();
        for (Item item : items) nombres.add(item.getNombre());
        return nombres.toString();
    }

    private static void mostrar(String mensajes) {
        if (!mensajes.isEmpty()) System.out.println(mensajes);
    }

    // pide un número entre minimo y maximo hasta que escriban uno válido
    private static int leerOpcion(int minimo, int maximo) {
        while (true) {
            System.out.print("> ");
            if (!teclado.hasNextLine()) System.exit(0);
            String linea = teclado.nextLine().trim();
            try {
                int opcion = Integer.parseInt(linea);
                if (opcion >= minimo && opcion <= maximo) return opcion;
            } catch (NumberFormatException e) {
                // no era un número: se vuelve a pedir
            }
            System.out.println("Elegí una opción entre " + minimo + " y " + maximo + ".");
        }
    }
}
