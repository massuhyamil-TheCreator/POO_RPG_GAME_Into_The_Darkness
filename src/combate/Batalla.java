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
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Batalla {

    public static final int MAXIMO_POR_BANDO = 5;

    private PartyAliada partyAliada;
    private PartyEnemiga partyEnemiga;
    private List<Entidad> ordenTurnos = new ArrayList<>();
    private int indiceTurno;
    private int ronda;
    private EstadoCombate estadoCombate;
    private List<String> registro = new ArrayList<>();
    private List<Entidad> retrasadosProximaRonda = new ArrayList<>();
    private List<Entidad> invocados = new ArrayList<>();
    private Random random = new Random();
    private boolean otorgaExperiencia;
    private int experienciaGanada;
    private int oroGanado;
    private List<Item> objetosGanados = new ArrayList<>();

    public Batalla(PartyAliada partyAliada, PartyEnemiga partyEnemiga) {
        this(partyAliada, partyEnemiga, true);
    }

    // otorgaExperiencia = false para un nodo ya consumido: se pelea igual, pero no da experiencia
    public Batalla(PartyAliada partyAliada, PartyEnemiga partyEnemiga, boolean otorgaExperiencia) {
        this.otorgaExperiencia = otorgaExperiencia;
        this.partyAliada = partyAliada;
        this.partyEnemiga = partyEnemiga;
        this.estadoCombate = EstadoCombate.EN_CURSO;
        this.ronda = 0;
        this.indiceTurno = -1;
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

    private void nuevaRonda() {
        ronda++;
        ordenTurnos.clear();
        for (Personaje p : partyAliada.getMiembrosJugador()) {
            if (p.estaVivo()) ordenTurnos.add(p);
        }
        for (Enemigo e : partyEnemiga.getMiembrosEnemigos()) {
            if (e.estaVivo()) ordenTurnos.add(e);
        }
        ordenTurnos.sort((a, b) -> {
            if (a.actuaPrimero() != b.actuaPrimero()) return a.actuaPrimero() ? -1 : 1;
            return b.getVelocidad() - a.getVelocidad();
        });
        for (Entidad retrasado : retrasadosProximaRonda) {
            int posicion = ordenTurnos.indexOf(retrasado);
            if (posicion >= 0 && posicion < ordenTurnos.size() - 1) Collections.swap(ordenTurnos, posicion, posicion + 1);
        }
        retrasadosProximaRonda.clear();
    }

    public boolean retrasarTurno(Entidad entidad) {
        int posicion = ordenTurnos.indexOf(entidad);
        if (posicion > indiceTurno) {
            // todavía no actuó en esta ronda: pasa uno para atrás ahora
            if (posicion == ordenTurnos.size() - 1) return false;
            Collections.swap(ordenTurnos, posicion, posicion + 1);
            return true;
        }
        // ya actuó: se atrasa en la ronda que viene
        if (retrasadosProximaRonda.contains(entidad)) return false;
        retrasadosProximaRonda.add(entidad);
        return true;
    }

    public boolean invocar(Entidad invocado, Entidad invocador) {
        if (aliadosDe(invocador).size() >= MAXIMO_POR_BANDO) return false;
        // se suma al bando que corresponde y empieza a actuar en la ronda siguiente
        if (invocado instanceof Personaje) partyAliada.getMiembrosJugador().add((Personaje) invocado);
        else partyEnemiga.getMiembrosEnemigos().add((Enemigo) invocado);
        invocados.add(invocado);
        return true;
    }

    public Entidad getTurnoActual() {
        return ordenTurnos.get(indiceTurno);
    }

    public boolean esTurnoDelJugador() {
        return getTurnoActual() instanceof Personaje;
    }

    public void atacar(Enemigo objetivo) {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        usarHabilidad(getTurnoActual().getBasico(), objetivo);
    }

    public void usarHabilidad(Habilidad habilidad, Entidad elegido) {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        ejecutarHabilidad(getTurnoActual(), habilidad, elegido);
    }

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

    private List<Entidad> resolverObjetivos(Habilidad habilidad, Entidad usuario, Entidad elegido) {
        List<Entidad> enemigos = enemigosDe(usuario);
        enemigos.removeIf(enemigo -> !usuario.puedeAtacarA(enemigo));
        List<Entidad> aliados = aliadosDe(usuario);
        List<Entidad> objetivos = new ArrayList<>();
        switch (habilidad.getObjetivo()) {
            case UN_ENEMIGO:
                if (enemigos.contains(elegido)) objetivos.add(elegido);
                break;
            case VARIOS_ENEMIGOS:
                Collections.shuffle(enemigos, random);
                int rango = habilidad.getMaximoObjetivos() - habilidad.getMinimoObjetivos() + 1;
                int cantidad = Math.min(enemigos.size(), habilidad.getMinimoObjetivos() + random.nextInt(rango));
                objetivos.addAll(enemigos.subList(0, cantidad));
                break;
            case TODOS_LOS_ENEMIGOS:
                objetivos.addAll(enemigos);
                break;
            case UN_ALIADO:
                if (aliados.contains(elegido)) objetivos.add(elegido);
                break;
            case OTRO_ALIADO:
                if (aliados.contains(elegido) && elegido != usuario) objetivos.add(elegido);
                break;
            case SI_MISMO:
                objetivos.add(usuario);
                break;
            case TODOS_LOS_ALIADOS:
                objetivos.addAll(aliados);
                break;
            case ALIADOS_CON_MENOS_VIDA:
                aliados.sort((a, b) -> a.getPuntosDeSalud() - b.getPuntosDeSalud());
                objetivos.addAll(aliados.subList(0, Math.min(aliados.size(), habilidad.getMaximoObjetivos())));
                break;
        }
        return objetivos;
    }

    public List<Entidad> aliadosDe(Entidad entidad) {
        if (entidad instanceof Personaje) return new ArrayList<>(personajesVivos());
        return new ArrayList<>(enemigosVivos());
    }

    public List<Entidad> enemigosDe(Entidad entidad) {
        if (entidad instanceof Personaje) return new ArrayList<>(enemigosVivos());
        return new ArrayList<>(personajesVivos());
    }

    public void usarConsumible(Consumibles consumible, Personaje objetivo) {
        if (estadoCombate != EstadoCombate.EN_CURSO || !esTurnoDelJugador()) return;
        Personaje usuario = (Personaje) getTurnoActual();
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

    public void turnoEnemigo() {
        if (estadoCombate != EstadoCombate.EN_CURSO || esTurnoDelJugador()) return;
        Enemigo enemigo = (Enemigo) getTurnoActual();
        Habilidad habilidad = enemigo.elegirHabilidad(this);
        if (!ejecutarHabilidad(enemigo, habilidad, enemigo.elegirObjetivo(habilidad, this))) {
            registro.add(enemigo.getNombre() + " duda y no hace nada.");
            terminarTurno();
        }
    }

    private void terminarTurno() {
        getTurnoActual().finalizarTurno();
        avanzarAlSiguienteTurno();
    }

    private void avanzarAlSiguienteTurno() {
        while (true) {
            actualizarEstado();
            if (estadoCombate != EstadoCombate.EN_CURSO) return;
            indiceTurno++;
            if (indiceTurno >= ordenTurnos.size()) {
                nuevaRonda();
                indiceTurno = 0;
            }
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

    private void actualizarEstado() {
        if (enemigosVivos().isEmpty()) estadoCombate = EstadoCombate.VICTORIA;
        else if (personajesVivos().isEmpty()) estadoCombate = EstadoCombate.DERROTA;
        else return;
        partyAliada.getMiembrosJugador().removeAll(invocados);
        partyEnemiga.getMiembrosEnemigos().removeAll(invocados);
        if (estadoCombate == EstadoCombate.VICTORIA) otorgarRecompensas();
        for (Personaje p : partyAliada.getMiembrosJugador()) {
            p.limpiarEfectos();
        }
    }

    private void otorgarRecompensas() {
        registro.add("¡Victoria!");
        for (Enemigo enemigo : partyEnemiga.getMiembrosEnemigos()) {
            experienciaGanada += enemigo.getExperienciaOtorgada();
            oroGanado += enemigo.getOroOtorgado();
            Item objeto = enemigo.robarObjeto();
            if (objeto != null) {
                objetosGanados.add(objeto);
                partyAliada.getInventario().agregar(objeto);
                registro.add("La party encuentra " + objeto.getNombre() + ".");
            }
        }
        partyAliada.agregarOro(oroGanado);
        if (oroGanado > 0) registro.add("La party gana " + oroGanado + " de oro.");
        if (!otorgaExperiencia) {
            experienciaGanada = 0;
            return;
        }
        // solo los que quedaron en pie
        for (Personaje p : personajesVivos()) {
            registro.addAll(p.ganarExperiencia(experienciaGanada));
        }
    }

    public void pausar() {
        if (estadoCombate == EstadoCombate.EN_CURSO) estadoCombate = EstadoCombate.PAUSADA;
    }

    public void reanudar() {
        if (estadoCombate == EstadoCombate.PAUSADA) estadoCombate = EstadoCombate.EN_CURSO;
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
    public List<Entidad> getOrdenTurnos() { return ordenTurnos; }
    public int getRonda() { return ronda; }
    public EstadoCombate getEstadoCombate() { return estadoCombate; }
    public int getExperienciaGanada() { return experienciaGanada; }
    public int getOroGanado() { return oroGanado; }
    public List<Item> getObjetosGanados() { return objetosGanados; }

    public String tomarMensajes() {
        String mensajes = String.join("\n", registro);
        registro.clear();
        return mensajes;
    }
}
