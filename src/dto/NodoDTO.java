package dto;
import java.util.List;

// Un nodo del mapa tal como lo dibuja la pantalla.
public class NodoDTO {

    private final int id;
    private final int fila;
    private final int columna;
    private final String tipo;
    private final boolean visitado;
    private final boolean consumido;
    private final boolean accesible;
    private final boolean actual;
    private final List<Integer> conexiones;

    public NodoDTO(int id, int fila, int columna, String tipo, boolean visitado, boolean consumido,
                   boolean accesible, boolean actual, List<Integer> conexiones) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
        this.tipo = tipo;
        this.visitado = visitado;
        this.consumido = consumido;
        this.accesible = accesible;
        this.actual = actual;
        this.conexiones = conexiones;
    }

    public int getId() { return id; }
    public int getFila() { return fila; }
    public int getColumna() { return columna; }
    // "INICIO", "Batalla", "BatallaElite", "BOSS" o "Fogata"
    public String getTipo() { return tipo; }
    public boolean isVisitado() { return visitado; }
    public boolean isConsumido() { return consumido; }
    // ¿se puede ir a este nodo desde donde está la party?
    public boolean isAccesible() { return accesible; }
    public boolean isActual() { return actual; }
    // ids de los nodos a los que se puede avanzar desde este
    public List<Integer> getConexiones() { return conexiones; }
}
