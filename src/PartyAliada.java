import entidades.personajes.Personaje;
import java.util.List;

public class PartyAliada {

    private List<Personaje> miembrosJugador;

    public PartyAliada(List<Personaje> miembrosJugador) {
        this.miembrosJugador = miembrosJugador;
    }

    public List<Personaje> getMiembrosJugador() {
        return miembrosJugador;
    }

    public void setMiembrosJugador(List<Personaje> miembrosJugador) {
        this.miembrosJugador = miembrosJugador;
    }
}
