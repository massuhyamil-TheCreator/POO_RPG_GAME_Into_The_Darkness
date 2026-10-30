package mapa;

import java.util.List;

public class Mapa {
    private List<List<Nodo>> rows; // rows.get(0) = nodo inicial (unico)
    private Nodo nodoActual;

    public Mapa(List<List<Nodo>> rows) {
        this.rows = rows;
        this.nodoActual = rows.get(0).get(0);
        this.nodoActual.setVisitado(true);
    }

    public List<List<Nodo>> getFilas() { return rows; }

    public Nodo getNodoActual() { return nodoActual; }

    public List<Nodo> getCaminosPosibles() {
        return nodoActual.getConecciones();
    }

    // Solo se puede avanzar hacia un nodo conectado (siempre de la fila siguiente),
    // asi que nunca se puede volver ni repetir un nodo.
    public boolean puedeMoverA(Nodo node) {
        return node != null && nodoActual.getConecciones().contains(node);
    }

    public void moveTo(Nodo node) {
        if (!puedeMoverA(node))
            throw new IllegalStateException("Camino inválido");
        nodoActual = node;
        nodoActual.setVisitado(true);
    }

    public Nodo buscarNodo(int id) {
        for (List<Nodo> fila : rows)
            for (Nodo n : fila)
                if (n.getId() == id) return n;
        return null;
    }

    // para cargar una partida guardada
    public void ubicarEn(Nodo node) {
        nodoActual = node;
        nodoActual.setVisitado(true);
    }

    public boolean llegoAlBoss() {
        return nodoActual.getTpe() == TipoNodo.BOSS;
    }
}
