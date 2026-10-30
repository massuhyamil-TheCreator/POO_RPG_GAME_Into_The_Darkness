package mapa;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.function.Consumer;

public class PanelMapa extends JPanel {
    private static final int RADIO = 22;              // para detectar clicks (la mitad de TAMANO_NODO)
    private static final int TAMANO_NODO = 44;         // ancho/alto en pantalla de cada nodo normal
    private static final int TAMANO_BOSS = 80;          // la puerta del jefe es mas grande
    private static final int GROSOR_CONEXION = 8;
    private static final int MARGEN = 40;
    private static final int SEPARACION_MAX = 130;   // separacion horizontal maxima entre columnas (evita que se estire con la ventana maximizada)
    private static final double FRACCION_SUPERFICIE = 0.9;   // que porcentaje del ancho del panel ocupa la franja de piedra (el fondo del castillo lo dibuja PantallaMapa, detras de todo)

    private static final String DIR_IMAGENES = "src/mapa/imagenes/";
    private static final BufferedImage SHEET_SUPERFICIE = cargarImagen("superficie_mapa.png");
    private static final BufferedImage SHEET_BASES = cargarImagen("sheet_bases_nodos.png");
    private static final BufferedImage SHEET_ICONOS = cargarImagen("sheet_iconos_nodos.png");
    private static final BufferedImage SHEET_INDICADORES = cargarImagen("sheet_indicadores_nodos.png");
    private static final BufferedImage SHEET_CONEXIONES = cargarImagen("sheet_conexiones.png");
    private static final BufferedImage SHEET_PUERTA = cargarImagen("sheet_puerta_jefe.png");

    private Mapa mapa;
    // quien usa el panel decide qué pasa al elegir un nodo (la partida avanza, el mapa se mueve, etc.)
    private final Consumer<Nodo> alElegir;

    public PanelMapa(Mapa mapa, Consumer<Nodo> alElegir) {
        this.mapa = mapa;
        this.alElegir = alElegir;
        setPreferredSize(new Dimension(420, 720));
        setBackground(new Color(35, 35, 42));
        setToolTipText("");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Nodo n = nodoEn(e.getPoint());
                if (n != null && PanelMapa.this.mapa.puedeMoverA(n)) {
                    PanelMapa.this.alElegir.accept(n);
                    repaint();
                }
            }
        });
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                Nodo n = nodoEn(e.getPoint());
                setToolTipText(n == null ? null : n.getTpe().name());
                setCursor(Cursor.getPredefinedCursor(
                        PanelMapa.this.mapa.puedeMoverA(n) ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
            }
        });
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
        repaint();
    }

    // Ancho de la franja de piedra (superficie_mapa), centrada en el panel. Los nodos se ubican
    // dentro de esta franja, no del panel entero, para que no queden flotando sobre el castillo.
    private int anchoSuperficie() {
        return (int) (getWidth() * FRACCION_SUPERFICIE);
    }

    // Cada nivel se centra horizontalmente, asi el mapa queda simetrico
    private Point posicion(Nodo n) {
        int totalFilas = mapa.getFilas().size();
        int enFila = mapa.getFilas().get(n.getFila()).size();
        int ancho = anchoSuperficie() - 2 * MARGEN;
        int alto = getHeight() - 2 * MARGEN;
        double separacion = Math.min((double) ancho / (CreadorMapa.MaximoNodosFila - 1), SEPARACION_MAX);
        double desplazamiento = n.getColumna() - (enFila - 1) / 2.0;
        int x = getWidth() / 2 + (int) Math.round(desplazamiento * separacion);
        int y = getHeight() - MARGEN - (int) ((double) n.getFila() / (totalFilas - 1) * alto);
        return new Point(x, y);
    }

    private Nodo nodoEn(Point p) {
        for (List<Nodo> fila : mapa.getFilas())
            for (Nodo n : fila)
                if (posicion(n).distance(p) <= RADIO) return n;
        return null;
    }

    // ---- que pieza de cada sheet le corresponde a cada nodo segun su estado ----

    private String estadoBase(Nodo n, Nodo actual) {
        if (n == actual) return "actual";
        if (n.getVisitado()) return "completado";
        if (mapa.puedeMoverA(n)) return "accesible";
        return "normal";
    }

    // null = no lleva indicador encima (el borde de la base ya alcanza)
    private String indicador(Nodo n, Nodo actual) {
        if (n == actual) return "actual";
        if (n.getVisitado()) return "completado";
        if (mapa.puedeMoverA(n)) return null;
        return "candado";
    }

    private String piezaIcono(TipoNodo t) {
        switch (t) {
            case Batalla:      return "combate";
            case BatallaElite: return "elite";
            case Fogata:       return "descanso";
            case Herrero:      return "mercader";
            case Evento:       return "evento";
            case Tesoro:       return "tesoro";
            case INICIO:       return "santuario";
            default:           return null;   // BOSS usa la puerta aparte
        }
    }

    private String estadoConexion(Nodo n, Nodo s, Nodo actual) {
        if (n.getVisitado() && s.getVisitado()) return "recorrido_horizontal";
        if (n == actual) return "disponible_horizontal";   // sale del nodo donde estoy parado: es una opcion directa
        return "pendiente_horizontal";
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        // La franja de piedra (centrada) donde van los nodos. El fondo del castillo va detrás,
        // lo dibuja PantallaMapa a pantalla completa (este panel es transparente).
        int anchoSuperficie = anchoSuperficie();
        int xSuperficie = (getWidth() - anchoSuperficie) / 2;
        Rectangle superficie = RecortesSprites.get("superficie_mapa.png", "superficie");
        g.drawImage(SHEET_SUPERFICIE, xSuperficie, 0, xSuperficie + anchoSuperficie, getHeight(),
                superficie.x, superficie.y, superficie.x + superficie.width, superficie.y + superficie.height, null);

        Nodo actual = mapa.getNodoActual();

        // Conexiones (van debajo de los nodos)
        for (List<Nodo> fila : mapa.getFilas())
            for (Nodo n : fila)
                for (Nodo s : n.getConecciones())
                    dibujarConexion(g, posicion(n), posicion(s), estadoConexion(n, s, actual));

        // Nodos: base + icono de tipo + indicador de estado (o la puerta, si es el BOSS)
        for (List<Nodo> fila : mapa.getFilas())
            for (Nodo n : fila)
                dibujarNodo(g, n, actual);
    }

    private void dibujarNodo(Graphics2D g, Nodo n, Nodo actual) {
        Point p = posicion(n);

        if (n.getTpe() == TipoNodo.BOSS) {
            String piezaPuerta = n == actual ? "seleccionada" : mapa.puedeMoverA(n) ? "disponible" : "inactiva";
            dibujarRecortadoCentrado(g, SHEET_PUERTA, RecortesSprites.get("sheet_puerta_jefe.png", piezaPuerta),
                    p.x, p.y, TAMANO_BOSS);
            return;
        }

        dibujarRecortadoCentrado(g, SHEET_BASES, RecortesSprites.get("sheet_bases_nodos.png", estadoBase(n, actual)),
                p.x, p.y, TAMANO_NODO);

        String piezaIcono = piezaIcono(n.getTpe());
        if (piezaIcono != null) {
            dibujarRecortadoCentrado(g, SHEET_ICONOS, RecortesSprites.get("sheet_iconos_nodos.png", piezaIcono),
                    p.x, p.y, (int) (TAMANO_NODO * 0.6));
        }

        String piezaIndicador = indicador(n, actual);
        if ("actual".equals(piezaIndicador)) {
            // el diamante que marca "estoy aca" va flotando arriba del nodo, no pisa el icono
            dibujarRecortadoCentrado(g, SHEET_INDICADORES, RecortesSprites.get("sheet_indicadores_nodos.png", piezaIndicador),
                    p.x, p.y - TAMANO_NODO, (int) (TAMANO_NODO * 0.5));
        } else if (piezaIndicador != null) {
            // completado / candado van como insignia chica en la esquina del nodo
            dibujarRecortadoCentrado(g, SHEET_INDICADORES, RecortesSprites.get("sheet_indicadores_nodos.png", piezaIndicador),
                    p.x + TAMANO_NODO / 3, p.y + TAMANO_NODO / 3, (int) (TAMANO_NODO * 0.45));
        }
    }

    // Dibuja el rectangulo "r" recortado de "sheet", centrado en (cx,cy), escalado para entrar
    // en un cuadrado de cajaMax x cajaMax sin deformarse (conserva la proporcion del recorte).
    private void dibujarRecortadoCentrado(Graphics2D g, BufferedImage sheet, Rectangle r, int cx, int cy, int cajaMax) {
        double escala = Math.min((double) cajaMax / r.width, (double) cajaMax / r.height);
        int ancho = (int) Math.round(r.width * escala);
        int alto = (int) Math.round(r.height * escala);
        int x1 = cx - ancho / 2;
        int y1 = cy - alto / 2;
        g.drawImage(sheet, x1, y1, x1 + ancho, y1 + alto,
                r.x, r.y, r.x + r.width, r.y + r.height, null);
    }

    // Dibuja una conexion entre dos puntos usando la pieza "horizontal" del color correspondiente,
    // rotada y estirada para que encaje exacto entre "a" y "b" sea cual sea el angulo real entre nodos.
    // (El kit trae piezas dedicadas para 0/90/45/-45 grados, pero como la grilla del mapa no siempre
    // cae justo en esos angulos, es mas prolijo rotar una sola pieza que combinar cuatro.)
    private void dibujarConexion(Graphics2D g, Point a, Point b, String pieza) {
        Rectangle r = RecortesSprites.get("sheet_conexiones.png", pieza);
        double dx = b.x - a.x;
        double dy = b.y - a.y;
        double distancia = Math.hypot(dx, dy);
        double angulo = Math.atan2(dy, dx);

        AffineTransform t = new AffineTransform();
        t.translate(a.x, a.y);
        t.rotate(angulo);
        t.translate(0, -GROSOR_CONEXION / 2.0);
        t.scale(distancia / r.width, GROSOR_CONEXION / (double) r.height);

        g.drawImage(sheet(SHEET_CONEXIONES, r), t, null);
    }

    private BufferedImage sheet(BufferedImage completa, Rectangle r) {
        return completa.getSubimage(r.x, r.y, r.width, r.height);
    }

    private static BufferedImage cargarImagen(String archivo) {
        try {
            return ImageIO.read(new File(DIR_IMAGENES + archivo));
        } catch (Exception e) {
            throw new RuntimeException("No se pudo cargar " + archivo, e);
        }
    }
}
