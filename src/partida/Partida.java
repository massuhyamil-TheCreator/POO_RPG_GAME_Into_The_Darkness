package partida;
import combate.Batalla;
import combate.EstadoCombate;
import combate.PartyAliada;
import combate.PartyEnemiga;
import entidades.enemigos.Enemigo;
import entidades.personajes.Curador;
import entidades.personajes.Personaje;
import mapa.CreadorMapa;
import mapa.Mapa;
import mapa.Nodo;
import mapa.TipoNodo;
import objetos.Catalogo;
import objetos.Inventario;
import objetos.Item;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class Partida {

    private static final Random AZAR = new Random();
    private static final int MEJORA_ARMA = 2;
    private static final int MEJORA_ARMADURA = 1;
    private static final int ORO_TESORO_MINIMO = 20;
    private static final int ORO_TESORO_MAXIMO = 50;
    private static final int CURACION_FUENTE = 15;
    private static final int DANIO_TRAMPA = 8;
    private static final int ORO_EVENTO = 30;

    private PartyAliada party;
    private long semilla;
    private Mapa mapa;
    private EstadoPartida estado = EstadoPartida.EN_MAPA;
    private PartyEnemiga enemigosPendientes;
    private Batalla batallaActual;
    private Properties antesDeLaBatalla; // si se guarda en medio de una pelea, se guarda esto
    private List<String> registro = new ArrayList<>();

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
        inventario.agregar(Catalogo.crear(Catalogo.COTA_DE_MALLA));
        inventario.agregar(Catalogo.crear(Catalogo.TUNICA_ENCANTADA));
        this.party = new PartyAliada(personajes, inventario);
        this.semilla = semilla;
        this.mapa = new CreadorMapa().generate(semilla);
    }

    // ---- Moverse por el mapa ----

    public List<Nodo> getCaminosPosibles() {
        return mapa.getCaminosPosibles();
    }

    public boolean avanzarA(Nodo nodo) {
        if (estado != EstadoPartida.EN_MAPA || !mapa.puedeMoverA(nodo)) return false;
        mapa.moveTo(nodo);
        for (Personaje p : party.getMiembrosJugador()) {
            if (p instanceof Curador) ((Curador) p).getSalvador().avanzarNodo();
        }
        entrarAlNodo(nodo);
        return true;
    }

    private void entrarAlNodo(Nodo nodo) {
        switch (nodo.getTpe()) {
            case Batalla:
                prepararBatalla(GeneradorDeEncuentros.batalla(nodo.getFila()));
                break;
            case BatallaElite:
                prepararBatalla(GeneradorDeEncuentros.elite());
                break;
            case BOSS:
                prepararBatalla(GeneradorDeEncuentros.jefe());
                break;
            case Fogata:
                registro.add("La party llega a una fogata.");
                estado = EstadoPartida.EN_FOGATA;
                break;
            case Herrero:
                registro.add("La party llega a la herrería. Se puede mejorar un arma o una armadura.");
                estado = EstadoPartida.EN_HERRERO;
                break;
            case Tesoro:
                abrirTesoro();
                break;
            case Evento:
                resolverEvento();
                break;
            default:
                break;
        }
    }

    // ---- Batallas ----

    private void prepararBatalla(PartyEnemiga enemigos) {
        enemigosPendientes = enemigos;
        List<String> nombres = new ArrayList<>();
        for (Enemigo enemigo : enemigos.getMiembrosEnemigos()) nombres.add(enemigo.getNombre());
        registro.add("¡Aparecen enemigos: " + String.join(", ", nombres) + "!");
        estado = EstadoPartida.PREPARANDO_BATALLA;
        antesDeLaBatalla = GuardadoDePartidas.capturar(this);
    }

    // se llama cuando el jugador terminó de elegir arma, armadura y pociones
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
            if (p instanceof Curador) ((Curador) p).getSalvador().descansarEnFogata();
        }
        registro.add("La party descansa junto al fuego y recupera toda la vida y el maná.");
        terminarNodo();
    }

    // ---- Herrero: se mejora una sola cosa por visita ----

    public boolean mejorarArma(Personaje personaje) {
        if (estado != EstadoPartida.EN_HERRERO) return false;
        Armas arma = personaje.getArma();
        if (arma == null) {
            registro.add(personaje.getNombre() + " no tiene un arma equipada.");
            return false;
        }
        if (arma.getDanioFisico() >= arma.getDanioMagico()) arma.setDanioFisico(arma.getDanioFisico() + MEJORA_ARMA);
        else arma.setDanioMagico(arma.getDanioMagico() + MEJORA_ARMA);
        registro.add("El herrero mejora " + arma.getNombre() + " de " + personaje.getNombre() + ".");
        terminarNodo();
        return true;
    }

    public boolean mejorarArmadura(Personaje personaje) {
        if (estado != EstadoPartida.EN_HERRERO) return false;
        Armaduras armadura = personaje.getArmadura();
        if (armadura == null) {
            registro.add(personaje.getNombre() + " no tiene una armadura equipada.");
            return false;
        }
        armadura.setDefensaFisica(armadura.getDefensaFisica() + MEJORA_ARMADURA);
        armadura.setDefensaMagica(armadura.getDefensaMagica() + MEJORA_ARMADURA);
        registro.add("El herrero refuerza " + armadura.getNombre() + " de " + personaje.getNombre() + ".");
        terminarNodo();
        return true;
    }

    public void salirDelHerrero() {
        if (estado != EstadoPartida.EN_HERRERO) return;
        terminarNodo();
    }

    // ---- Tesoro y Evento ----

    private void abrirTesoro() {
        Item objeto = Catalogo.botinAlAzar();
        int oro = ORO_TESORO_MINIMO + AZAR.nextInt(ORO_TESORO_MAXIMO - ORO_TESORO_MINIMO + 1);
        party.getInventario().agregar(objeto);
        party.agregarOro(oro);
        registro.add("¡Un cofre! La party encuentra " + objeto.getNombre() + " y " + oro + " de oro.");
        terminarNodo();
    }

    private void resolverEvento() {
        switch (AZAR.nextInt(5)) {
            case 0:
                for (Personaje p : party.getMiembrosJugador()) {
                    if (p.estaVivo()) p.curar(CURACION_FUENTE);
                }
                registro.add("Una fuente de agua clara: los que están en pie recuperan " + CURACION_FUENTE + " de vida.");
                terminarNodo();
                break;
            case 1:
                for (Personaje p : party.getMiembrosJugador()) {
                    // la trampa lastima pero no mata a nadie
                    if (p.estaVivo()) p.recibirDanio(Math.min(DANIO_TRAMPA, p.getPuntosDeSalud() - 1));
                }
                registro.add("¡Una trampa! Los que están en pie pierden hasta " + DANIO_TRAMPA + " de vida.");
                terminarNodo();
                break;
            case 2:
                Item objeto = Catalogo.botinAlAzar();
                party.getInventario().agregar(objeto);
                registro.add("Un viajero les regala " + objeto.getNombre() + ".");
                terminarNodo();
                break;
            case 3:
                party.agregarOro(ORO_EVENTO);
                registro.add("Encuentran una bolsa con " + ORO_EVENTO + " de oro.");
                terminarNodo();
                break;
            default:
                registro.add("¡Una emboscada!");
                prepararBatalla(GeneradorDeEncuentros.batalla(getNodoActual().getFila()));
                break;
        }
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
        if (enemigos != null) antesDeLaBatalla = GuardadoDePartidas.capturar(this);
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
