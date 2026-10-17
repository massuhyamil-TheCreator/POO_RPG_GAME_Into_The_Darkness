package mapa;

import javax.swing.*;
import java.awt.*;

public class VentanaMapa extends JFrame {
    private static final Color FONDO = new Color(28, 28, 34);
    private final PanelMapa panelMapa1;

    private Mapa mapa = new CreadorMapa().generate(System.nanoTime());
    private final PanelMapa panelMapa;
    private final JLabel estado = new JLabel();

    public VentanaMapa() {
        super("RPG - Mapa");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        panelMapa1;

        add(crearPanelEquipo(), BorderLayout.WEST);
        add(panelMapa, BorderLayout.CENTER);
        add(crearPanelInventario(), BorderLayout.EAST);
        add(crearPanelInferior(), BorderLayout.SOUTH);

        actualizarEstado();
        pack();
        setLocationRelativeTo(null);
    }

    private void actualizarEstado() {
        if (mapa.llegoAlBoss()) {
            estado.setText("Llegaste al boss!");
        } else {
            estado.setText("Ubicacion: " + mapa.getNodoActual().getTpe()
                    + " - elegi un nodo conectado (borde dorado)");
        }
    }

    // ---- Paneles laterales: datos de relleno hasta que Party/Personaje tengan getters ----

    private JPanel panelBase(String titulo) {
        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setBackground(FONDO);
        p.setPreferredSize(new Dimension(180, 0));
        p.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        p.add(etiqueta(titulo.toUpperCase(), true));
        p.add(Box.createVerticalStrut(10));
        return p;
    }

    private JLabel etiqueta(String texto, boolean titulo) {
        JLabel l = new JLabel(texto);
        l.setForeground(titulo ? new Color(255, 200, 60) : Color.WHITE);
        l.setFont(new Font("SansSerif", titulo ? Font.BOLD : Font.PLAIN, titulo ? 16 : 14));
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JPanel crearPanelEquipo() {
        JPanel p = panelBase("Equipo");
        for (String nombre : new String[]{"Guerrero", "Arquero", "Mago", "(vacio)"}) {
            p.add(etiqueta(nombre, false));
            JProgressBar vida = new JProgressBar(0, 100);
            vida.setValue(nombre.startsWith("(") ? 0 : 100);
            vida.setForeground(new Color(60, 180, 75));
            vida.setAlignmentX(Component.LEFT_ALIGNMENT);
            vida.setMaximumSize(new Dimension(Integer.MAX_VALUE, 12));
            p.add(vida);
            p.add(Box.createVerticalStrut(12));
        }
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel crearPanelInventario() {
        JPanel p = panelBase("Inventario");
        for (String item : new String[]{"Espada", "Escudo", "Pocion", "Capucha"}) p.add(etiqueta("- " + item, false));
        p.add(Box.createVerticalStrut(20));
        p.add(etiqueta("RECURSOS", true));
        p.add(Box.createVerticalStrut(10));
        p.add(etiqueta("Oro: 275", false));
        p.add(etiqueta("Pociones: 3", false));
        p.add(etiqueta("Llaves: 1", false));
        p.add(Box.createVerticalGlue());
        return p;
    }

    private JPanel crearPanelInferior() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(FONDO);
        p.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));
        estado.setForeground(Color.WHITE);
        JButton nuevo = new JButton("Nuevo mapa");
        nuevo.addActionListener(e -> {
            mapa = new CreadorMapa().generate(System.nanoTime());
            panelMapa.setMapa(mapa);
            actualizarEstado();
        });
        p.add(estado, BorderLayout.CENTER);
        p.add(nuevo, BorderLayout.EAST);
        return p;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VentanaMapa().setVisible(true));
    }
}
