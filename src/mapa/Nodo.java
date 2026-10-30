package mapa;

import java.util.ArrayList;
import java.util.List;

public class Nodo {
    private int id;
    private int fila;      // "piso" del mapa (profundidad)
    private int columna;   // posición horizontal en esa fila
    private TipoNodo tpe;  // tipo del nodo (enumerado en TipoNodo)
    private List<Nodo> conecciones = new ArrayList<>(); // a qué nodos puede avanzar
    private boolean visitado;    // para en la parte visual marcar con una cruz o algo los nodos que ya se pasaron
    private boolean consumido;   // ya se ganó su batalla o se usó: no vuelve a dar experiencia ni premios

    public Nodo(int id, int fila, int columna) {
        this.id = id;
        this.fila = fila;
        this.columna = columna;
    }

    public int getId() {return id;}
    public int getFila() {return fila;}
    public int getColumna() {return columna;}
    public TipoNodo getTpe() {return tpe;}
    public List<Nodo> getConecciones() {return conecciones;}
    public boolean getVisitado() {return visitado;}
    public boolean isConsumido() {return consumido;}

    public void setTpe(TipoNodo tpe) {this.tpe = tpe;}
    public void setConecciones(List<Nodo> conecciones) {this.conecciones = conecciones;}
    public void setVisitado(boolean visitado) {this.visitado = visitado;}
    public void consumir() {this.consumido = true;}

    public boolean esBatalla() {
        return tpe == TipoNodo.Batalla || tpe == TipoNodo.BatallaElite || tpe == TipoNodo.BOSS;
    }

    public void conectarCon(Nodo otro) {
        if (!conecciones.contains(otro)) conecciones.add(otro);
    }
}
