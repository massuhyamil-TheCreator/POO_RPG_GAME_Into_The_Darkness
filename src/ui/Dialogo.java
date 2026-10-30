package ui;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.event.KeyEvent;

// Ventanita para preguntar o avisar algo con el estilo del juego (en vez de las grises de Java).
// elegir(...) devuelve el número de la opción que se tocó (0, 1, 2...) o -1 si se apretó Esc.
public class Dialogo {

    private static final int ANCHO_TEXTO = 460;
    private static final int ANCHO_BOTON = 196;
    private static final int ALTO_BOTON = 64;

    public static int elegir(Component padre, String titulo, String mensaje, String... opciones) {
        Window ventana = padre instanceof Window ? (Window) padre : SwingUtilities.getWindowAncestor(padre);
        JDialog dialogo = new JDialog(ventana, Dialog.ModalityType.APPLICATION_MODAL);
        dialogo.setUndecorated(true);
        int[] elegida = {-1};

        JPanel panel = new JPanel(new BorderLayout(0, 16)) {
            @Override
            protected void paintComponent(Graphics g0) {
                Graphics2D g = (Graphics2D) g0.create();
                g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g.setColor(Recursos.FONDO_OSCURO);
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(Recursos.DORADO);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                g.drawRect(4, 4, getWidth() - 9, getHeight() - 9);
                g.dispose();
            }
        };
        panel.setBorder(BorderFactory.createEmptyBorder(22, 30, 24, 30));

        JLabel etiquetaTitulo = new JLabel(titulo, SwingConstants.CENTER);
        etiquetaTitulo.setFont(Recursos.fuente(32f));
        etiquetaTitulo.setForeground(Recursos.DORADO);
        panel.add(etiquetaTitulo, BorderLayout.NORTH);

        if (mensaje != null && !mensaje.isEmpty()) {
            JTextArea texto = new JTextArea(mensaje);
            texto.setEditable(false);
            texto.setFocusable(false);
            texto.setLineWrap(true);
            texto.setWrapStyleWord(true);
            texto.setOpaque(false);
            texto.setFont(Recursos.fuente(21f));
            texto.setForeground(Color.WHITE);
            // con un ancho fijo, el área calcula sola cuántas líneas necesita
            texto.setSize(ANCHO_TEXTO, Short.MAX_VALUE);
            texto.setPreferredSize(new Dimension(ANCHO_TEXTO, texto.getPreferredSize().height));
            panel.add(texto, BorderLayout.CENTER);
        }

        int columnas = opciones.length <= 3 ? opciones.length : 2;
        JPanel botones = new JPanel(new GridLayout(0, Math.max(1, columnas), 12, 12));
        botones.setOpaque(false);
        for (int i = 0; i < opciones.length; i++) {
            int numero = i;
            JButton boton = Recursos.botonPlaca(opciones[i], ANCHO_BOTON, ALTO_BOTON, 21f);
            boton.addActionListener(e -> {
                elegida[0] = numero;
                dialogo.dispose();
            });
            botones.add(boton);
        }
        JPanel filaBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        filaBotones.setOpaque(false);
        filaBotones.add(botones);
        panel.add(filaBotones, BorderLayout.SOUTH);

        // Esc cierra sin elegir nada
        dialogo.getRootPane().registerKeyboardAction(e -> dialogo.dispose(),
                KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        dialogo.setContentPane(panel);
        dialogo.pack();
        dialogo.setLocationRelativeTo(ventana);
        dialogo.setVisible(true); // espera acá hasta que se cierre
        return elegida[0];
    }

    public static void avisar(Component padre, String titulo, String mensaje) {
        elegir(padre, titulo, mensaje, "Aceptar");
    }
}
