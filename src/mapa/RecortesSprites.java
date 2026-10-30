package mapa;

import java.awt.Rectangle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

// Lee src/mapa/datos/recortes_verificados.csv una sola vez y deja disponible,
// para cada archivo (ej. "sheet_bases_nodos.png") y cada pieza (ej. "actual"),
// el Rectangle exacto que hay que recortar de esa imagen.
//
// Columnas del CSV: archivo,ancho_png,alto_png,filas_visuales,columnas_visuales,
//                    pieza,x,y,ancho_recorte,alto_recorte,margen,separacion
public class RecortesSprites {
    private static final String RUTA_CSV = "src/mapa/datos/recortes_verificados.csv";

    // archivo -> (pieza -> rectangulo)
    private static final Map<String, Map<String, Rectangle>> RECORTES = cargar();

    private static Map<String, Map<String, Rectangle>> cargar() {
        Map<String, Map<String, Rectangle>> resultado = new HashMap<>();
        try (BufferedReader lector = new BufferedReader(new FileReader(RUTA_CSV))) {
            lector.readLine(); // encabezado: se descarta, no es un dato

            String linea;
            while ((linea = lector.readLine()) != null) {
                if (linea.isBlank()) continue;
                String[] campos = linea.split(",");

                String archivo = campos[0];
                String pieza = campos[5];
                int x = Integer.parseInt(campos[6]);
                int y = Integer.parseInt(campos[7]);
                int ancho = Integer.parseInt(campos[8]);
                int alto = Integer.parseInt(campos[9]);

                Map<String, Rectangle> piezasDeEseArchivo = resultado.get(archivo);
                if (piezasDeEseArchivo == null) {
                    piezasDeEseArchivo = new HashMap<>();
                    resultado.put(archivo, piezasDeEseArchivo);
                }
                piezasDeEseArchivo.put(pieza, new Rectangle(x, y, ancho, alto));
            }
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar " + RUTA_CSV, e);
        }
        return resultado;
    }

    // Esto es lo que usan PanelMapa, VentanaMapa, etc.
    public static Rectangle get(String archivo, String pieza) {
        Map<String, Rectangle> piezas = RECORTES.get(archivo);
        if (piezas == null) throw new IllegalArgumentException("No hay recortes cargados para " + archivo);
        Rectangle r = piezas.get(pieza);
        if (r == null) throw new IllegalArgumentException("No existe la pieza '" + pieza + "' en " + archivo);
        return r;
    }
}
