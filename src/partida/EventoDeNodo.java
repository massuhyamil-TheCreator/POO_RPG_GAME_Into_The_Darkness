package partida;
import mapa.Nodo;

// Lo que pasa cuando la party entra a un nodo del mapa.
// Cada tipo de nodo tiene su propia implementación: Partida no decide por tipo con un switch.
public interface EventoDeNodo {
    void entrar(Partida partida, Nodo nodo);
}
