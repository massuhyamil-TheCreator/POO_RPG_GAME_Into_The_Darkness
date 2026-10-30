package ui;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import partida.GuardadoDePartidas;
import partida.Partida;

public class PantallaPartidas extends JPanel {
    private static final String[] NUMEROS_ROMANOS = {"I", "II", "III"};

    private Image fondo;
    private Font fuenteTarjetas;
    private Image panelPartidaNormal;
    private Image panelPartidaSeleccionado;
    private Image panelPartidaPresionado;
    private Image panelNuevaNormal;
    private Image panelNuevaSeleccionado;
    private Image panelNuevaPresionado;
    private Image botonVolverNormal;
    private Image botonVolverSeleccionado;
    private Image botonVolverPresionado;
    private VentanaPrincipal ventana;

    private BotonRanura botonSlot1;
    private BotonRanura botonSlot2;
    private BotonRanura botonSlot3;
    private JButton botonVolver;

    private int anchoSlot = 700;
    private int altoSlot = 160;
    private int anchoVolver = 220;
    private int altoVolver = 50;

    public PantallaPartidas(VentanaPrincipal ventana) {
        this.ventana = ventana;

        ImageIcon icon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_PlayMenu.png");
        fondo = icon.getImage();

        // Si la fuente opcional no está instalada en el proyecto, se usa una del sistema.
        fuenteTarjetas = new Font("Serif", Font.PLAIN, 24);
        try {
            fuenteTarjetas = Font.createFont(Font.TRUETYPE_FONT,
                    new File("src/ui/fuentes/CormorantGaramond-SemiBold.ttf"));
        } catch (IOException | FontFormatException e) {
            // Se mantiene la fuente alternativa; la pantalla puede seguir funcionando.
        }

        BufferedImage hojaPaneles = null;
        try {
            hojaPaneles = ImageIO.read(new File("src/ui/imagenes/Into_the_Darkness_ArtePantallaDePartidas.png"));
        } catch (IOException e) {
            System.out.println("Error al leer la imagen de paneles: " + e.getMessage());
        }

        panelPartidaNormal = hojaPaneles.getSubimage(20, 115, 648, 190);
        panelPartidaSeleccionado = hojaPaneles.getSubimage(690, 115, 646, 190);
        panelPartidaPresionado = hojaPaneles.getSubimage(1359, 115, 647, 190);

        panelNuevaNormal = hojaPaneles.getSubimage(20, 343, 648, 188);
        panelNuevaSeleccionado = hojaPaneles.getSubimage(690, 343, 646, 188);
        panelNuevaPresionado = hojaPaneles.getSubimage(1359, 343, 647, 188);

        botonVolverNormal = hojaPaneles.getSubimage(20, 582, 648, 111);
        botonVolverSeleccionado = hojaPaneles.getSubimage(690, 582, 646, 111);
        botonVolverPresionado = hojaPaneles.getSubimage(1359, 582, 647, 111);

        setLayout(null);

        // panel con el cuadradito: ranura con partida guardada
        Image[] partidaEscalado = {
                escalar(panelPartidaNormal, anchoSlot, altoSlot),
                escalar(panelPartidaSeleccionado, anchoSlot, altoSlot),
                escalar(panelPartidaPresionado, anchoSlot, altoSlot)};
        // panel liso: ranura vacía
        Image[] nuevaEscalado = {
                escalar(panelNuevaNormal, anchoSlot, altoSlot),
                escalar(panelNuevaSeleccionado, anchoSlot, altoSlot),
                escalar(panelNuevaPresionado, anchoSlot, altoSlot)};

        botonSlot1 = new BotonRanura(1, partidaEscalado, nuevaEscalado);
        botonSlot2 = new BotonRanura(2, partidaEscalado, nuevaEscalado);
        botonSlot3 = new BotonRanura(3, partidaEscalado, nuevaEscalado);

        add(botonSlot1);
        add(botonSlot2);
        add(botonSlot3);

        Image volverNormalEscalado = botonVolverNormal.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH);
        Image volverSeleccionadoEscalado = botonVolverSeleccionado.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH);
        Image volverPresionadoEscalado = botonVolverPresionado.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH);

        botonVolver = new JButton("Volver");
        botonVolver.setIcon(new ImageIcon(volverNormalEscalado));
        botonVolver.setRolloverIcon(new ImageIcon(volverSeleccionadoEscalado));
        botonVolver.setPressedIcon(new ImageIcon(volverPresionadoEscalado));
        botonVolver.setContentAreaFilled(false);
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setFont(fuenteTarjetas.deriveFont(20f));
        botonVolver.setForeground(java.awt.Color.WHITE);
        botonVolver.setHorizontalTextPosition(SwingConstants.CENTER);
        botonVolver.setVerticalTextPosition(SwingConstants.CENTER);
        botonVolver.addActionListener(e -> ventana.cambiarPantallaConFundido("menu"));
        add(botonVolver);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarBotones();
            }
        });

        refrescar();
    }

    // vuelve a leer las 3 ranuras (se llama cada vez que se entra a esta pantalla)
    public void refrescar() {
        botonSlot1.actualizar();
        botonSlot2.actualizar();
        botonSlot3.actualizar();
        repaint();
    }

    // ImageIcon espera a que la imagen achicada esté lista antes de usarla
    private Image escalar(Image imagen, int ancho, int alto) {
        return new ImageIcon(imagen.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)).getImage();
    }

    private void elegirRanura(int ranura) {
        String titulo = "Ranura " + ranura;
        if (!GuardadoDePartidas.hayPartida(ranura)) {
            ventana.elegirPersonajes(ranura);
            return;
        }
        int opcion = Dialogo.elegir(this, titulo, GuardadoDePartidas.describir(ranura), "Continuar", "Borrar", "Volver");
        if (opcion == 0) {
            continuar(ranura);
        } else if (opcion == 1) {
            borrar(ranura);
        }
    }

    private void continuar(int ranura) {
        try {
            Partida partida = GuardadoDePartidas.cargar(ranura);
            ventana.jugar(partida, ranura);

        } catch (IOException | RuntimeException e) {
            Dialogo.avisar(this, "No se pudo cargar", "El archivo de la ranura " + ranura + " está dañado: " + e.getMessage());
        }
    }

    private void borrar(int ranura) {
        int confirmar = Dialogo.elegir(this, "Borrar partida",
                "¿Seguro que querés borrar la partida de la ranura " + ranura + "? No se puede deshacer.", "Borrar", "Cancelar");
        if (confirmar != 0) return;
        try {
            GuardadoDePartidas.borrar(ranura);
        } catch (IOException e) {
            Dialogo.avisar(this, "No se pudo borrar", e.getMessage());
        }
        refrescar();
    }

    // Un panel de ranura: con partida muestra el número en el cuadradito y el resumen;
    // vacía muestra "Partida nueva".
    private class BotonRanura extends JButton {
        private final int ranura;
        private final Image[] imagenesPartida;
        private final Image[] imagenesNueva;
        private boolean ocupada;
        private String grupo = "";
        private String detalle = "";

        BotonRanura(int ranura, Image[] imagenesPartida, Image[] imagenesNueva) {
            this.ranura = ranura;
            this.imagenesPartida = imagenesPartida;
            this.imagenesNueva = imagenesNueva;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            addActionListener(e -> elegirRanura(ranura));
        }

        // se lee del disco acá y no al dibujar, que pasa muchas veces
        void actualizar() {
            ocupada = GuardadoDePartidas.hayPartida(ranura);
            grupo = "";
            detalle = "";
            if (ocupada) {
                // "Mago nv 1, Guerrero nv 1, Curador nv 1 · piso 2 · 29/10/2026 18:40"
                String descripcion = GuardadoDePartidas.describir(ranura);
                int corte = descripcion.indexOf(" · ");
                grupo = corte < 0 ? descripcion : descripcion.substring(0, corte);
                detalle = corte < 0 ? "" : descripcion.substring(corte + 3);
            }
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            ButtonModel modelo = getModel();
            Image[] imagenes = ocupada ? imagenesPartida : imagenesNueva;
            Image imagen = modelo.isPressed() ? imagenes[2] : modelo.isRollover() ? imagenes[1] : imagenes[0];
            int ancho = getWidth();
            int alto = getHeight();
            // el cuadradito de la imagen es transparente: se pinta oscuro para que se lea el número
            if (ocupada) {
                g.setColor(new Color(12, 13, 17));
                g.fillRect((int) (ancho * 0.075), (int) (alto * 0.17), (int) (ancho * 0.197), (int) (alto * 0.665));
            }
            g.drawImage(imagen, 0, 0, ancho, alto, this);

            g.setColor(Color.WHITE);
            if (!ocupada) {
                g.setFont(fuenteTarjetas.deriveFont(28f));
                centrar(g, "Ranura " + ranura + "  ·  Partida nueva", ancho / 2, alto / 2);
            } else {
                // el cuadradito de la imagen ocupa del 7% al 27% del ancho
                g.setFont(fuenteTarjetas.deriveFont(44f));
                centrar(g, NUMEROS_ROMANOS[ranura - 1], (int) (ancho * 0.172), alto / 2);

                int x = (int) (ancho * 0.31);
                int anchoTexto = (int) (ancho * 0.63);
                g.setFont(fuenteTarjetas.deriveFont(28f));
                g.drawString("Ranura " + ranura, x, (int) (alto * 0.37));
                g.setColor(new Color(225, 215, 195));
                g.setFont(fuenteTarjetas.deriveFont(19f));
                g.drawString(recortar(g, grupo, anchoTexto), x, (int) (alto * 0.60));
                g.setColor(new Color(180, 170, 150));
                g.setFont(fuenteTarjetas.deriveFont(17f));
                g.drawString(recortar(g, detalle, anchoTexto), x, (int) (alto * 0.78));
            }
            g.dispose();
        }

        private void centrar(Graphics2D g, String texto, int centroX, int centroY) {
            FontMetrics fm = g.getFontMetrics();
            g.drawString(texto, centroX - fm.stringWidth(texto) / 2, centroY + (fm.getAscent() - fm.getDescent()) / 2);
        }

        // si no entra, se corta con "…"
        private String recortar(Graphics2D g, String texto, int anchoMaximo) {
            FontMetrics fm = g.getFontMetrics();
            if (fm.stringWidth(texto) <= anchoMaximo) return texto;
            while (texto.length() > 1 && fm.stringWidth(texto + "…") > anchoMaximo) {
                texto = texto.substring(0, texto.length() - 1);
            }
            return texto + "…";
        }
    }

        private void posicionarBotones () {
            int espacio = 30;
            int xSlot = (getWidth() / 2) - (anchoSlot / 2);

            int ySlot1 = (int) (getHeight() * 0.15);
            int ySlot2 = ySlot1 + altoSlot + espacio;
            int ySlot3 = ySlot2 + altoSlot + espacio;

            botonSlot1.setBounds(xSlot, ySlot1, anchoSlot, altoSlot);
            botonSlot2.setBounds(xSlot, ySlot2, anchoSlot, altoSlot);
            botonSlot3.setBounds(xSlot, ySlot3, anchoSlot, altoSlot);

            int xVolver = (int)(getWidth() * 0.05);
            int yVolver = (int)(getHeight() * 0.85);
            botonVolver.setBounds(xVolver, yVolver, anchoVolver, altoVolver);

        }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}
