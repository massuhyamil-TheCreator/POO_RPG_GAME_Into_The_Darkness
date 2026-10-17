package mapa;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreadorMapa {
    // Cantidad de nodos por nivel: inicio, 2, 4, 3, 4, 3, 4, 3, 2, boss (10 niveles)
    private static final int[] NodosPorFila = {1, 2, 4, 3, 4, 3, 4, 3, 2, 1};
    private static final int totalfilas = NodosPorFila.length;
    public static final int MaximoNodosFila = 4;       // el nivel mas ancho (lo usa la UI para dibujar)

    public Mapa generate(long seed) {
        Random rng = new Random(seed);
        List<List<Nodo>> filas = crearfilasVacias();
        List<List<Nodo>> caminos = generarCaminos(filas);
        conectarCaminos(caminos);
        asignarTipoNodo(filas, rng);
        asignarBoss(filas);
        return new Mapa(filas);
    }

    // Crea los nodos de cada nivel. La columna es el indice dentro del nivel (0..n-1);
    // la UI los centra, asi que cada nivel queda simetrico.
    private List<List<Nodo>> crearfilasVacias() {
        List<List<Nodo>> filas = new ArrayList<>();
        int id = 0;
        for (int f = 0; f < totalfilas; f++) {
            List<Nodo> fila = new ArrayList<>();
            for (int c = 0; c < NodosPorFila[f]; c++) fila.add(new Nodo(id++, f, c));
            filas.add(fila);
        }
        return filas;
    }

    // Cada "camino" es un enlace [origen, destino] entre dos niveles consecutivos.
    // Se reparte cada nivel en tramos iguales y se enlazan los nodos cuyos tramos se superponen:
    //   1 -> 2: el inicial va a los dos       2 -> 4: cada uno se divide en dos
    //   4 -> 3, 3 -> 4, 3 -> 2: los del medio comparten un nodo       2 -> 1: ambos van al boss
    // Asi no hay cruces, todos los nodos tienen entrada y salida, y el resultado es simetrico.
    private List<List<Nodo>> generarCaminos(List<List<Nodo>> filas) {
        List<List<Nodo>> caminos = new ArrayList<>();
        for (int f = 0; f < totalfilas - 1; f++) {
            List<Nodo> origen = filas.get(f);
            List<Nodo> destino = filas.get(f + 1);
            int a = origen.size();
            int b = destino.size();
            for (int i = 0; i < a; i++) {
                for (int j = 0; j < b; j++) {
                    if (i * b < (j + 1) * a && j * a < (i + 1) * b) {
                        List<Nodo> enlace = new ArrayList<>();
                        enlace.add(origen.get(i));
                        enlace.add(destino.get(j));
                        caminos.add(enlace);
                    }
                }
            }
        }
        return caminos;
    }

    private void conectarCaminos(List<List<Nodo>> caminos) {
        for (List<Nodo> camino : caminos)
            for (int i = 0; i < camino.size() - 1; i++)
                camino.get(i).conectarCon(camino.get(i + 1));
    }

    private void asignarTipoNodo(List<List<Nodo>> filas, Random rng) {
        for (int f = 0; f < filas.size(); f++) {
            for (Nodo n : filas.get(f)) {
                if (f == 0) n.setTpe(TipoNodo.INICIO);
                else if (f == filas.size() - 1) n.setTpe(TipoNodo.BOSS);
                else if (f == 1) n.setTpe(TipoNodo.Batalla);                  // primera fila siempre batalla
                else if (f == filas.size() - 2) n.setTpe(TipoNodo.Fogata);    // descanso antes del boss
                else n.setTpe(tipoAleatorio(f, rng));
            }
        }
    }

    private TipoNodo tipoAleatorio(int fila, Random rng) {
        int r = rng.nextInt(100);
        if (r < 45) return TipoNodo.Batalla;
        if (r < 55) return fila >= 4 ? TipoNodo.BatallaElite : TipoNodo.Batalla;
        if (r < 67) return TipoNodo.Tienda;
        if (r < 82) return TipoNodo.Evento;
        if (r < 92) return TipoNodo.Fogata;
        return TipoNodo.Herrero;
    }

    private void asignarBoss(List<List<Nodo>> filas) {
        filas.get(filas.size() - 1).get(0).setTpe(TipoNodo.BOSS);
    }
}
