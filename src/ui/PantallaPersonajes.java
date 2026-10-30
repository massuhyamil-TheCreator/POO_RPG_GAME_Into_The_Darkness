package ui;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import combate.PartyAliada;
import entidades.efectos.Efecto;
import entidades.habilidades.Habilidad;
import entidades.personajes.Personaje;
import partida.CreadorDePersonajes;
import partida.GuardadoDePartidas;
import partida.Partida;

// Partida nueva: se eligen los 3 personajes. Pasando el mouse por una clase se ven sus datos;
// tocándola se suma a la party, y tocando un personaje de la party se lo saca.
public class PantallaPersonajes extends JPanel {
    private static final int TAMANIO_PARTY = PartyAliada.TAMANIO_MAXIMO;
    private static final int ANCHO_CONTENIDO = 1180;
    private static final int ANCHO_CLASE = 230;
    private static final int ALTO_CLASE = 56;
    private static final int ANCHO_TARJETA = 400;
    private static final int ALTO_TARJETA = 100;

    private VentanaPrincipal ventana;
    private int ranura;
    private Image fondo;
    private List<String> elegidas = new ArrayList<>();

    private JLabel titulo;
    private List<JButton> botonesClase = new ArrayList<>();
    private JPanel cajaDetalle;
    private JLabel tituloDetalle;
    private JTextArea detalle;
    private JLabel tituloParty;
    private TarjetaPersonaje[] tarjetas = new TarjetaPersonaje[TAMANIO_PARTY];
    private JButton botonVolver;
    private JButton botonComenzar;

    public PantallaPersonajes(VentanaPrincipal ventana, int ranura) {
        this.ventana = ventana;
        this.ranura = ranura;
        fondo = new ImageIcon("src/ui/imagenes/Into_The_Darkness_PlayMenu.png").getImage();
        setLayout(null);

        titulo = new JLabel("Ranura " + ranura + "  ·  Elegí " + TAMANIO_PARTY + " personajes", SwingConstants.CENTER);
        titulo.setFont(Recursos.fuente(40f));
        titulo.setForeground(Recursos.DORADO);
        add(titulo);

        for (String clase : CreadorDePersonajes.CLASES) {
            JButton boton = Recursos.botonPlaca(clase, ANCHO_CLASE, ALTO_CLASE, 22f);
            boton.addActionListener(e -> agregar(clase));
            boton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    mostrarDetalle(clase);
                }
            });
            botonesClase.add(boton);
            add(boton);
        }

        tituloDetalle = new JLabel("", SwingConstants.LEFT);
        tituloDetalle.setFont(Recursos.fuente(32f));
        tituloDetalle.setForeground(Recursos.DORADO);
        detalle = new JTextArea();
        detalle.setEditable(false);
        detalle.setFocusable(false);
        detalle.setOpaque(false);
        detalle.setLineWrap(true);
        detalle.setWrapStyleWord(true);
        detalle.setFont(Recursos.fuente(20f));
        detalle.setForeground(Color.WHITE);
        cajaDetalle = new JPanel(new BorderLayout(0, 8)) {
            @Override
            protected void paintComponent(Graphics g) {
                // recuadro oscuro semitransparente con borde dorado
                g.setColor(new Color(10, 11, 15, 215));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Recursos.DORADO);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        cajaDetalle.setOpaque(false);
        cajaDetalle.setBorder(BorderFactory.createEmptyBorder(14, 22, 14, 22));
        cajaDetalle.add(tituloDetalle, BorderLayout.NORTH);
        cajaDetalle.add(detalle, BorderLayout.CENTER);
        add(cajaDetalle);

        tituloParty = new JLabel("", SwingConstants.CENTER);
        tituloParty.setFont(Recursos.fuente(28f));
        tituloParty.setForeground(Color.WHITE);
        add(tituloParty);

        Image[] conRetrato = Recursos.placaConRetrato(ANCHO_TARJETA, ALTO_TARJETA);
        Image[] lisa = Recursos.placaLisa(ANCHO_TARJETA, ALTO_TARJETA);
        for (int i = 0; i < TAMANIO_PARTY; i++) {
            tarjetas[i] = new TarjetaPersonaje(i, conRetrato, lisa);
            add(tarjetas[i]);
        }

        botonVolver = Recursos.botonPlaca("Volver", 220, 60, 22f);
        botonVolver.addActionListener(e -> ventana.cambiarPantallaConFundido("partidas"));
        add(botonVolver);

        botonComenzar = Recursos.botonPlaca("Comenzar", 220, 60, 24f);
        botonComenzar.addActionListener(e -> comenzar());
        add(botonComenzar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionar();
            }
        });

        mostrarDetalle(CreadorDePersonajes.CLASES.get(0));
        actualizarParty();
    }

    private void agregar(String clase) {
        if (elegidas.size() >= TAMANIO_PARTY) {
            Dialogo.avisar(this, "Party completa", "Ya elegiste " + TAMANIO_PARTY
                    + " personajes. Tocá uno de la derecha para sacarlo y elegir otro.");
            return;
        }
        elegidas.add(clase);
        actualizarParty();
    }

    private void quitar(int indice) {
        if (indice >= elegidas.size()) return;
        elegidas.remove(indice);
        actualizarParty();
    }

    private void actualizarParty() {
        // se crean como en la partida, así se ven los nombres repetidos ("Mago 2")
        List<Personaje> personajes = CreadorDePersonajes.crearParty(elegidas);
        for (int i = 0; i < TAMANIO_PARTY; i++) {
            tarjetas[i].mostrar(i < personajes.size() ? personajes.get(i) : null);
        }
        boolean completa = elegidas.size() == TAMANIO_PARTY;
        tituloParty.setText(completa ? "Tu party está lista" : "Tu party (" + elegidas.size() + "/" + TAMANIO_PARTY + ")");
        repaint();
    }

    private void mostrarDetalle(String clase) {
        Personaje p = CreadorDePersonajes.crear(clase, clase);
        StringBuilder texto = new StringBuilder();
        texto.append("Vida ").append(p.getPuntosDeSaludMaximos()).append("   ·   Maná ").append(p.getRecursoMaximo()).append('\n');
        texto.append("Ataque físico ").append(p.getAtaqueFisico()).append("   ·   mágico ").append(p.getAtaqueMagico()).append('\n');
        texto.append("Defensa física ").append(p.getDefensaFisica()).append("   ·   mágica ").append(p.getDefensaMagica()).append('\n');
        texto.append("Velocidad ").append(p.getVelocidad()).append("   ·   Precisión ").append(p.getPrecision()).append('\n');
        texto.append("Mochila: ").append(p.getCapacidadInventario()).append(" espacios\n\n");
        for (Efecto efecto : p.getEfectos()) {
            if (efecto.esPasiva()) texto.append("Pasiva: ").append(efecto.getNombre()).append('\n');
        }
        texto.append("Ataque básico: ").append(p.getBasico().getNombre()).append('\n');
        texto.append("Habilidades:");
        for (Habilidad habilidad : p.getHabilidades()) {
            texto.append("\n   •  ").append(habilidad);
        }
        tituloDetalle.setText(clase);
        detalle.setText(texto.toString());
    }

    private void comenzar() {
        if (elegidas.size() != TAMANIO_PARTY) {
            Dialogo.avisar(this, "Faltan personajes", "Elegí " + (TAMANIO_PARTY - elegidas.size())
                    + " más para completar la party.");
            return;
        }
        Partida partida = new Partida(CreadorDePersonajes.crearParty(elegidas));
        try {
            GuardadoDePartidas.guardar(partida, ranura);
        } catch (IOException e) {
            Dialogo.avisar(this, "No se pudo guardar", e.getMessage());
            return;
        }
        ventana.jugar(partida, ranura);

    }

    private void posicionar() {
        int x = Math.max(20, (getWidth() - ANCHO_CONTENIDO) / 2);
        titulo.setBounds(0, 28, getWidth(), 56);

        int y = 110;
        for (JButton boton : botonesClase) {
            boton.setBounds(x, y, ANCHO_CLASE, ALTO_CLASE);
            y += ALTO_CLASE + 6;
        }

        cajaDetalle.setBounds(x + 255, 110, 500, 428);

        int xParty = x + ANCHO_CONTENIDO - ANCHO_TARJETA;
        tituloParty.setBounds(xParty, 110, ANCHO_TARJETA, 36);
        for (int i = 0; i < TAMANIO_PARTY; i++) {
            tarjetas[i].setBounds(xParty, 156 + i * (ALTO_TARJETA + 12), ANCHO_TARJETA, ALTO_TARJETA);
        }

        int yAbajo = getHeight() - 90;
        botonVolver.setBounds(x, yAbajo, 220, 60);
        botonComenzar.setBounds(xParty + (ANCHO_TARJETA - 220) / 2, yAbajo, 220, 60);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        // un poco más oscuro que la pantalla de ranuras, para que se lea el texto
        g.setColor(new Color(0, 0, 0, 110));
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    // Un lugar de la party: con personaje muestra sus iniciales y datos; vacío dice "Vacío".
    private class TarjetaPersonaje extends JButton {
        private final Image[] imagenesConRetrato;
        private final Image[] imagenesLisa;
        private Personaje personaje;

        TarjetaPersonaje(int indice, Image[] imagenesConRetrato, Image[] imagenesLisa) {
            this.imagenesConRetrato = imagenesConRetrato;
            this.imagenesLisa = imagenesLisa;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setRolloverEnabled(true);
            addActionListener(e -> quitar(indice));
        }

        void mostrar(Personaje personaje) {
            this.personaje = personaje;
            setToolTipText(personaje == null ? null : "Clic para sacarlo de la party");
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g0) {
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            int ancho = getWidth();
            int alto = getHeight();
            ButtonModel modelo = getModel();
            int estado = personaje != null && modelo.isPressed() ? 2 : personaje != null && modelo.isRollover() ? 1 : 0;

            if (personaje == null) {
                if (imagenesLisa != null) g.drawImage(imagenesLisa[0], 0, 0, ancho, alto, this);
                g.setFont(Recursos.fuente(22f));
                g.setColor(Recursos.TEXTO_SECUNDARIO);
                centrar(g, "Vacío", ancho / 2, alto / 2);
            } else {
                // el cuadradito de la imagen es transparente: se pinta oscuro para que se lea
                g.setColor(new Color(12, 13, 17));
                g.fillRect((int) (ancho * 0.075), (int) (alto * 0.17), (int) (ancho * 0.197), (int) (alto * 0.665));
                if (imagenesConRetrato != null) g.drawImage(imagenesConRetrato[estado], 0, 0, ancho, alto, this);
                g.setColor(Color.WHITE);
                g.setFont(Recursos.fuente(30f));
                centrar(g, Recursos.iniciales(personaje.getNombre()), (int) (ancho * 0.172), alto / 2);
                int x = (int) (ancho * 0.31);
                g.setFont(Recursos.fuente(24f));
                g.drawString(personaje.getNombre(), x, (int) (alto * 0.45));
                g.setFont(Recursos.fuente(16f));
                g.setColor(Recursos.TEXTO_SECUNDARIO);
                g.drawString("Vida " + personaje.getPuntosDeSaludMaximos() + "  ·  Mochila " + personaje.getCapacidadInventario()
                        + "  ·  clic para sacar", x, (int) (alto * 0.70));
            }
            g.dispose();
        }

        private void centrar(Graphics2D g, String texto, int centroX, int centroY) {
            FontMetrics fm = g.getFontMetrics();
            g.drawString(texto, centroX - fm.stringWidth(texto) / 2, centroY + (fm.getAscent() - fm.getDescent()) / 2);
        }
    }
}
