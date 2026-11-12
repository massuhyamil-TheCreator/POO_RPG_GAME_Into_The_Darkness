package combate;
import entidades.Entidad;
import entidades.efectos.Defensa;
import entidades.efectos.Efecto;
import entidades.enemigos.Enemigo;
import entidades.habilidades.Habilidad;
import entidades.personajes.Personaje;
import objetos.Item;
import objetos.consumibles.Consumibles;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Coordina una batalla: valida y ejecuta las acciones y lleva el estado del combate.
// El orden de los turnos lo lleva OrdenDeTurnos, la elección de objetivos cada tipo de Objetivo
// y el reparto del botín Recompensas.
public class Batalla {

    public static final int MAXIMO_POR_BANDO = 5;

    private PartyAliada partyAliada;
    private PartyEnemiga partyEnemiga;
    private OrdenDeTurnos ordenDeTurnos;
    private EstadoCombate estadoCombate;
    private List<String> registro = new ArrayList<>();
    private List<Entidad> invocados = new ArrayList<>();
    private Random random = new Random();
    private boolean otorgaExperiencia;
    private Recompensas recompensas = new Recompensas();

    public Batalla(PartyAliada partyAliada, PartyEnemiga partyEnemiga) {
        this(partyAliada, partyEnemiga, true);
    }

    // otorgaExperiencia = false para un nodo ya consumido: se pelea igual, pero no da experiencia
    public Batalla(PartyAliada partyAliada, PartyEnemiga partyEnemiga, boolean otorgaExperiencia) {
        this.otorgaExperiencia = otorgaExperiencia;
        this.partyAliada = partyAliada;
        this.partyEnemiga = partyEnemiga;
        this.ordenDeTurnos = new OrdenDeTurnos(partyAliada, partyEnemiga);
        this.estadoCombate = EstadoCombate.EN_CURSO;
        for (Entidad entidad : todasLasEntidades()) {
            for (Efecto efecto : new ArrayList<>(entidad.getEfectos())) {
                efecto.alComenzarBatalla(this);
            }
        }
        avanzarAlSiguienteTurno();
    }

    private List<Entidad> todasLasEntidades() {
        List<Entidad> todas = new ArrayList<>(partyAliada.getMiembrosJugador());
        todas.addAll(partyEnemiga.getMiembrosEnemigos());
        return todas;
    }

    public boolean retrasarTurno(Entidad entidad) {
        return ordenDeTurnos.retrasar(entidad);
    }

    public boolean invocar(Entidad invocado, Entidad invocador) {
        if (aliadosDe(invocador).size() >= MAXIMO_POR_BANDO) return false;
        // se suma al bando que le corresponde (lo decide el propio invocado) y empieza a actuar en la ronda siguiente
        invocado.unirseA(this);
        invocados.add(invocado);
        return true;
    }

    public Entidad getTurnoActual() {
        return ordenDeTurnos.actual();
    }

    public boolean esTurnoDelJugador() {
        return getTurnoActual().esAliado();
    }

    // ---- Acciones del jugador ----

    public void atacar(Enemigo objetivo) {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        usarHabilidad(getTurnoActual().getBasico(), objetivo);
    }

    public void usarHabilidad(Habilidad habilidad, Entidad elegido) {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        ejecutarHabilidad(getTurnoActual(), habilidad, elegido);
    }

    public void usarConsumible(Consumibles consumible, Personaje objetivo) {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        Personaje usuario = personajeEnTurno();
        if (!usuario.puedeUsarItems()) {
            registro.add(usuario.getNombre() + " no puede usar objetos ahora.");
            return;
        }
        if (!usuario.getMochila().contains(consumible)) {
            registro.add(usuario.getNombre() + " no lleva " + consumible.getNombre() + ".");
            return;
        }
        if (!objetivo.estaVivo()) {
            registro.add(objetivo.getNombre() + " está caído, " + consumible.getNombre() + " no lo revive.");
            return;
        }
        usuario.sacarDeMochila(consumible);
        registro.add(usuario.getNombre() + " usó " + consumible.getNombre() + " en " + objetivo.getNombre() + ".");
        registro.add(consumible.usar(objetivo));
        terminarTurno();
    }

    public void defender() {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        Entidad actual = getTurnoActual();
        actual.aplicarEfecto(new Defensa());
        registro.add(actual.getNombre() + " se pone en guardia: recibe la mitad del daño hasta su próximo turno.");
        terminarTurno();
    }

    public void pasarTurno() {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        registro.add(getTurnoActual().getNombre() + " pasa el turno.");
        terminarTurno();
    }

    // ---- Turno de los enemigos ----

    public void turnoEnemigo() {
        if (estadoCombate != EstadoCombate.EN_CURSO || esTurnoDelJugador()) return;
        Enemigo enemigo = enemigoEnTurno();
        Habilidad habilidad = enemigo.elegirHabilidad(this);
        if (!ejecutarHabilidad(enemigo, habilidad, enemigo.elegirObjetivo(habilidad, this))) {
            registro.add(enemigo.getNombre() + " duda y no hace nada.");
            terminarTurno();
        }
    }

    // ---- Ejecución de habilidades ----

    private boolean ejecutarHabilidad(Entidad usuario, Habilidad habilidad, Entidad elegido) {
        if (!usuario.conoce(habilidad) || !habilidad.estaDisponible()) return false;
        if (!usuario.tieneRecurso(habilidad.getCosto())) {
            registro.add(usuario.getNombre() + " no tiene maná suficiente para " + habilidad.getNombre() + ".");
            return false;
        }
        List<Entidad> objetivos = resolverObjetivos(habilidad, usuario, elegido);
        if (objetivos.isEmpty()) {
            if (elegido != null && !usuario.puedeAtacarA(elegido)) {
                registro.add(usuario.getNombre() + " está encantado y no se atreve a atacar a " + elegido.getNombre() + ".");
            } else {
                registro.add("Elegí un objetivo válido para " + habilidad.getNombre() + ".");
            }
            return false;
        }
        registro.add(usuario.getNombre() + " usa " + habilidad.getNombre() + ".");
        if (habilidad.getCosto() > 0 && usuario.ahorraCosto()) {
            registro.add("  ¡No gastó maná!");
        } else {
            usuario.gastarRecurso(habilidad.getCosto());
        }
        habilidad.registrarUso();
        registro.addAll(habilidad.ejecutar(usuario, objetivos, this));
        terminarTurno();
        return true;
    }

    // cada tipo de Objetivo sabe elegir a los suyos; acá solo se arman las listas de aliados y enemigos
    private List<Entidad> resolverObjetivos(Habilidad habilidad, Entidad usuario, Entidad elegido) {
        List<Entidad> enemigos = enemigosDe(usuario);
        enemigos.removeIf(enemigo -> !usuario.puedeAtacarA(enemigo));
        List<Entidad> aliados = aliadosDe(usuario);
        return habilidad.getObjetivo().resolver(usuario, elegido, aliados, enemigos, habilidad, random);
    }

    public List<Entidad> aliadosDe(Entidad entidad) {
        if (entidad.esAliado()) return new ArrayList<>(personajesVivos());
        return new ArrayList<>(enemigosVivos());
    }

    public List<Entidad> enemigosDe(Entidad entidad) {
        if (entidad.esAliado()) return new ArrayList<>(enemigosVivos());
        return new ArrayList<>(personajesVivos());
    }

    // ---- Avance de los turnos ----

    private void terminarTurno() {
        getTurnoActual().finalizarTurno();
        avanzarAlSiguienteTurno();
    }

    private void avanzarAlSiguienteTurno() {
        while (true) {
            actualizarEstado();
            if (estadoCombate != EstadoCombate.EN_CURSO) return;
            ordenDeTurnos.siguiente();
            Entidad actual = getTurnoActual();
            if (!actual.estaVivo()) continue;
            if (comenzarTurno(actual)) return;
            actual.finalizarTurno();
        }
    }

    private boolean comenzarTurno(Entidad actual) {
        registro.addAll(actual.iniciarTurno());
        if (!actual.estaVivo()) {
            registro.add(actual.getNombre() + " cayó por sus heridas.");
            return false;
        }
        if (!actual.puedeActuar()) {
            registro.add(actual.getNombre() + " no puede actuar este turno.");
            return false;
        }
        if (actual.estaDescontrolado()) {
            atacarAUnAliado(actual);
            return false;
        }
        return true;
    }

    private void atacarAUnAliado(Entidad descontrolado) {
        List<Entidad> aliados = aliadosDe(descontrolado);
        aliados.remove(descontrolado);
        if (aliados.isEmpty()) return;
        Entidad victima = aliados.get(random.nextInt(aliados.size()));
        registro.add(descontrolado.getNombre() + " pierde el control y ataca a " + victima.getNombre() + "!");
        registro.addAll(descontrolado.getBasico().ejecutar(descontrolado, List.of(victima), this));
    }

    // ---- Estado y final del combate ----

    private void actualizarEstado() {
        if (enemigosVivos().isEmpty()) estadoCombate = EstadoCombate.VICTORIA;
        else if (personajesVivos().isEmpty()) estadoCombate = EstadoCombate.DERROTA;
        else return;
        partyAliada.getMiembrosJugador().removeAll(invocados);
        partyEnemiga.getMiembrosEnemigos().removeAll(invocados);
        if (estadoCombate == EstadoCombate.VICTORIA) {
            registro.add("¡Victoria!");
            registro.addAll(recompensas.otorgar(partyAliada, partyEnemiga, otorgaExperiencia));
        }
        for (Personaje p : partyAliada.getMiembrosJugador()) {
            p.limpiarEfectos();
        }
    }

    public void pausar() {
        if (estadoCombate == EstadoCombate.EN_CURSO) estadoCombate = EstadoCombate.PAUSADA;
    }

    public void reanudar() {
        if (estadoCombate == EstadoCombate.PAUSADA) estadoCombate = EstadoCombate.EN_CURSO;
    }

    // ---- Consultas ----

    // el que está en turno, con su tipo exacto (se busca en su bando, sin castear)
    private Personaje personajeEnTurno() {
        Entidad actual = getTurnoActual();
        for (Personaje p : partyAliada.getMiembrosJugador()) {
            if (p == actual) return p;
        }
        return null;
    }

    private Enemigo enemigoEnTurno() {
        Entidad actual = getTurnoActual();
        for (Enemigo e : partyEnemiga.getMiembrosEnemigos()) {
            if (e == actual) return e;
        }
        return null;
    }

    public List<Personaje> personajesVivos() {
        List<Personaje> vivos = new ArrayList<>();
        for (Personaje p : partyAliada.getMiembrosJugador()) {
            if (p.estaVivo()) vivos.add(p);
        }
        return vivos;
    }

    public List<Enemigo> enemigosVivos() {
        List<Enemigo> vivos = new ArrayList<>();
        for (Enemigo e : partyEnemiga.getMiembrosEnemigos()) {
            if (e.estaVivo()) vivos.add(e);
        }
        return vivos;
    }

    public PartyAliada getPartyAliada() { return partyAliada; }
    public PartyEnemiga getPartyEnemiga() { return partyEnemiga; }
    public List<Entidad> getOrdenTurnos() { return ordenDeTurnos.getOrden(); }
    public int getRonda() { return ordenDeTurnos.getRonda(); }
    public EstadoCombate getEstadoCombate() { return estadoCombate; }
    public int getExperienciaGanada() { return recompensas.getExperiencia(); }
    public int getOroGanado() { return recompensas.getOro(); }
    public List<Item> getObjetosGanados() { return recompensas.getObjetos(); }

    public String tomarMensajes() {
        String mensajes = String.join("\n", registro);
        registro.clear();
        return mensajes;
    }
}
