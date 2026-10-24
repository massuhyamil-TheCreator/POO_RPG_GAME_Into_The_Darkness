package ui;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class PantallaMenu extends JPanel {
    private Image fondo;
    private JButton botonJugar;
    private JButton botonOpciones;
    private JButton botonSalir;
    private int anchoBoton = 160;
    private int altoBoton = 55;

    public PantallaMenu() {
        ImageIcon icon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_MainTitle.png");
        fondo = icon.getImage();
        setLayout(null);

        BufferedImage hoja = null;
        try {
            hoja = ImageIO.read(new File("src/ui/imagenes/Botones_Into_The_Darkness_MainTitle.png"));
        } catch (IOException e) {
            System.out.println("Error al leer la imagen: " + e.getMessage());
        }

        int anchoCelda = hoja.getWidth() / 3;
        int altoCelda = hoja.getHeight() / 3;

        BufferedImage jugarNormalImg = hoja.getSubimage(0 * anchoCelda, 0 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoJugarNormal = new ImageIcon(jugarNormalImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        botonJugar = new JButton();
        botonJugar.setIcon(iconoJugarNormal);
        botonJugar.setContentAreaFilled(false);
        botonJugar.setBorderPainted(false);
        botonJugar.setFocusPainted(false);
        BufferedImage jugarSeleccionadoImg = hoja.getSubimage(1 * anchoCelda, 0 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoJugarSeleccionado = new ImageIcon(jugarSeleccionadoImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        BufferedImage jugarPresionadoImg = hoja.getSubimage(2 * anchoCelda, 0 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoJugarPresionado = new ImageIcon(jugarPresionadoImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        botonJugar.setRolloverIcon(iconoJugarSeleccionado);
        botonJugar.setPressedIcon(iconoJugarPresionado);
        add(botonJugar);

        BufferedImage opcionesNormalImg = hoja.getSubimage(0 * anchoCelda, 1 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoOpcionesNormal = new ImageIcon(opcionesNormalImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        botonOpciones = new JButton();
        botonOpciones.setIcon(iconoOpcionesNormal);
        botonOpciones.setContentAreaFilled(false);
        botonOpciones.setBorderPainted(false);
        botonOpciones.setFocusPainted(false);
        BufferedImage opcionesSeleccionadoImg = hoja.getSubimage(1 * anchoCelda, 1 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoOpcionesSeleccionado = new ImageIcon(opcionesSeleccionadoImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        BufferedImage opcionesPresionadoImg = hoja.getSubimage(2 * anchoCelda, 1 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoOpcionesPresionado = new ImageIcon(opcionesPresionadoImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        botonOpciones.setRolloverIcon(iconoOpcionesSeleccionado);
        botonOpciones.setPressedIcon(iconoOpcionesPresionado);
        add(botonOpciones);

        BufferedImage salirNormalImg = hoja.getSubimage(0 * anchoCelda, 2 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoSalirNormal = new ImageIcon(salirNormalImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        botonSalir = new JButton();
        botonSalir.setIcon(iconoSalirNormal);
        botonSalir.setContentAreaFilled(false);
        botonSalir.setBorderPainted(false);
        botonSalir.setFocusPainted(false);
        botonSalir.addActionListener(e -> System.exit(0));
        BufferedImage salirSeleccionadoImg = hoja.getSubimage(1 * anchoCelda, 2 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoSalirSeleccionado = new ImageIcon(salirSeleccionadoImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        BufferedImage salirPresionadoImg = hoja.getSubimage(2 * anchoCelda, 2 * altoCelda, anchoCelda, altoCelda);
        ImageIcon iconoSalirPresionado = new ImageIcon(salirPresionadoImg.getScaledInstance(anchoBoton, altoBoton, Image.SCALE_SMOOTH));

        botonSalir.setRolloverIcon(iconoSalirSeleccionado);
        botonSalir.setPressedIcon(iconoSalirPresionado);
        add(botonSalir);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarBotones();
            }
        });
    }

    private void posicionarBotones() {
        int centroX = (getWidth() / 2) - (anchoBoton / 2);
        botonJugar.setBounds(centroX, (int)(getHeight() * 0.31), anchoBoton, altoBoton);
        botonOpciones.setBounds(centroX, (int)(getHeight() * 0.42), anchoBoton, altoBoton);
        botonSalir.setBounds(centroX, (int)(getHeight() * 0.53), anchoBoton, altoBoton);    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
    }
}