package ui;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import entidades.personajes.Personaje;
import partida.EstadoPartida;
import partida.GuardadoDePartidas;
import partida.Partida;

// Cuando la partida termina: Victoria (cayó el jefe) o Game Over (cayó toda la party).
// Muestra cómo quedó la party; con Game Over se puede volver al último guardado de la ranura.
public class PantallaFinal extends JPanel {
    private static final Color ROJO = new Color(190, 50, 50);

    private Image fondo;
    private JLabel titulo;
    private JTextArea resumen;
    private JPanel cajaResumen;
    private JButton botonMenu;
    private JButton botonCargar;

    public PantallaFinal(VentanaPrincipal ventana, Partida partida, int ranura) {
        boolean ganada = partida.getEstado() == EstadoPartida.GANADA;
        fondo = new ImageIcon(ganada ? "src/ui/imagenes/Into_The_Darkness_MainTitle.png"
                : "src/ui/imagenes/Into_The_Darkness_PlayMenu.png").getImage();
        setLayout(null);

        titulo = new JLabel(ganada ? "¡Victoria!" : "Game Over", SwingConstants.CENTER);
        titulo.setFont(Recursos.fuente(86f));
        titulo.setForeground(ganada ? Recursos.DORADO : ROJO);
        add(titulo);

        StringBuilder texto = new StringBuilder();
        String mensajes = partida.tomarMensajes();
        if (!mensajes.isEmpty()) texto.append(mensajes).append("\n\n");
        texto.append(ganada ? "La party salió de la oscuridad:" : "La party llegó hasta el piso " + partida.getNodoActual().getFila() + ":");
        for (Personaje p : partida.getParty().getMiembrosJugador()) {
            texto.append("\n   •  ").append(p.getNombre()).append("  ·  nivel ").append(p.getNivel());
        }
        texto.append("\n\nOro: ").append(partida.getParty().getOro());
        resumen = Recursos.areaDeTexto(22f);
        resumen.setText(texto.toString());
        cajaResumen = Recursos.caja(null, resumen);
        add(cajaResumen);

        botonMenu = Recursos.botonPlaca("Volver al menú", 240, 62, 22f);
        botonMenu.addActionListener(e -> ventana.cambiarPantallaConFundido("menu"));
        add(botonMenu);

        // con Game Over: volver a jugar desde lo último que se guardó en esta ranura
        botonCargar = Recursos.botonPlaca("Último guardado", 240, 62, 22f);
        botonCargar.addActionListener(e -> {
            try {
                ventana.jugar(GuardadoDePartidas.cargar(ranura), ranura);
            } catch (IOException | RuntimeException error) {
                Dialogo.avisar(this, "No se pudo cargar", error.getMessage());
            }
        });
        botonCargar.setVisible(!ganada && GuardadoDePartidas.hayPartida(ranura));
        add(botonCargar);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionar();
            }
        });
    }

    private void posicionar() {
        int ancho = getWidth();
        int alto = getHeight();
        titulo.setBounds(0, 40, ancho, 110);
        cajaResumen.setBounds((ancho - 620) / 2, 170, 620, alto - 170 - 120);
        int yAbajo = alto - 96;
        if (botonCargar.isVisible()) {
            botonCargar.setBounds(ancho / 2 - 250, yAbajo, 240, 62);
            botonMenu.setBounds(ancho / 2 + 10, yAbajo, 240, 62);
        } else {
            botonMenu.setBounds((ancho - 240) / 2, yAbajo, 240, 62);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        g.setColor(new Color(0, 0, 0, 160));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
