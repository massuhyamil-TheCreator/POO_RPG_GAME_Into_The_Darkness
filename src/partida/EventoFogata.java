package partida;
import mapa.Nodo;

// Entrar a una fogata: la partida queda esperando que la party decida descansar.
public class EventoFogata implements EventoDeNodo {

    @Override
    public void entrar(Partida partida, Nodo nodo) {
        partida.entrarEnFogata();
    }
}
