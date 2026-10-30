package ui;
import javax.swing.ButtonModel;
import javax.swing.JButton;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import entidades.Entidad;

// El panel con retrato de un personaje o enemigo: iniciales en el cuadradito, nombre y nivel,
// barra de vida, barra de maná y una línea de texto abajo (experiencia, efectos, etc.).
// La usan el mapa y el combate.
public class TarjetaEntidad extends JButton {
    private static final Color VIDA = new Color(176, 44, 44);
    private static final Color MANA = new Color(60, 110, 205);

    private final Image[] imagenes;
    private Entidad entidad;
    private String linea = "";
    private boolean resaltada;

    public TarjetaEntidad(int ancho, int alto) {
        imagenes = Recursos.placaConRetrato(ancho, alto);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        setRolloverEnabled(true);
    }

    public void mostrar(Entidad entidad, String linea) {
        this.entidad = entidad;
        this.linea = linea == null ? "" : linea;
        repaint();
    }

    // dorada: la de quien tiene el turno, o las que se pueden elegir como objetivo
    public void setResaltada(boolean resaltada) {
        this.resaltada = resaltada;
        repaint();
    }

    public Entidad getEntidad() {
        return entidad;
    }

    @Override
    protected void paintComponent(Graphics g0) {
        if (entidad == null) return;
        Graphics2D g = (Graphics2D) g0.create();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int ancho = getWidth();
        int alto = getHeight();
        ButtonModel modelo = getModel();
        int estado = resaltada ? 1 : 0;
        if (resaltada && modelo.isRollover()) estado = modelo.isPressed() ? 2 : 1;

        // los caídos se ven apagados
        if (!entidad.estaVivo()) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.45f));

        // el cuadradito de la imagen es transparente: se pinta oscuro para que se lean las iniciales
        int xCuadro = (int) (ancho * 0.075);
        int yCuadro = (int) (alto * 0.17);
        int anchoCuadro = (int) (ancho * 0.197);
        int altoCuadro = (int) (alto * 0.665);
        g.setColor(new Color(12, 13, 17));
        g.fillRect(xCuadro, yCuadro, anchoCuadro, altoCuadro);
        // (merge) si la clase tiene icono va en el cuadradito; si no (enemigos, invocaciones), las iniciales
        Image icono = Recursos.iconoDeClase(entidad);
        if (icono != null) {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            int lado = Math.min(anchoCuadro, altoCuadro);
            g.drawImage(icono, xCuadro + (anchoCuadro - lado) / 2, yCuadro + (altoCuadro - lado) / 2, lado, lado, this);
        }
        if (imagenes != null) g.drawImage(imagenes[estado], 0, 0, ancho, alto, this);

        g.setColor(Color.WHITE);
        if (icono == null) {
            g.setFont(Recursos.fuente(alto * 0.30f));
            centrar(g, Recursos.iniciales(entidad.getNombre()), (int) (ancho * 0.172), alto / 2);
        }

        int x = (int) (ancho * 0.31);
        int anchoTexto = (int) (ancho * 0.62);
        g.setFont(Recursos.fuente(alto * 0.21f));
        g.drawString(recortar(g, entidad.getNombre() + "  ·  nv " + entidad.getNivel(), anchoTexto), x, (int) (alto * 0.36));

        int altoBarra = Math.max(6, (int) (alto * 0.11));
        int yVida = (int) (alto * 0.44);
        barra(g, x, yVida, anchoTexto, altoBarra, entidad.getPuntosDeSalud(), entidad.getPuntosDeSaludMaximos(), VIDA,
                entidad.estaVivo() ? entidad.getPuntosDeSalud() + " / " + entidad.getPuntosDeSaludMaximos() : "Caído");
        int yAbajo = yVida + altoBarra + 4;
        if (entidad.getRecursoMaximo() > 0) {
            barra(g, x, yAbajo, anchoTexto, altoBarra - 2, entidad.getRecurso(), entidad.getRecursoMaximo(), MANA, null);
            yAbajo += altoBarra + 2;
        }
        if (!linea.isEmpty()) {
            g.setColor(Recursos.TEXTO_SECUNDARIO);
            g.setFont(Recursos.fuente(alto * 0.15f));
            g.drawString(recortar(g, linea, anchoTexto), x, Math.min(alto - (int) (alto * 0.12), yAbajo + g.getFontMetrics().getAscent()));
        }
        g.dispose();
    }

    private void barra(Graphics2D g, int x, int y, int ancho, int alto, int valor, int maximo, Color color, String texto) {
        g.setColor(new Color(0, 0, 0, 170));
        g.fillRect(x, y, ancho, alto);
        int lleno = maximo <= 0 ? 0 : (int) Math.round((double) ancho * Math.max(0, valor) / maximo);
        g.setColor(color);
        g.fillRect(x, y, Math.min(ancho, lleno), alto);
        g.setColor(new Color(255, 255, 255, 70));
        g.drawRect(x, y, ancho, alto);
        if (texto != null) {
            g.setColor(Color.WHITE);
            g.setFont(Recursos.fuente(alto * 1.05f));
            centrar(g, texto, x + ancho / 2, y + alto / 2);
        }
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
