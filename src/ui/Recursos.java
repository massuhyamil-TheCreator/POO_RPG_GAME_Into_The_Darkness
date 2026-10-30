package ui;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.HashMap;
import java.util.Map;
import entidades.Entidad;

// La fuente y los recortes de la hoja de paneles que usan varias pantallas.
// Se cargan una sola vez, la primera vez que alguien los pide.
public class Recursos {

    public static final Color DORADO = new Color(226, 178, 84);
    public static final Color FONDO_OSCURO = new Color(16, 17, 22);
    public static final Color TEXTO_SECUNDARIO = new Color(190, 180, 160);

    private static Font fuente;
    private static BufferedImage hojaPaneles;
    private static boolean cargado;

    public static Font fuente(float tamanio) {
        cargar();
        return fuente.deriveFont(tamanio);
    }

    // los tres estados de cada recorte: normal, seleccionado y presionado
    public static Image[] placaChica(int ancho, int alto) {
        return recortar(new int[]{168, 837, 1507}, 580, 350, 114, ancho, alto);
    }

    public static Image[] placaConRetrato(int ancho, int alto) {
        return recortar(new int[]{20, 690, 1359}, 115, 646, 190, ancho, alto);
    }

    public static Image[] placaLisa(int ancho, int alto) {
        return recortar(new int[]{20, 690, 1359}, 343, 646, 188, ancho, alto);
    }

    // un botón con la placa chica y el texto encima (como el Volver de las ranuras)
    public static JButton botonPlaca(String texto, int ancho, int alto, float tamanioFuente) {
        JButton boton = new JButton(texto);
        boton.setFont(fuente(tamanioFuente));
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(ancho, alto));
        Image[] placa = placaChica(ancho, alto);
        if (placa != null) {
            boton.setIcon(new ImageIcon(placa[0]));
            boton.setRolloverIcon(new ImageIcon(placa[1]));
            boton.setPressedIcon(new ImageIcon(placa[2]));
            boton.setContentAreaFilled(false);
            boton.setBorderPainted(false);
            boton.setHorizontalTextPosition(SwingConstants.CENTER);
            boton.setVerticalTextPosition(SwingConstants.CENTER);
        }
        return boton;
    }

    // texto de solo lectura, transparente, que corta las líneas largas
    public static JTextArea areaDeTexto(float tamanio) {
        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFocusable(false);
        area.setOpaque(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(fuente(tamanio));
        area.setForeground(Color.WHITE);
        return area;
    }

    // recuadro oscuro semitransparente con borde dorado, título arriba y el texto con barra de desplazamiento
    public static JPanel caja(String titulo, JTextArea contenido) {
        JPanel panel = new JPanel(new BorderLayout(0, 6)) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(10, 11, 15, 215));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(DORADO);
                g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 10));
        if (titulo != null) {
            JLabel etiqueta = new JLabel(titulo);
            etiqueta.setFont(fuente(24f));
            etiqueta.setForeground(DORADO);
            panel.add(etiqueta, BorderLayout.NORTH);
        }
        JScrollPane desplazable = new JScrollPane(contenido);
        desplazable.setOpaque(false);
        desplazable.getViewport().setOpaque(false);
        desplazable.setBorder(BorderFactory.createEmptyBorder());
        desplazable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(desplazable, BorderLayout.CENTER);
        return panel;
    }

    // agrega los mensajes al final y baja hasta ellos
    public static void registrar(JTextArea registro, String mensajes) {
        if (mensajes == null || mensajes.isEmpty()) return;
        if (!registro.getText().isEmpty()) registro.append("\n");
        registro.append(mensajes);
        registro.setCaretPosition(registro.getDocument().getLength());
    }

    public static String describir(Armas arma) {
        if (arma == null) return "ninguna";
        return arma.getNombre() + " (+" + arma.getDanioFisico() + " fís, +" + arma.getDanioMagico() + " mág)";
    }

    public static String describir(Armaduras armadura) {
        if (armadura == null) return "ninguna";
        return armadura.getNombre() + " (+" + armadura.getDefensaFisica() + " fís, +" + armadura.getDefensaMagica() + " mág)";
    }

    // "Caballero Sagrado 2" -> "CS2", para el cuadradito del retrato
    public static String iniciales(String nombre) {
        StringBuilder iniciales = new StringBuilder();
        for (String palabra : nombre.split(" ")) {
            if (palabra.isEmpty()) continue;
            iniciales.append(Character.isDigit(palabra.charAt(0)) ? palabra : palabra.substring(0, 1).toUpperCase());
        }
        return iniciales.toString();
    }

    // (merge) Icono de la clase (los mismos de la pantalla de selección), buscado por
    // e.getClass().getSimpleName(): así sale bien aunque el personaje se llame "Mago 2".
    // Los originales son de 1254x1254, así que se achican una sola vez y se guardan.
    private static final int TAMANIO_ICONO = 160;
    private static final Map<String, String> ICONO_CLASE = Map.of(
            "Mago", "Into_the_Darkness_Mage_Icon.png",
            "Guerrero", "Into_the_Darkness_Warrior_Icon.png",
            "Curador", "Into_the_Darkness_Healer_Icon.png",
            "Arquero", "Into_the_Darkness_Archer_Icon.png",
            "Asesino", "Into_the_Darkness_Assasin_Icon.png",
            "Caballero", "Into_the_Darkness_Knight_Icon.png",
            "Cazador", "Into_the_Darkness_Hunter_Icon.png"
    );
    private static final Map<String, Image> iconos = new HashMap<>();

    // null si esa clase no tiene icono (enemigos, Guardia Real): la tarjeta muestra las iniciales
    public static Image iconoDeClase(Entidad entidad) {
        String clase = entidad.getClass().getSimpleName();
        if (iconos.containsKey(clase)) return iconos.get(clase);
        Image icono = null;
        String archivo = ICONO_CLASE.get(clase);
        if (archivo != null) {
            try {
                BufferedImage original = ImageIO.read(new File("src/ui/imagenes/" + archivo));
                icono = new ImageIcon(original.getScaledInstance(TAMANIO_ICONO, TAMANIO_ICONO, Image.SCALE_SMOOTH)).getImage();
            } catch (IOException e) {
                System.out.println("No se pudo leer el icono de " + clase + ": " + e.getMessage());
            }
        }
        iconos.put(clase, icono);
        return icono;
    }

    private static Image[] recortar(int[] columnas, int y, int anchoRecorte, int altoRecorte, int ancho, int alto) {
        cargar();
        if (hojaPaneles == null) return null;
        Image[] estados = new Image[columnas.length];
        for (int i = 0; i < columnas.length; i++) {
            // ImageIcon espera a que la imagen achicada esté lista antes de usarla
            estados[i] = new ImageIcon(hojaPaneles.getSubimage(columnas[i], y, anchoRecorte, altoRecorte)
                    .getScaledInstance(ancho, alto, Image.SCALE_SMOOTH)).getImage();
        }
        return estados;
    }

    private static void cargar() {
        if (cargado) return;
        cargado = true;
        fuente = new Font("Serif", Font.BOLD, 20);
        try {
            fuente = Font.createFont(Font.TRUETYPE_FONT, new File("src/ui/fuentes/CormorantGaramond-SemiBold.ttf"));
        } catch (IOException | FontFormatException e) {
            // se queda con la fuente de Java
        }
        try {
            hojaPaneles = ImageIO.read(new File("src/ui/imagenes/Into_the_Darkness_ArtePantallaDePartidas.png"));
        } catch (IOException e) {
            System.out.println("Error al leer la imagen de paneles: " + e.getMessage());
        }
    }
}
