package combate;
import entidades.Entidad;
import entidades.enemigos.Enemigo;
import entidades.personajes.Personaje;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// Lleva la cuenta de quién actúa y cuándo: arma el orden de cada ronda y avanza turno por turno.
// Batalla le pregunta a quién le toca; no sabe cómo se ordena.
public class OrdenDeTurnos {

    private final PartyAliada partyAliada;
    private final PartyEnemiga partyEnemiga;
    private final Comparator<Entidad> criterio = new ComparadorDeTurnos();
    private final List<Entidad> orden = new ArrayList<>();
    private final List<Entidad> retrasadosProximaRonda = new ArrayList<>();
    private int indice = -1;
    private int ronda = 0;

    public OrdenDeTurnos(PartyAliada partyAliada, PartyEnemiga partyEnemiga) {
        this.partyAliada = partyAliada;
        this.partyEnemiga = partyEnemiga;
    }

    public Entidad actual() {
        return orden.get(indice);
    }

    // pasa al siguiente turno; si la ronda se terminó, arma la que sigue
    public void siguiente() {
        indice++;
        if (indice >= orden.size()) {
            nuevaRonda();
            indice = 0;
        }
    }

    private void nuevaRonda() {
        ronda++;
        orden.clear();
        for (Personaje p : partyAliada.getMiembrosJugador()) {
            if (p.estaVivo()) orden.add(p);
        }
        for (Enemigo e : partyEnemiga.getMiembrosEnemigos()) {
            if (e.estaVivo()) orden.add(e);
        }
        orden.sort(criterio);
        for (Entidad retrasado : retrasadosProximaRonda) {
            int posicion = orden.indexOf(retrasado);
            if (posicion >= 0 && posicion < orden.size() - 1) Collections.swap(orden, posicion, posicion + 1);
        }
        retrasadosProximaRonda.clear();
    }

    public boolean retrasar(Entidad entidad) {
        int posicion = orden.indexOf(entidad);
        if (posicion > indice) {
            // todavía no actuó en esta ronda: pasa uno para atrás ahora
            if (posicion == orden.size() - 1) return false;
            Collections.swap(orden, posicion, posicion + 1);
            return true;
        }
        // ya actuó: se atrasa en la ronda que viene
        if (retrasadosProximaRonda.contains(entidad)) return false;
        retrasadosProximaRonda.add(entidad);
        return true;
    }

    public List<Entidad> getOrden() { return orden; }
    public int getRonda() { return ronda; }
}
