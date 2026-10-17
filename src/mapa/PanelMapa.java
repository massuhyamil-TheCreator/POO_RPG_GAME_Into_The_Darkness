package mapa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PanelMapa extends JPanel {
    private static final int RADIO = 20;
    private static final int MARGEN = 40;
    private static final Color DORADO = new Color(255, 200, 60);

    private Mapa mapa;
    private final Runnable alCambiar;

    public PanelMapa(Mapa mapa, Runnable alCambiar) {
        this.mapa = mapa;
        this.alCambiar = alCambiar;
        setPreferredSize(new Dimension(420, 720));
        setBackground(new Color(35, 35, 42));
        setToolTipText("");

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Nodo n = nodoEn(e.getPoint());
                if (n != null && PanelMapa.this.mapa.puedeMoverA(n)) {
                    PanelMapa.this.mapa.moveTo(n);
                    repaint();
                    PanelMapa.this.alCambiar.run();
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

    // Cada nivel se centra horizontalmente, asi el mapa queda simetrico
    private Point posicion(Nodo n) {
        int totalFilas = mapa.getFilas().size();
        int enFila = mapa.getFilas().get(n.getFila()).size();
        int ancho = getWidth() - 2 * MARGEN;
        int alto = getHeight() - 2 * MARGEN;
        double separacion = (double) ancho / (CreadorMapa.MaximoNodosFila - 1);
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

    private Color color(TipoNodo t) {
        switch (t) {
            case Batalla:      return new Color(150, 150, 165);
            case BatallaElite: return new Color(190, 50, 50);
            case BOSS:         return new Color(210, 30, 30);
            case Tienda:       return new Color(220, 180, 40);
            case Evento:       return new Color(150, 80, 200);
            case Fogata:       return new Color(230, 120, 30);
            case Herrero:      return new Color(70, 130, 180);
            default:           return new Color(120, 120, 130);   // INICIO
        }
    }

    private String simbolo(TipoNodo t) {
        switch (t) {
            case Batalla:      return "B";
            case BatallaElite: return "E";
            case BOSS:         return "J";
            case Tienda:       return "$";
            case Evento:       return "?";
            case Fogata:       return "F";
            case Herrero:      return "H";
            default:           return "";
        }
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Nodo actual = mapa.getNodoActual();

        // Conexiones
        for (List<Nodo> fila : mapa.getFilas()) {
            for (Nodo n : fila) {
                Point a = posicion(n);
                for (Nodo s : n.getConecciones()) {
                    Point b = posicion(s);
                    boolean camino = n == actual;
                    g.setColor(camino ? DORADO : new Color(90, 90, 100));
                    g.setStroke(new BasicStroke(camino ? 3f : 2f));
                    g.drawLine(a.x, a.y, b.x, b.y);
                }
            }
        }

        // Nodos
        g.setFont(new Font("SansSerif", Font.BOLD, 15));
        for (List<Nodo> fila : mapa.getFilas()) {
            for (Nodo n : fila) {
                Point p = posicion(n);
                boolean alcanzable = mapa.puedeMoverA(n);
                Color base = color(n.getTpe());

                g.setColor(n.getVisitado() ? base.darker().darker() : base);
                g.fillOval(p.x - RADIO, p.y - RADIO, 2 * RADIO, 2 * RADIO);

                g.setStroke(new BasicStroke(alcanzable || n == actual ? 3.5f : 2f));
                g.setColor(alcanzable ? DORADO : n == actual ? Color.WHITE : new Color(20, 20, 25));
                g.drawOval(p.x - RADIO, p.y - RADIO, 2 * RADIO, 2 * RADIO);

                g.setColor(Color.WHITE);
                String s = simbolo(n.getTpe());
                FontMetrics fm = g.getFontMetrics();
                g.drawString(s, p.x - fm.stringWidth(s) / 2, p.y + (fm.getAscent() - fm.getDescent()) / 2);

                // cruz sobre los nodos ya pasados
                if (n.getVisitado() && n != actual) {
                    g.setColor(new Color(255, 255, 255, 190));
                    g.setStroke(new BasicStroke(2f));
                    int d = RADIO - 6;
                    g.drawLine(p.x - d, p.y - d, p.x + d, p.y + d);
                    g.drawLine(p.x - d, p.y + d, p.x + d, p.y - d);
                }
            }
        }

        // Personaje: flecha celeste sobre el nodo actual
        Point p = posicion(actual);
        int[] xs = {p.x - 8, p.x + 8, p.x};
        int[] ys = {p.y - RADIO - 18, p.y - RADIO - 18, p.y - RADIO - 4};
        g.setColor(new Color(80, 200, 255));
        g.fillPolygon(xs, ys, 3);
        g.setColor(Color.WHITE);
        g.setStroke(new BasicStroke(2f));
        g.drawPolygon(xs, ys, 3);
    }
}
