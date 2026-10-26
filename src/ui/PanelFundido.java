package ui;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;

public class PanelFundido extends JPanel {
    private float alpha = 0f;

    public PanelFundido() {
        setOpaque(false);
    }

    public void setAlpha(float alpha) {
        this.alpha = alpha;
        repaint();
    }

    public float getAlpha() {
        return alpha;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(0, 0, 0, (int)(alpha * 255)));
        g2.fillRect(0, 0, getWidth(), getHeight());
    }
}