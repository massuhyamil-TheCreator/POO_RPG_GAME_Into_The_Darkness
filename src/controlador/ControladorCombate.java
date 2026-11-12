package controlador;
import combate.Batalla;
import combate.EstadoCombate;
import dto.CombateDTO;
import dto.EntidadDTO;
import dto.EstadoDelCombate;
import dto.HabilidadDTO;
import dto.ItemDTO;
import dto.SeleccionDeObjetivo;
import entidades.Entidad;
import entidades.enemigos.Enemigo;
import entidades.habilidades.Habilidad;
import entidades.habilidades.Objetivo;
import entidades.personajes.Personaje;
import objetos.consumibles.Consumibles;
import java.util.ArrayList;
import java.util.List;

// El controlador de la pelea: la pantalla le pide acciones ("defender", "usar esta habilidad sobre ese")
// y le pregunta cómo está todo; él habla con Batalla y devuelve DTOs. La pantalla no toca el modelo.
// A cada entidad se la nombra por su id: primero los aliados, después los enemigos.
public class ControladorCombate {

    private final Batalla batalla;

    ControladorCombate(Batalla batalla) {
        this.batalla = batalla;
    }

    // ---- Cómo está la pelea ----

    public CombateDTO getEstado() {
        List<Personaje> aliados = batalla.getPartyAliada().getMiembrosJugador();
        List<Enemigo> enemigos = batalla.getPartyEnemiga().getMiembrosEnemigos();
        List<EntidadDTO> aliadosDTO = new ArrayList<>();
        for (int i = 0; i < aliados.size(); i++) aliadosDTO.add(Conversor.personaje(aliados.get(i), i));
        List<EntidadDTO> enemigosDTO = new ArrayList<>();
        for (int i = 0; i < enemigos.size(); i++) enemigosDTO.add(Conversor.entidad(enemigos.get(i), aliados.size() + i));

        Entidad turno = batalla.getTurnoActual();
        EntidadDTO turnoDTO = Conversor.entidad(turno, idDe(turno));
        HabilidadDTO basico = null;
        List<HabilidadDTO> habilidades = new ArrayList<>();
        List<ItemDTO> mochila = new ArrayList<>();
        if (batalla.esTurnoDelJugador()) {
            Personaje p = personajeEnTurno();
            turnoDTO = Conversor.personaje(p, idDe(p));
            basico = Conversor.habilidad(p.getBasico(), p);
            for (Habilidad h : p.getHabilidades()) habilidades.add(Conversor.habilidad(h, p));
            for (int i = 0; i < p.getMochila().size(); i++) mochila.add(Conversor.item(p.getMochila().get(i), i));
        }
        return new CombateDTO(batalla.getRonda(), turnoDTO, batalla.esTurnoDelJugador(), estado(),
                aliadosDTO, enemigosDTO, basico, habilidades, mochila);
    }

    public EstadoDelCombate estado() {
        EstadoCombate estado = batalla.getEstadoCombate();
        return EstadoDelCombate.valueOf(estado.name());
    }

    // lo que fue pasando desde la última vez que se pidió
    public String tomarMensajes() {
        return batalla.tomarMensajes();
    }

    // ---- Acciones del jugador ----

    public void defender() {
        batalla.defender();
    }

    public void pasarTurno() {
        batalla.pasarTurno();
    }

    // una habilidad del que tiene el turno; indice -1 es su ataque básico. idObjetivo -1 si no hace falta elegir.
    public void usarHabilidad(int indice, int idObjetivo) {
        batalla.usarHabilidad(habilidad(indice), entidadPorId(idObjetivo));
    }

    public void usarConsumible(int indiceEnMochila, int idObjetivo) {
        Personaje usuario = personajeEnTurno();
        Consumibles consumible = usuario.getMochila().get(indiceEnMochila);
        batalla.usarConsumible(consumible, personajePorId(idObjetivo));
    }

    // ---- Turno de los enemigos y pausa ----

    public void turnoEnemigo() {
        batalla.turnoEnemigo();
    }

    public void pausar() {
        batalla.pausar();
    }

    public void reanudar() {
        batalla.reanudar();
    }

    // ---- Preguntas que se hacen antes de actuar ----

    // ¿se puede usar la habilidad ahora? Si no, devuelve por qué (si sí, null)
    public String motivoNoUsable(int indice) {
        if (indice < 0) return null;
        Personaje p = personajeEnTurno();
        Habilidad h = habilidad(indice);
        if (!h.estaDisponible()) return h.getNombre() + " todavía se está recargando.";
        if (!p.tieneRecurso(h.getCosto())) return p.getNombre() + " no tiene maná suficiente para " + h.getNombre() + ".";
        return null;
    }

    // ¿a quién se la puede aplicar? Usarla directo, elegir entre unos candidatos, o no hay nadie
    public SeleccionDeObjetivo objetivosDe(int indice) {
        Personaje p = personajeEnTurno();
        Objetivo objetivo = habilidad(indice).getObjetivo();
        if (!objetivo.esSobreAliados()) {
            // si está Encantado no puede atacar al vampiro que lo encantó: solo se ofrecen los demás
            List<Entidad> enemigos = batalla.enemigosDe(p);
            enemigos.removeIf(enemigo -> !p.puedeAtacarA(enemigo));
            if (enemigos.isEmpty()) {
                return SeleccionDeObjetivo.ninguno(p.getNombre() + " está encantado y no se anima a atacar a nadie."
                        + " Usá un objeto, defendete o pasá el turno.");
            }
            if (objetivo.hayQueElegirEnemigo()) return SeleccionDeObjetivo.elegir(aDTO(enemigos));
        } else if (objetivo.hayQueElegirAliado()) {
            List<Entidad> aliados = batalla.aliadosDe(p);
            if (objetivo == Objetivo.OTRO_ALIADO) aliados.remove(p);
            if (aliados.isEmpty()) {
                return SeleccionDeObjetivo.ninguno("No hay otro aliado en pie para " + habilidad(indice).getNombre() + ".");
            }
            return SeleccionDeObjetivo.elegir(aDTO(aliados));
        }
        return SeleccionDeObjetivo.directo();
    }

    // ¿puede usar objetos ahora? Si no, por qué (si sí, null)
    public String motivoSinObjetos() {
        Personaje p = personajeEnTurno();
        if (p.getMochila().isEmpty()) return p.getNombre() + " no lleva objetos.";
        if (!p.puedeUsarItems()) return p.getNombre() + " no puede usar objetos ahora.";
        return null;
    }

    // sobre quién se puede usar un objeto (los aliados)
    public List<EntidadDTO> destinosDeObjeto() {
        return aDTO(batalla.aliadosDe(personajeEnTurno()));
    }

    // ---- Auxiliares ----

    // id de una entidad: su posición entre los aliados y después los enemigos
    private int idDe(Entidad e) {
        List<Personaje> aliados = batalla.getPartyAliada().getMiembrosJugador();
        for (int i = 0; i < aliados.size(); i++) if (aliados.get(i) == e) return i;
        List<Enemigo> enemigos = batalla.getPartyEnemiga().getMiembrosEnemigos();
        for (int i = 0; i < enemigos.size(); i++) if (enemigos.get(i) == e) return aliados.size() + i;
        return -1;
    }

    private Entidad entidadPorId(int id) {
        if (id < 0) return null;
        List<Personaje> aliados = batalla.getPartyAliada().getMiembrosJugador();
        if (id < aliados.size()) return aliados.get(id);
        return batalla.getPartyEnemiga().getMiembrosEnemigos().get(id - aliados.size());
    }

    private Personaje personajePorId(int id) {
        return batalla.getPartyAliada().getMiembrosJugador().get(id);
    }

    private Personaje personajeEnTurno() {
        Entidad actual = batalla.getTurnoActual();
        for (Personaje p : batalla.getPartyAliada().getMiembrosJugador()) {
            if (p == actual) return p;
        }
        return null;
    }

    private Habilidad habilidad(int indice) {
        Personaje p = personajeEnTurno();
        return indice < 0 ? p.getBasico() : p.getHabilidades().get(indice);
    }

    private List<EntidadDTO> aDTO(List<Entidad> entidades) {
        List<EntidadDTO> dtos = new ArrayList<>();
        for (Entidad e : entidades) dtos.add(Conversor.entidad(e, idDe(e)));
        return dtos;
    }
}
