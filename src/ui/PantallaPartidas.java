package ui;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class PantallaPartidas extends JPanel {
    private Image fondo;
    private Font fuenteTarjetas;
    private Image panelPartidaNormal;
    private Image panelPartidaSeleccionado;
    private Image panelPartidaPresionado;
    private Image panelNuevaNormal;
    private Image panelNuevaSeleccionado;
    private Image panelNuevaPresionado;
    private Image botonVolverNormal;
    private Image botonVolverSeleccionado;
    private Image botonVolverPresionado;
    private VentanaPrincipal ventana;

    private JButton botonSlot1;
    private JButton botonSlot2;
    private JButton botonSlot3;
    private JButton botonVolver;

    private int anchoSlot = 700;
    private int altoSlot = 160;
    private int anchoVolver = 220;
    private int altoVolver = 50;

    public PantallaPartidas(VentanaPrincipal ventana) {
        this.ventana = ventana;

        ImageIcon icon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_PlayMenu.png");
        fondo = icon.getImage();

        try {
            fuenteTarjetas = Font.createFont(Font.TRUETYPE_FONT, new File("src/ui/fuentes/CormorantGaramond-SemiBold.ttf"));
        } catch (IOException | FontFormatException e) {
            System.out.println("Error al cargar la fuente: " + e.getMessage());
        }

        BufferedImage hojaPaneles = null;
        try {
            hojaPaneles = ImageIO.read(new File("src/ui/imagenes/Into_the_Darkness_ArtePantallaDePartidas.png"));
        } catch (IOException e) {
            System.out.println("Error al leer la imagen de paneles: " + e.getMessage());
        }

        panelPartidaNormal = hojaPaneles.getSubimage(20, 115, 648, 190);
        panelPartidaSeleccionado = hojaPaneles.getSubimage(690, 115, 646, 190);
        panelPartidaPresionado = hojaPaneles.getSubimage(1359, 115, 647, 190);

        panelNuevaNormal = hojaPaneles.getSubimage(20, 343, 648, 188);
        panelNuevaSeleccionado = hojaPaneles.getSubimage(690, 343, 646, 188);
        panelNuevaPresionado = hojaPaneles.getSubimage(1359, 343, 647, 188);

        botonVolverNormal = hojaPaneles.getSubimage(20, 582, 648, 111);
        botonVolverSeleccionado = hojaPaneles.getSubimage(690, 582, 646, 111);
        botonVolverPresionado = hojaPaneles.getSubimage(1359, 582, 647, 111);

        setLayout(null);


        Image nuevaNormalEscalado = panelNuevaNormal.getScaledInstance(anchoSlot, altoSlot, Image.SCALE_SMOOTH);
        Image nuevaSeleccionadoEscalado = panelNuevaSeleccionado.getScaledInstance(anchoSlot, altoSlot, Image.SCALE_SMOOTH);
        Image nuevaPresionadoEscalado = panelNuevaPresionado.getScaledInstance(anchoSlot, altoSlot, Image.SCALE_SMOOTH);

        botonSlot1 = crearBotonSlot(nuevaNormalEscalado, nuevaSeleccionadoEscalado, nuevaPresionadoEscalado);
        botonSlot2 = crearBotonSlot(nuevaNormalEscalado, nuevaSeleccionadoEscalado, nuevaPresionadoEscalado);
        botonSlot3 = crearBotonSlot(nuevaNormalEscalado, nuevaSeleccionadoEscalado, nuevaPresionadoEscalado);

        add(botonSlot1);
        add(botonSlot2);
        add(botonSlot3);

        Image volverNormalEscalado = botonVolverNormal.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH);
        Image volverSeleccionadoEscalado = botonVolverSeleccionado.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH);
        Image volverPresionadoEscalado = botonVolverPresionado.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH);

        botonVolver = new JButton("Volver");
        botonVolver.setIcon(new ImageIcon(volverNormalEscalado));
        botonVolver.setRolloverIcon(new ImageIcon(volverSeleccionadoEscalado));
        botonVolver.setPressedIcon(new ImageIcon(volverPresionadoEscalado));
        botonVolver.setContentAreaFilled(false);
        botonVolver.setBorderPainted(false);
        botonVolver.setFocusPainted(false);
        botonVolver.setFont(fuenteTarjetas.deriveFont(20f));
        botonVolver.setForeground(java.awt.Color.WHITE);
        botonVolver.setHorizontalTextPosition(SwingConstants.CENTER);
        botonVolver.setVerticalTextPosition(SwingConstants.CENTER);
        botonVolver.addActionListener(e -> ventana.cambiarPantallaConFundido("menu"));
        add(botonVolver);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarBotones();
            }
        });
    }

    private JButton crearBotonSlot(Image normal, Image seleccionado, Image presionado) {
        JButton boton = new JButton("Partida Nueva");
        boton.setIcon(new ImageIcon(normal));
        boton.setRolloverIcon(new ImageIcon(seleccionado));
        boton.setPressedIcon(new ImageIcon(presionado));
        boton.setContentAreaFilled(false);
        boton.setBorderPainted(false);
        boton.setFocusPainted(false);

        boton.setFont(fuenteTarjetas.deriveFont(26f));
        boton.setForeground(java.awt.Color.WHITE);
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.CENTER);

        return boton;
    }
        private void posicionarBotones () {
            int espacio = 30;
            int xSlot = (getWidth() / 2) - (anchoSlot / 2);

            int ySlot1 = (int) (getHeight() * 0.15);
            int ySlot2 = ySlot1 + altoSlot + espacio;
            int ySlot3 = ySlot2 + altoSlot + espacio;

            botonSlot1.setBounds(xSlot, ySlot1, anchoSlot, altoSlot);
            botonSlot2.setBounds(xSlot, ySlot2, anchoSlot, altoSlot);
            botonSlot3.setBounds(xSlot, ySlot3, anchoSlot, altoSlot);

            int xVolver = (int)(getWidth() * 0.05);
            int yVolver = (int)(getHeight() * 0.85);
            botonVolver.setBounds(xVolver, yVolver, anchoVolver, altoVolver);

        }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}