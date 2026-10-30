package ui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JLayeredPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Timer;
import partida.Partida;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private PanelFundido panelFundido;
    private JPanel panelContenedor;
    private PantallaPartidas pantallaPartidas;
    private JPanel pantallaPersonajes;
    private PantallaMapa pantallaMapa;
    private JPanel pantallaPreparacion;
    private JPanel pantallaCombate;
    private JPanel pantallaFinal;
    private Timer fundidoEnCurso;
    public VentanaPrincipal() {
        setTitle("Into The Darkness");
        setSize(1280, 720);
        // si la pantalla es más chica que la ventana, se abre maximizada
        Rectangle pantalla = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        if (pantalla.width < getWidth() || pantalla.height < getHeight()) setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1024, 680));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        pantallaPartidas = new PantallaPartidas(this);
        panelContenedor.add(new PantallaMenu(this), "menu");
        panelContenedor.add(pantallaPartidas, "partidas");
        panelContenedor.setBounds(0, 0, getWidth(), getHeight());

        panelFundido = new PanelFundido();
        panelFundido.setBounds(0, 0, getWidth(), getHeight());

        JLayeredPane capas = getLayeredPane();
        capas.add(panelContenedor, JLayeredPane.DEFAULT_LAYER);
        capas.add(panelFundido, JLayeredPane.PALETTE_LAYER);
        // se usa el tamaño de adentro de la ventana (sin la barra de título ni los bordes),
        // si no, lo de abajo y lo de la derecha queda tapado
        capas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelContenedor.setBounds(0, 0, capas.getWidth(), capas.getHeight());
                panelContenedor.revalidate();
                panelContenedor.repaint();

                panelFundido.setBounds(0, 0, capas.getWidth(), capas.getHeight());
                panelFundido.repaint();
            }
        });
        setVisible(true);
    }

    public void mostrarPantalla(String nombre) {
        // las ranuras se vuelven a leer cada vez que se entra, por si se guardó o borró una partida
        if (nombre.equals("partidas")) pantallaPartidas.refrescar();
        cardLayout.show(panelContenedor, nombre);
    }

    // partida nueva en una ranura vacía: la pantalla se arma de cero cada vez
    public void elegirPersonajes(int ranura) {
        // (merge) se usa la pantalla con splash arts e iconos; PantallaPersonajes (la versión simple)
        // queda en el proyecto por si hace falta volver a ella
        pantallaPersonajes = reemplazar(pantallaPersonajes, new PantallaSeleccionPersonajes(this, ranura), "personajes");
        cambiarPantallaConFundido("personajes");
    }

    // abre el mapa de una partida nueva o cargada; se guarda en la misma ranura
    public void jugar(Partida partida, int ranura) {
        pantallaMapa = (PantallaMapa) reemplazar(pantallaMapa, new PantallaMapa(this, partida, ranura), "mapa");
        cambiarPantallaConFundido("mapa");
    }

    // antes de cada pelea: elegir arma, armadura y objetos
    public void preparar(Partida partida, int ranura) {
        pantallaPreparacion = reemplazar(pantallaPreparacion, new PantallaPreparacion(this, partida, ranura), "preparacion");
        cambiarPantallaConFundido("preparacion");
    }

    public void combatir(Partida partida, int ranura) {
        pantallaCombate = reemplazar(pantallaCombate, new PantallaCombate(this, partida, ranura), "combate");
        cambiarPantallaConFundido("combate");
    }

    // después de ganar una pelea se vuelve al mismo mapa (con su registro de lo que pasó)
    public void volverAlMapa(String resumen) {
        pantallaMapa.volverDelCombate(resumen);
        cambiarPantallaConFundido("mapa");
    }

    // Victoria o Game Over
    public void finalizar(Partida partida, int ranura) {
        pantallaFinal = reemplazar(pantallaFinal, new PantallaFinal(this, partida, ranura), "final");
        cambiarPantallaConFundido("final");
    }

    // estas pantallas se arman de cero cada vez: se saca la anterior y se agrega la nueva
    private JPanel reemplazar(JPanel anterior, JPanel nueva, String nombre) {
        if (anterior != null) panelContenedor.remove(anterior);
        panelContenedor.add(nueva, nombre);
        return nueva;
    }

    public void cambiarPantallaConFundido(String nombrePantalla) {
        animarFundido(true, () -> {
            mostrarPantalla(nombrePantalla);
            animarFundido(false, null);
        });
    }

    private void animarFundido(boolean oscureciendo, Runnable alTerminar) {
        // si había un fundido a medias se corta: si no, uno oscurece y el otro aclara a la vez y no terminan nunca
        if (fundidoEnCurso != null) fundidoEnCurso.stop();
        Timer temporizador = new Timer(20, null);
        fundidoEnCurso = temporizador;
        temporizador.addActionListener(e -> {
            float alpha = panelFundido.getAlpha();
            if (oscureciendo) {
                alpha += 0.05f;
            } else {
                alpha -= 0.05f;
            }

            boolean termino = oscureciendo ? alpha >= 1f : alpha <= 0f;
            if (termino) {
                alpha = oscureciendo ? 1f : 0f;
                temporizador.stop();
            }

            panelFundido.setAlpha(alpha);

            if (termino && alTerminar != null) {
                alTerminar.run();
            }
        });
        temporizador.start();
    }

    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
