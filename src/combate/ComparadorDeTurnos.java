package combate;
import entidades.Entidad;
import java.util.Comparator;

// Criterio para ordenar los turnos de una ronda: primero los que "actúan primero" y después
// de mayor a menor velocidad (un Comparator, como en la Clase 12).
public class ComparadorDeTurnos implements Comparator<Entidad> {

    @Override
    public int compare(Entidad a, Entidad b) {
        if (a.actuaPrimero() != b.actuaPrimero()) return a.actuaPrimero() ? -1 : 1;
        return b.getVelocidad() - a.getVelocidad();
    }
}
