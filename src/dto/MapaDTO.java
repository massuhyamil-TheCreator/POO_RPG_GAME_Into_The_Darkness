package dto;
import java.util.List;

public class MapaDTO {

    private final List<List<NodoDTO>> filas;

    public MapaDTO(List<List<NodoDTO>> filas) {
        this.filas = filas;
    }

    public List<List<NodoDTO>> getFilas() { return filas; }

    public NodoDTO getActual() {
        for (List<NodoDTO> fila : filas)
            for (NodoDTO nodo : fila)
                if (nodo.isActual()) return nodo;
        return null;
    }

    public NodoDTO buscar(int id) {
        for (List<NodoDTO> fila : filas)
            for (NodoDTO nodo : fila)
                if (nodo.getId() == id) return nodo;
        return null;
    }

    // cantidad de nodos del nivel más ancho (la pantalla lo usa para repartir el espacio)
    public int getMaximoNodosPorFila() {
        int maximo = 1;
        for (List<NodoDTO> fila : filas) maximo = Math.max(maximo, fila.size());
        return maximo;
    }
}
