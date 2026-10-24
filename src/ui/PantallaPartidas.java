package ui;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;

public class PantallaPartidas extends JPanel {
    private Image fondo;

    public PantallaPartidas() {
        ImageIcon icon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_PlayMenu.png");
        fondo = icon.getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}