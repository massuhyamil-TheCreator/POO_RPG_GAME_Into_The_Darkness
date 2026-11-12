package partida;
import combate.Batalla;
import combate.EstadoCombate;
import combate.PartyAliada;
import combate.PartyEnemiga;
import entidades.enemigos.Enemigo;
import entidades.personajes.Personaje;
import mapa.CreadorMapa;
import mapa.Mapa;
import mapa.Nodo;
import mapa.TipoNodo;
import objetos.Catalogo;
import objetos.Inventario;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class Partida {

    private PartyAliada party;
    private long semilla;
    private Mapa mapa;
    private EstadoPartida estado = EstadoPartida.EN_MAPA;
    private PartyEnemiga enemigosPendientes;
    private Batalla batallaActual;
    private Properties antesDeLaBatalla; // si se guarda en medio de una pelea, se guarda esto
    private List<String> registro = new ArrayList<>();
    // qué pasa al entrar a cada tipo de nodo (agregar un tipo nuevo es agregar una línea acá)
    private final Map<TipoNodo, EventoDeNodo> eventos = new EnumMap<>(TipoNodo.class);

    public Partida(List<Personaje> personajes) {
        this(personajes, System.nanoTime());
    }

    // con la misma semilla sale el mismo mapa: así se guarda la partida sin guardar todo el mapa
    public Partida(List<Personaje> personajes, long semilla) {
        Inventario inventario = new Inventario();
        inventario.agregar(Catalogo.crear(Catalogo.POCION_VIDA_CHICA));
        inventario.agregar(Catalogo.crear(Catalogo.POCION_VIDA_CHICA));
        inventario.agregar(Catalogo.crear(Catalogo.POCION_MANA_CHICA));
        inventario.agregar(Catalogo.crear(Catalogo.ANTIDOTO));
        inventario.agregar(Catalogo.crear(Catalogo.ESPADA_LARGA));
        inventario.agregar(Catalogo.crear(Catalogo.BACULO_OSCURO));
        inventario.agregar(Catalogo.crear(Catalogo.ARCO_DE_CAZA));
        this.party = new PartyAliada(personajes, inventario);
        this.semilla = semilla;
        this.mapa = new CreadorMapa().generate(semilla);
        eventos.put(TipoNodo.Batalla, new EventoBatalla(nodo -> GeneradorDeEncuentros.batalla(nodo.getFila())));
        eventos.put(TipoNodo.BatallaElite, new EventoBatalla(nodo -> GeneradorDeEncuentros.elite()));
        eventos.put(TipoNodo.BOSS, new EventoBatalla(nodo -> GeneradorDeEncuentros.jefe()));
        eventos.put(TipoNodo.Fogata, new EventoFogata());
    }

    // ---- Moverse por el mapa ----

    public List<Nodo> getCaminosPosibles() {
        return mapa.getCaminosPosibles();
    }

    public boolean avanzarA(Nodo nodo) {
        if (estado != EstadoPartida.EN_MAPA || !mapa.puedeMoverA(nodo)) return false;
        mapa.moveTo(nodo);
        for (Personaje p : party.getMiembrosJugador()) p.alAvanzarNodo();
        entrarAlNodo(nodo);
        return true;
    }

    private void entrarAlNodo(Nodo nodo) {
        EventoDeNodo evento = eventos.get(nodo.getTpe());
        if (evento != null) evento.entrar(this, nodo);   // el nodo inicial no tiene evento
    }

    // lo usa EventoFogata
    void entrarEnFogata() {
        registro.add("La party llega a una fogata.");
        estado = EstadoPartida.EN_FOGATA;
    }

    // ---- Batallas ----

    // lo usa EventoBatalla
    void prepararBatalla(PartyEnemiga enemigos) {
        enemigosPendientes = enemigos;
        List<String> nombres = new ArrayList<>();
        for (Enemigo enemigo : enemigos.getMiembrosEnemigos()) nombres.add(enemigo.getNombre());
        registro.add("¡Aparecen enemigos: " + String.join(", ", nombres) + "!");
        estado = EstadoPartida.PREPARANDO_BATALLA;
        antesDeLaBatalla = SerializadorDePartida.capturar(this);
    }

    // se llama cuando el jugador terminó de elegir arma y pociones
    public Batalla comenzarBatalla() {
        if (estado != EstadoPartida.PREPARANDO_BATALLA) return null;
        batallaActual = new Batalla(party, enemigosPendientes, !getNodoActual().isConsumido());
        enemigosPendientes = null;
        estado = EstadoPartida.EN_BATALLA;
        return batallaActual;
    }

    // se llama cuando la batalla quedó en VICTORIA o DERROTA
    public void terminarBatalla() {
        if (estado != EstadoPartida.EN_BATALLA) return;
        EstadoCombate resultado = batallaActual.getEstadoCombate();
        if (resultado == EstadoCombate.DERROTA) {
            registro.add("La party fue derrotada. Fin de la partida.");
            estado = EstadoPartida.PERDIDA;
        } else if (resultado == EstadoCombate.VICTORIA) {
            getNodoActual().consumir();
            if (getNodoActual().getTpe() == TipoNodo.BOSS) {
                registro.add("¡El jefe cayó! La party sale de la oscuridad.");
                estado = EstadoPartida.GANADA;
            } else {
                estado = EstadoPartida.EN_MAPA;
            }
        } else {
            return; // todavía en curso o pausada
        }
        batallaActual = null;
        antesDeLaBatalla = null;
    }

    // ---- Fogata ----

    public void descansar() {
        if (estado != EstadoPartida.EN_FOGATA) return;
        for (Personaje p : party.getMiembrosJugador()) {
            if (!p.estaVivo()) registro.add(p.getNombre() + " vuelve a levantarse.");
            p.curar(p.getPuntosDeSaludMaximos());
            p.recuperarRecurso(p.getRecursoMaximo());
            p.alDescansarEnFogata();
        }
        registro.add("La party descansa junto al fuego y recupera toda la vida y el maná.");
        terminarNodo();
    }

    private void terminarNodo() {
        getNodoActual().consumir();
        estado = EstadoPartida.EN_MAPA;
    }

    // ---- Para la UI ----

    // se puede guardar en cualquier momento salvo con la partida terminada;
    // si es en medio de una pelea, al cargar se vuelve a la preparación de esa pelea
    public boolean puedeGuardar() {
        return estado != EstadoPartida.GANADA && estado != EstadoPartida.PERDIDA;
    }

    public boolean estaEnBatalla() {
        return estado == EstadoPartida.PREPARANDO_BATALLA || estado == EstadoPartida.EN_BATALLA;
    }

    Properties getAntesDeLaBatalla() {
        return antesDeLaBatalla;
    }

    // al cargar: deja la partida en el mismo estado en que se guardó
    void retomar(EstadoPartida estado, PartyEnemiga enemigos) {
        this.estado = estado;
        this.enemigosPendientes = enemigos;
        if (enemigos != null) antesDeLaBatalla = SerializadorDePartida.capturar(this);
    }

    public PartyAliada getParty() { return party; }
    public long getSemilla() { return semilla; }
    public Mapa getMapa() { return mapa; }
    public Nodo getNodoActual() { return mapa.getNodoActual(); }
    public EstadoPartida getEstado() { return estado; }
    public PartyEnemiga getEnemigosPendientes() { return enemigosPendientes; }
    public Batalla getBatallaActual() { return batallaActual; }

    public String tomarMensajes() {
        String mensajes = String.join("\n", registro);
        registro.clear();
        return mensajes;
    }
}
