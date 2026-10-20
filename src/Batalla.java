import entidades.personajes.Personaje;
import entidades.enemigos.Enemigo;
import entidades.Entidad;
import java.util.List;

public class Batalla {

    private String nodo;
    private List<Personaje> party;
    private List<Enemigo> enemigos;
    private List<Entidad> ordenTurnos;
    private Entidad turnoActual;
    private EstadoCombate estadoCombate;

    public Batalla(String nodo, List<Personaje> party, List<Enemigo> enemigos, List<Entidad> ordenTurnos, Entidad turnoActual, EstadoCombate estadoCombate) {
        this.nodo = nodo;
        this.party = party;
        this.enemigos = enemigos;
        this.ordenTurnos = ordenTurnos;
        this.turnoActual = turnoActual;
        this.estadoCombate = estadoCombate;
    }

    public String getNodo() {
        return nodo;
    }

    public void setNodo(String nodo) {
        this.nodo = nodo;
    }

    public List<Personaje> getParty() {
        return party;
    }

    public void setParty(List<Personaje> party) {
        this.party = party;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }

    public void setEnemigos(List<Enemigo> enemigos) {
        this.enemigos = enemigos;
    }

    public List<Entidad> getOrdenTurnos() {
        return ordenTurnos;
    }

    public void setOrdenTurnos(List<Entidad> ordenTurnos) {
        this.ordenTurnos = ordenTurnos;
    }

    public Entidad getTurnoActual() {
        return turnoActual;
    }

    public void setTurnoActual(Entidad turnoActual) {
        this.turnoActual = turnoActual;
    }

    public EstadoCombate getEstadoCombate() {
        return estadoCombate;
    }

    public void setEstadoCombate(EstadoCombate estadoCombate) {
        this.estadoCombate = estadoCombate;
    }
}
