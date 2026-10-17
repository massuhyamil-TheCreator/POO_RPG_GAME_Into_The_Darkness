package ui;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.CardLayout;

public class VentanaPrincipal extends JFrame {
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    public VentanaPrincipal() {
        setTitle("Into The Darkness");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);
        panelContenedor.add(new PantallaMenu(), "menu");
        add(panelContenedor);
        setVisible(true);
    }
    public static void main(String[] args) {
        new VentanaPrincipal();
    }
}
