package partida;
import combate.PartyEnemiga;
import mapa.Nodo;
import java.util.function.Function;

// Entrar a un nodo de pelea: se generan los enemigos (según el nodo) y la partida queda preparando la batalla.
public class EventoBatalla implements EventoDeNodo {

    private final Function<Nodo, PartyEnemiga> generadorDeEnemigos;

    public EventoBatalla(Function<Nodo, PartyEnemiga> generadorDeEnemigos) {
        this.generadorDeEnemigos = generadorDeEnemigos;
    }

    @Override
    public void entrar(Partida partida, Nodo nodo) {
        partida.prepararBatalla(generadorDeEnemigos.apply(nodo));
    }
}
