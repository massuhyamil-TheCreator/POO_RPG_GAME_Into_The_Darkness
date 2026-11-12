package controlador;
import combate.Batalla;
import combate.EstadoCombate;
import dto.EntidadDTO;
import dto.EstadoDelJuego;
import dto.ItemDTO;
import dto.MapaDTO;
import dto.NodoDTO;
import dto.PartidaDTO;
import dto.ResultadoFinal;
import dto.ResumenDeBatalla;
import entidades.personajes.Personaje;
import excepciones.PartidaException;
import mapa.Mapa;
import mapa.Nodo;
import objetos.Inventario;
import objetos.Item;
import partida.CreadorDePersonajes;
import partida.EstadoPartida;
import partida.GuardadoDePartidas;
import partida.Partida;
import java.util.ArrayList;
import java.util.List;

// El controlador de la partida: las pantallas le piden todo lo que pasa en el juego (empezar, cargar, guardar,
// moverse por el mapa, preparar la pelea, terminarla) y le preguntan cómo está todo.
// Él habla con el modelo y devuelve DTOs: ninguna pantalla crea ni modifica objetos del modelo.
public class ControladorJuego {

    private static ControladorJuego instancia;

    private Partida partida;
    private int ranura;
    private ControladorCombate combate;

    private ControladorJuego() {
    }

    public static ControladorJuego getInstancia() {
        if (instancia == null) instancia = new ControladorJuego();
        return instancia;
    }

    // ---- Ranuras de guardado ----

    public boolean hayPartida(int ranura) {
        return GuardadoDePartidas.hayPartida(ranura);
    }

    public String describirRanura(int ranura) {
        return GuardadoDePartidas.describir(ranura);
    }

    public void borrarPartida(int ranura) throws PartidaException {
        GuardadoDePartidas.borrar(ranura);
    }

    // arma una partida con esas clases de personaje, la guarda en la ranura y queda como la partida actual
    public void nuevaPartida(int ranura, List<String> clases) throws PartidaException {
        Partida nueva = new Partida(CreadorDePersonajes.crearParty(clases));
        GuardadoDePartidas.guardar(nueva, ranura);
        adoptar(nueva, ranura);
    }

    public void cargarPartida(int ranura) throws PartidaException {
        adoptar(GuardadoDePartidas.cargar(ranura), ranura);
    }

    private void adoptar(Partida nueva, int ranuraNueva) {
        this.partida = nueva;
        this.ranura = ranuraNueva;
        this.combate = null;
    }

    // guarda la partida actual en su ranura
    public void guardar() throws PartidaException {
        GuardadoDePartidas.guardar(partida, ranura);
    }

    public int getRanura() {
        return ranura;
    }

    public boolean estaEnBatalla() {
        return partida.estaEnBatalla();
    }

    // ---- El mapa ----

    public EstadoDelJuego getEstado() {
        EstadoPartida estado = partida.getEstado();
        return EstadoDelJuego.valueOf(estado.name());
    }

    public String tomarMensajes() {
        return partida.tomarMensajes();
    }

    public MapaDTO getMapa() {
        Mapa mapa = partida.getMapa();
        Nodo actual = mapa.getNodoActual();
        List<List<NodoDTO>> filas = new ArrayList<>();
        for (List<Nodo> fila : mapa.getFilas()) {
            List<NodoDTO> nodos = new ArrayList<>();
            for (Nodo nodo : fila) {
                List<Integer> conexiones = new ArrayList<>();
                for (Nodo siguiente : nodo.getConecciones()) conexiones.add(siguiente.getId());
                nodos.add(new NodoDTO(nodo.getId(), nodo.getFila(), nodo.getColumna(), nodo.getTpe().name(),
                        nodo.getVisitado(), nodo.isConsumido(), mapa.puedeMoverA(nodo), nodo == actual, conexiones));
            }
            filas.add(nodos);
        }
        return new MapaDTO(filas);
    }

    public PartidaDTO getPartida() {
        List<EntidadDTO> party = new ArrayList<>();
        List<Personaje> miembros = partida.getParty().getMiembrosJugador();
        for (int i = 0; i < miembros.size(); i++) party.add(Conversor.personaje(miembros.get(i), i));
        Inventario inventario = partida.getParty().getInventario();
        List<ItemDTO> armas = new ArrayList<>();
        for (Item arma : inventario.getArmas()) armas.add(Conversor.item(arma, armas.size()));
        List<ItemDTO> consumibles = new ArrayList<>();
        for (Item consumible : inventario.getConsumibles()) consumibles.add(Conversor.item(consumible, consumibles.size()));
        return new PartidaDTO(party, partida.getParty().getOro(), partida.getNodoActual().getFila(),
                partida.getMapa().getFilas().size() - 1, armas, consumibles);
    }

    // la party avanza al nodo; false si no se puede ir ahí
    public boolean avanzarA(int idNodo) {
        Nodo nodo = partida.getMapa().buscarNodo(idNodo);
        return nodo != null && partida.avanzarA(nodo);
    }

    public void descansar() {
        partida.descansar();
    }

    // ---- Preparar la pelea ----

    // los personajes que pueden pelear (los caídos no), con su id para las acciones de abajo
    public List<EntidadDTO> personajesEnPie() {
        List<EntidadDTO> enPie = new ArrayList<>();
        List<Personaje> miembros = partida.getParty().getMiembrosJugador();
        for (int i = 0; i < miembros.size(); i++) {
            if (miembros.get(i).estaVivo()) enPie.add(Conversor.personaje(miembros.get(i), i));
        }
        return enPie;
    }

    public List<String> descripcionDeLosEnemigos() {
        List<String> nombres = new ArrayList<>();
        for (entidades.enemigos.Enemigo enemigo : partida.getEnemigosPendientes().getMiembrosEnemigos()) {
            nombres.add(enemigo.getNombre() + " (" + enemigo.getPuntosDeSalud() + " HP)");
        }
        return nombres;
    }

    public void equiparArma(int idPersonaje, int indiceDeArma) {
        Personaje personaje = partida.getParty().getMiembrosJugador().get(idPersonaje);
        partida.getParty().equiparArma(personaje, partida.getParty().getInventario().getArmas().get(indiceDeArma));
    }

    // false si no entra en su mochila
    public boolean darConsumible(int idPersonaje, int indiceDeConsumible) {
        Personaje personaje = partida.getParty().getMiembrosJugador().get(idPersonaje);
        return partida.getParty().darConsumible(personaje, partida.getParty().getInventario().getConsumibles().get(indiceDeConsumible));
    }

    public void vaciarMochila(int idPersonaje) {
        partida.getParty().vaciarMochila(partida.getParty().getMiembrosJugador().get(idPersonaje));
    }

    // empieza la pelea y devuelve el controlador para jugarla
    public ControladorCombate comenzarBatalla() {
        Batalla batalla = partida.comenzarBatalla();
        combate = new ControladorCombate(batalla);
        return combate;
    }

    public ControladorCombate getCombate() {
        if (combate == null && partida.getBatallaActual() != null) combate = new ControladorCombate(partida.getBatallaActual());
        return combate;
    }

    // ---- El final ----

    // cierra la pelea (que quedó en victoria o derrota) y devuelve el resumen de lo que se ganó
    public ResumenDeBatalla terminarBatalla() {
        Batalla batalla = partida.getBatallaActual();
        boolean ganada = batalla.getEstadoCombate() == EstadoCombate.VICTORIA;
        List<String> objetos = new ArrayList<>();
        for (Item item : batalla.getObjetosGanados()) objetos.add(item.getNombre());
        ResumenDeBatalla resumen = new ResumenDeBatalla(ganada, batalla.getExperienciaGanada(), batalla.getOroGanado(), objetos);
        partida.terminarBatalla();
        combate = null;
        return resumen;
    }

    public ResultadoFinal getResultadoFinal() {
        boolean ganada = partida.getEstado() == EstadoPartida.GANADA;
        List<String> party = new ArrayList<>();
        for (Personaje p : partida.getParty().getMiembrosJugador()) {
            party.add(p.getNombre() + "  ·  nivel " + p.getNivel());
        }
        return new ResultadoFinal(ganada, partida.tomarMensajes(), partida.getNodoActual().getFila(), party,
                partida.getParty().getOro(), GuardadoDePartidas.hayPartida(ranura));
    }
}
