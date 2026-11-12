package dto;
import java.util.List;

// Cómo está la pelea en este momento. Si le toca a un personaje, trae también sus acciones.
public class CombateDTO {

    private final int ronda;
    private final EntidadDTO turnoActual;
    private final boolean turnoDelJugador;
    private final EstadoDelCombate estado;
    private final List<EntidadDTO> aliados;
    private final List<EntidadDTO> enemigos;
    private final HabilidadDTO basico;
    private final List<HabilidadDTO> habilidades;
    private final List<ItemDTO> mochila;

    public CombateDTO(int ronda, EntidadDTO turnoActual, boolean turnoDelJugador, EstadoDelCombate estado,
                      List<EntidadDTO> aliados, List<EntidadDTO> enemigos,
                      HabilidadDTO basico, List<HabilidadDTO> habilidades, List<ItemDTO> mochila) {
        this.ronda = ronda;
        this.turnoActual = turnoActual;
        this.turnoDelJugador = turnoDelJugador;
        this.estado = estado;
        this.aliados = aliados;
        this.enemigos = enemigos;
        this.basico = basico;
        this.habilidades = habilidades;
        this.mochila = mochila;
    }

    public int getRonda() { return ronda; }
    public EntidadDTO getTurnoActual() { return turnoActual; }
    public boolean esTurnoDelJugador() { return turnoDelJugador; }
    public EstadoDelCombate getEstado() { return estado; }
    public List<EntidadDTO> getAliados() { return aliados; }
    public List<EntidadDTO> getEnemigos() { return enemigos; }
    // el ataque básico, las habilidades y la mochila del personaje que tiene el turno (null/vacías si le toca a un enemigo)
    public HabilidadDTO getBasico() { return basico; }
    public List<HabilidadDTO> getHabilidades() { return habilidades; }
    public List<ItemDTO> getMochila() { return mochila; }
}
