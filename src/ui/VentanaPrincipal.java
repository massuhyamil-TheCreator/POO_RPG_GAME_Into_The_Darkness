package ui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;
import javax.swing.JLayeredPane;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.Timer;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private PanelFundido panelFundido;
    private JPanel panelContenedor;
    public VentanaPrincipal() {
        setTitle("Into The Darkness");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.add(new PantallaMenu(this), "menu");
        panelContenedor.add(new PantallaPartidas(this), "partidas");
        panelContenedor.setBounds(0, 0, getWidth(), getHeight());

        panelFundido = new PanelFundido();
        panelFundido.setBounds(0, 0, getWidth(), getHeight());

        JLayeredPane capas = getLayeredPane();
        capas.add(panelContenedor, JLayeredPane.DEFAULT_LAYER);
        capas.add(panelFundido, JLayeredPane.PALETTE_LAYER);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                panelContenedor.setBounds(0, 0, getWidth(), getHeight());
                panelContenedor.revalidate();
                panelContenedor.repaint();

                panelFundido.setBounds(0, 0, getWidth(), getHeight());
                panelFundido.repaint();
            }
        });
        setVisible(true);
    }

    public void mostrarPantalla(String nombre) {
        cardLayout.show(panelContenedor, nombre);
    }

    public void cambiarPantallaConFundido(String nombrePantalla) {
        animarFundido(true, () -> {
            mostrarPantalla(nombrePantalla);
            animarFundido(false, null);
        });
    }

    private void animarFundido(boolean oscureciendo, Runnable alTerminar) {
        Timer temporizador = new Timer(20, null);
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