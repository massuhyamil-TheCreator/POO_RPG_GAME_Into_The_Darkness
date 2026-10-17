package ui;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import java.awt.Graphics;

public class PantallaMenu extends JPanel {
    private Image fondo;
    public PantallaMenu() {
        ImageIcon icon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_MainTitle.png");
        System.out.println("Ancho de la imagen: " + icon.getIconWidth());
        fondo = icon.getImage();
        setLayout(null);

        JButton botonJugar = new JButton("Jugar");
        botonJugar.setBounds(40, 420, 220, 50);
        add(botonJugar);

        JButton botonOpciones = new JButton("Opciones");
        botonOpciones.setBounds(40, 480, 220, 50);
        add(botonOpciones);

        JButton botonSalir = new JButton("Salir");
        botonSalir.setBounds(40, 540, 220, 50);
        botonSalir.addActionListener(e -> System.exit(0));
        add(botonSalir);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}