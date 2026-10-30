package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import partida.CreadorDePersonajes;
import partida.GuardadoDePartidas;
import partida.Partida;

// Partida nueva: se eligen los 3 personajes con su splash art, iconos y ventana de stats.
// (merge) Ahora recibe la ranura y al tocar Continuar arma la Partida, la guarda en esa ranura
// y abre el mapa (mismo flujo que PantallaPersonajes del amigo).
public class PantallaSeleccionPersonajes extends JPanel {
    private VentanaPrincipal ventana;
    private int ranura;
    private Image fondo;
    private Image splashArtPersonaje;
    private Image statsWindow;
    private Font fuenteStats;

    private static final int CANTIDAD_PERSONAJES = 7;
    private JToggleButton[] marcosPersonajes = new JToggleButton[CANTIDAD_PERSONAJES];
    private JButton botonContinuar;
    private JButton botonVolver;
    private int anchoBotonContinuar = 190;
    private int altoBotonContinuar;
    private int anchoVolver = 220;
    private int altoVolver = 50;
    private int tamañoMarco = 150;
    private int separacionDeseada = 0;
    private int indicePersonajeActual = 0;
    private int scrollHabilidades = 0;
    private int alturaContenidoHabilidades = 0;
    private int alturaSeccion2Actual = 0;
    private int cantidadSeleccionados = 0;
    private boolean revirtiendoSeleccion = false;

    private String[] nombresPersonajes = {
            "Cazador", "Guerrero", "Curador", "Arquero", "Asesino", "Caballero Sagrado", "Mago"
    };

    private String[] atributosDistintivos = {
            "Astucia", "Fuerza", "Curación", "Destreza", "Letalidad", "Constitución", "Inteligencia"
    };

    private String[] capacidadInventario = {
            "—", "6", "4", "4", "3", "6", "3"
    };

    // cada fila: {Pasiva, Básico, Habilidad 1, Habilidad 2, Habilidad 3}
    private String[][] habilidades = {
            { // Cazador
                    "El Vigilante de la Noche — x1.5 de daño contra Criaturas Nocturnas.",
                    "Escopetazo — +7 de daño físico.",
                    "Bomba de Humo — reduce 30% la precisión enemiga (15% en jefes).",
                    "Trampa para Bobos — 4 físico, -30% velocidad y Sangrado al enemigo.",
                    "Crucifijo con Ajo — retrasa y debilita a las criaturas nocturnas presentes."
            },
            { // Guerrero
                    "Valiente — inmune a miedo y efectos de asustar.",
                    "Espadazo — +6 de daño físico.",
                    "Grito de Guerra — +3 de ataque a todo el escuadrón.",
                    "Escudazo — 4 físico, aturde al enemigo este turno.",
                    "Protector — mitiga 80% del daño de un ataque a un aliado o a sí mismo."
            },
            { // Curador
                    "El Salvador — ante un golpe letal, intenta salvar a un compañero.",
                    "Bastonazo — 3 físico, 20% de curar a un aliado al azar.",
                    "Curación Divina — cura 10 HP y da invulnerabilidad 1 turno.",
                    "Azote del Curandero — 4 mágico, -40% curación enemiga.",
                    "Segundo Aliento — otorga 10 de maná/energía a un aliado."
            },
            { // Arquero
                    "Ligero como Pluma — 20% de esquivar un ataque.",
                    "Flechazo Veloz — 6 físico, siempre ataca primero.",
                    "Lluvia de Flechas — 7-10 físico a varios enemigos, puede Sangrar.",
                    "Flechazo Divino — 5 físico + 2 mágico, x1.5 a no muertos.",
                    "Capa del Bosque — sube su esquiva a 75% por 2 turnos."
            },
            { // Asesino
                    "Cazador Sigiloso — invisible, x2 probabilidad de crítico.",
                    "Apuñalar — +7 de daño físico.",
                    "Desvanecimiento — invisible 2 turnos (-80% de ser golpeado).",
                    "Golpe del Ladrón — 6 físico, roba oro o un objeto.",
                    "Daga Serruchada — 9 físico, inflige Sangrado 2 turnos."
            },
            { // Caballero Sagrado
                    "Pacto del Caballero — aliados con <50% vida ganan +40% defensa.",
                    "Mandoble Divino — 4 físico, +1 extra a no muertos.",
                    "Provocación — fuerza a un enemigo a atacarlo.",
                    "Discurso Heroico — +20% daño al escuadrón y quita debuffs.",
                    "Guardia Real — invoca un guardia aliado por 3 turnos."
            },
            { // Mago
                    "Conocimientos Arcanos — 20% de no gastar maná al atacar.",
                    "Proyectil Etéreo — 7 de daño mágico.",
                    "Bola de Fuego — 9 mágico a varios enemigos, aplica Quemadura.",
                    "Lluvia de Escarcha — 10 mágico a todos, ralentiza y puede congelar.",
                    "Escudo Mágico — bloquea 100% del daño recibido este turno."
            }
    };

    private String[] nombresHabilidades = {"Pasiva", "Ataque básico", "Habilidad 1", "Habilidad 2", "Habilidad 3"};

    private String[] nombresCaras = {
            "Into_the_Darkness_Hunter_Icon.png",
            "Into_the_Darkness_Warrior_Icon.png",
            "Into_the_Darkness_Healer_Icon.png",
            "Into_the_Darkness_Archer_Icon.png",
            "Into_the_Darkness_Assasin_Icon.png",
            "Into_the_Darkness_Knight_Icon.png",
            "Into_the_Darkness_Mage_Icon.png"
    };

    private String[] nombresSplashArts = {
            "Into_The_Darkness_Hunter_SplashArt.png",
            "Into_The_Darkness_Warrior_SplashArt.png",
            "Into_the_Darkness_Healer_SplashArt.png",
            "Into_the_Darkness_Archer_SplashArt.png",
            "Into_the_Darkness_Assassin_SplashArt.png",
            "Into_The_Darkness_HolyKnight_SplashArt.png",
            "Into_The_Darkness_Mage_SplashArt.png"
    };
    private Image[] splashArtsPersonajes = new Image[CANTIDAD_PERSONAJES];

    public PantallaSeleccionPersonajes(VentanaPrincipal ventana, int ranura) {
        this.ventana = ventana;
        this.ranura = ranura;
        setLayout(null);

        ImageIcon icon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_Character_Selection_Background.png");
        fondo = icon.getImage();

        ImageIcon statsIcon = new ImageIcon("src/ui/imagenes/Into_The_Darkness_Stats_Window.png");
        statsWindow = statsIcon.getImage();

        fuenteStats = new Font("Serif", Font.PLAIN, 16);
        try {
            Font fuenteCargada = Font.createFont(Font.TRUETYPE_FONT, new File("src/ui/fuentes/CormorantGaramond-SemiBold.ttf"));
            fuenteStats = fuenteCargada.deriveFont(16f);
        } catch (IOException | FontFormatException e) {
            System.out.println("Error al cargar la fuente: " + e.getMessage());
        }

        BufferedImage marcoVacio;
        BufferedImage marcoSeleccionado;
        BufferedImage marcoClickeado;

        try {
            marcoVacio = ImageIO.read(new File("src/ui/imagenes/Into_The_Darkness_Character_Selection_Icon_Square_Empty.png"));
            marcoSeleccionado = ImageIO.read(new File("src/ui/imagenes/Into_The_Darkness_Character_Selection_Icon_Square_Selected.png"));
            marcoClickeado = ImageIO.read(new File("src/ui/imagenes/Into_The_Darkness_Character_Selection_Icon_Square_Clicked.png"));

            for (int i = 0; i < CANTIDAD_PERSONAJES; i++) {
                BufferedImage cara = ImageIO.read(new File("src/ui/imagenes/" + nombresCaras[i]));
                splashArtsPersonajes[i] = new ImageIcon("src/ui/imagenes/" + nombresSplashArts[i]).getImage();

                JToggleButton marco = new JToggleButton();
                marco.setIcon(new ImageIcon(combinarMarcoYCara(marcoVacio, cara, tamañoMarco)));
                marco.setRolloverIcon(new ImageIcon(combinarMarcoYCara(marcoSeleccionado, cara, tamañoMarco)));
                marco.setSelectedIcon(new ImageIcon(combinarMarcoYCara(marcoClickeado, cara, tamañoMarco)));
                marco.setContentAreaFilled(false);
                marco.setBorderPainted(false);
                marco.setFocusPainted(false);

                final int indice = i;
                marco.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        indicePersonajeActual = indice;
                        scrollHabilidades = 0;
                        splashArtPersonaje = splashArtsPersonajes[indice];
                        repaint();
                    }
                });
                marco.addItemListener(e -> {
                    if (revirtiendoSeleccion) return;

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        if (cantidadSeleccionados >= 3) {
                            revirtiendoSeleccion = true;
                            marco.setSelected(false);
                            revirtiendoSeleccion = false;
                        } else {
                            cantidadSeleccionados++;
                        }
                    } else {
                        cantidadSeleccionados--;
                    }
                    botonContinuar.setEnabled(cantidadSeleccionados == 3);
                });

                add(marco);
                marcosPersonajes[i] = marco;
            }
        } catch (IOException e) {
            System.out.println("Error al cargar los marcos: " + e.getMessage());
            return;
        }

        try {
            BufferedImage spriteContinuar = ImageIO.read(new File("src/ui/imagenes/Into_The_Darkness_Continue_Button.png"));
            int anchoFrame = spriteContinuar.getWidth() / 3;
            int altoFrame = spriteContinuar.getHeight();

            BufferedImage normal = spriteContinuar.getSubimage(0, 0, anchoFrame, altoFrame);
            BufferedImage hover = spriteContinuar.getSubimage(anchoFrame, 0, anchoFrame, altoFrame);
            BufferedImage deshabilitado = spriteContinuar.getSubimage(anchoFrame * 2, 0, anchoFrame, altoFrame);

            altoBotonContinuar = (int) (anchoBotonContinuar * ((double) altoFrame / anchoFrame));

            botonContinuar = new JButton();
            botonContinuar.setIcon(new ImageIcon(normal.getScaledInstance(anchoBotonContinuar, altoBotonContinuar, Image.SCALE_SMOOTH)));
            botonContinuar.setRolloverIcon(new ImageIcon(hover.getScaledInstance(anchoBotonContinuar, altoBotonContinuar, Image.SCALE_SMOOTH)));
            botonContinuar.setDisabledIcon(new ImageIcon(deshabilitado.getScaledInstance(anchoBotonContinuar, altoBotonContinuar, Image.SCALE_SMOOTH)));
            botonContinuar.setContentAreaFilled(false);
            botonContinuar.setBorderPainted(false);
            botonContinuar.setFocusPainted(false);
            botonContinuar.setEnabled(false);

            botonContinuar.addActionListener(e -> {
                java.util.List<String> clasesElegidas = new java.util.ArrayList<>();
                for (int i = 0; i < CANTIDAD_PERSONAJES; i++) {
                    if (marcosPersonajes[i].isSelected()) {
                        clasesElegidas.add(nombresPersonajes[i]);
                    }
                }
                comenzar(clasesElegidas);
            });

            add(botonContinuar);
        } catch (IOException e) {
            System.out.println("Error al cargar el botón Continuar: " + e.getMessage());
        }

        try {
            BufferedImage hojaPartidas = ImageIO.read(new File("src/ui/imagenes/Into_the_Darkness_ArtePantallaDePartidas.png"));

            BufferedImage volverNormal = hojaPartidas.getSubimage(20, 582, 648, 111);
            BufferedImage volverSeleccionado = hojaPartidas.getSubimage(690, 582, 646, 111);
            BufferedImage volverPresionado = hojaPartidas.getSubimage(1359, 582, 647, 111);

            botonVolver = new JButton("Volver");
            botonVolver.setIcon(new ImageIcon(volverNormal.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH)));
            botonVolver.setRolloverIcon(new ImageIcon(volverSeleccionado.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH)));
            botonVolver.setPressedIcon(new ImageIcon(volverPresionado.getScaledInstance(anchoVolver, altoVolver, Image.SCALE_SMOOTH)));
            botonVolver.setContentAreaFilled(false);
            botonVolver.setBorderPainted(false);
            botonVolver.setFocusPainted(false);
            botonVolver.setFont(fuenteStats.deriveFont(20f));
            botonVolver.setForeground(Color.WHITE);
            botonVolver.setHorizontalTextPosition(SwingConstants.CENTER);
            botonVolver.setVerticalTextPosition(SwingConstants.CENTER);
            botonVolver.addActionListener(e -> ventana.cambiarPantallaConFundido("partidas"));

            add(botonVolver);
        } catch (IOException e) {
            System.out.println("Error al cargar el botón Volver: " + e.getMessage());
        }

        splashArtPersonaje = splashArtsPersonajes[0];

        posicionarElementos();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionarElementos();
            }
        });
        addMouseWheelListener(e -> {
            int maxScroll = Math.max(0, alturaContenidoHabilidades - alturaSeccion2Actual);
            scrollHabilidades += e.getWheelRotation() * 20;
            scrollHabilidades = Math.max(0, Math.min(scrollHabilidades, maxScroll));
            repaint();
        });
    }

    // Arma la partida con las clases elegidas, la guarda en la ranura y abre el mapa
    private void comenzar(java.util.List<String> clasesElegidas) {
        Partida partida = new Partida(CreadorDePersonajes.crearParty(clasesElegidas));
        try {
            GuardadoDePartidas.guardar(partida, ranura);
        } catch (IOException e) {
            Dialogo.avisar(this, "No se pudo guardar", e.getMessage());
            return;
        }
        ventana.jugar(partida, ranura);
    }

    private Image combinarMarcoYCara(BufferedImage marco, BufferedImage cara, int tamanoFinal) {
        int ancho = marco.getWidth();
        int alto = marco.getHeight();

        BufferedImage combinada = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = combinada.createGraphics();

        int margen = (int) (ancho * 0.20);
        Image caraEscalada = cara.getScaledInstance(ancho - margen * 2, alto - margen * 2, Image.SCALE_SMOOTH);
        g2.drawImage(caraEscalada, margen, margen, null);
        g2.drawImage(marco, 0, 0, ancho, alto, null);
        g2.dispose();

        return combinada.getScaledInstance(tamanoFinal, tamanoFinal, Image.SCALE_SMOOTH);
    }

    private void posicionarElementos() {
        int margenInferior = (int) (getHeight() * 0.04);
        int margenBotonInferior = (int) (getHeight() * 0.015);
        int margenBoton = (int) (getWidth() * 0.03);

        int anchoFila = CANTIDAD_PERSONAJES * tamañoMarco + (CANTIDAD_PERSONAJES - 1) * separacionDeseada;
        int xInicial = (getWidth() - anchoFila) / 2 - 15;
        int xBoton = getWidth() - anchoBotonContinuar - margenBoton;
        int yBoton = getHeight() - altoBotonContinuar - margenBotonInferior;

        int centroBotonesY = yBoton + altoBotonContinuar / 2;
        int xVolver = margenBoton;
        int yVolver = centroBotonesY - altoVolver / 2;

        int y = getHeight() - tamañoMarco - margenInferior;
        // (merge) si la ventana no es tan ancha (ej. 1280x720) los botones pisaban los retratos de
        // las puntas: en ese caso van los dos a la izquierda, uno arriba del otro, sobre la fila de retratos
        if (xInicial < xVolver + anchoVolver + 10) {
            xBoton = xVolver;
            yBoton = y - altoBotonContinuar - 12;
            yVolver = yBoton - altoVolver + 25;   // la imagen de Continuar trae margen transparente arriba
        }
        botonContinuar.setBounds(xBoton, yBoton, anchoBotonContinuar, altoBotonContinuar);
        botonVolver.setBounds(xVolver, yVolver, anchoVolver, altoVolver);
        for (int i = 0; i < CANTIDAD_PERSONAJES; i++) {
            int x = xInicial + i * (tamañoMarco + separacionDeseada);
            marcosPersonajes[i].setBounds(x, y, tamañoMarco, tamañoMarco);
        }
    }

    private java.util.List<String> cortarTexto(String texto, FontMetrics fm, int anchoMaximo) {
        java.util.List<String> lineas = new java.util.ArrayList<>();
        String[] palabras = texto.split(" ");
        StringBuilder lineaActual = new StringBuilder();

        for (String palabra : palabras) {
            String prueba = lineaActual.length() == 0 ? palabra : lineaActual + " " + palabra;
            if (fm.stringWidth(prueba) > anchoMaximo && lineaActual.length() > 0) {
                lineas.add(lineaActual.toString());
                lineaActual = new StringBuilder(palabra);
            } else {
                lineaActual = new StringBuilder(prueba);
            }
        }
        if (lineaActual.length() > 0) lineas.add(lineaActual.toString());
        return lineas;
    }

    private void dibujarHabilidades(Graphics g, int xStats, int yStats, int anchoStatsFinal, int altoStatsFinal) {
        int padding = (int) (anchoStatsFinal * 0.08);
        int xTexto = xStats + padding;
        int anchoTexto = anchoStatsFinal - padding * 2 - 20;

        int yInicioSeccion2 = yStats + (int) (altoStatsFinal * 0.42);
        int alturaSeccion2 = (int) (altoStatsFinal * 0.48);
        alturaSeccion2Actual = alturaSeccion2;

        Graphics2D g2 = (Graphics2D) g;
        Shape clipOriginal = g2.getClip();
        g2.setClip(xStats, yInicioSeccion2, anchoStatsFinal, alturaSeccion2);

        Font fuenteNombreHabilidad = fuenteStats.deriveFont(Font.BOLD, 18f);
        Font fuenteDescripcion = fuenteStats.deriveFont(16f);
        FontMetrics fmDescripcion = g2.getFontMetrics(fuenteDescripcion);

        int y = yInicioSeccion2 - scrollHabilidades;

        for (int i = 0; i < nombresHabilidades.length; i++) {
            g2.setFont(fuenteNombreHabilidad);
            g2.setColor(Color.WHITE);
            y += g2.getFontMetrics().getHeight();
            g2.drawString(nombresHabilidades[i], xTexto, y);

            g2.setFont(fuenteDescripcion);
            g2.setColor(new Color(200, 200, 200));
            for (String linea : cortarTexto(habilidades[indicePersonajeActual][i], fmDescripcion, anchoTexto)) {
                y += fmDescripcion.getHeight();
                g2.drawString(linea, xTexto, y);
            }

            y += 14;
        }

        alturaContenidoHabilidades = y - (yInicioSeccion2 - scrollHabilidades);
        g2.setClip(clipOriginal);

        dibujarScrollbar(g2, xStats, yInicioSeccion2, anchoStatsFinal, alturaSeccion2);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);

        int anchoOriginal = splashArtPersonaje.getWidth(this);
        int altoOriginal = splashArtPersonaje.getHeight(this);

        double anchoDisponible = getWidth() * 0.5;
        double altoDisponible = getHeight() * 0.65;

        double escala = Math.min(
                anchoDisponible / anchoOriginal,
                altoDisponible / altoOriginal
        );

        int anchoFinal = (int) (anchoOriginal * escala);
        int altoFinal = (int) (altoOriginal * escala);

        g.drawImage(splashArtPersonaje, (int) (getWidth() * 0.1), (int) (getHeight() * 0.77 - altoFinal), anchoFinal, altoFinal, this);
        int anchoStatsOriginal = statsWindow.getWidth(this);
        int altoStatsOriginal = statsWindow.getHeight(this);

        double altoStatsDisponible = getHeight() * 0.75;
        double escalaStats = altoStatsDisponible / altoStatsOriginal;

        int anchoStatsFinal = (int) (anchoStatsOriginal * escalaStats);
        int altoStatsFinal = (int) (altoStatsOriginal * escalaStats);

        int xStats = getWidth() - anchoStatsFinal - (int) (getWidth() * 0.03);
        int yStats = (int) (getHeight() * 0.04);

        g.drawImage(statsWindow, xStats, yStats, anchoStatsFinal, altoStatsFinal, this);
        dibujarStats(g, xStats, yStats, anchoStatsFinal, altoStatsFinal);
        dibujarHabilidades(g, xStats, yStats, anchoStatsFinal, altoStatsFinal);
    }

    private void dibujarScrollbar(Graphics2D g2, int xStats, int yInicioSeccion2, int anchoStatsFinal, int alturaSeccion2) {
        if (alturaContenidoHabilidades <= alturaSeccion2) return;

        int anchoRail = 4;
        int xRail = xStats + anchoStatsFinal - (int) (anchoStatsFinal * 0.06);

        g2.setColor(new Color(255, 255, 255, 60));
        g2.fillRoundRect(xRail, yInicioSeccion2, anchoRail, alturaSeccion2, anchoRail, anchoRail);

        int altoThumb = Math.max(20, alturaSeccion2 * alturaSeccion2 / alturaContenidoHabilidades);
        int maxScroll = alturaContenidoHabilidades - alturaSeccion2;
        int yThumb = yInicioSeccion2 + (scrollHabilidades * (alturaSeccion2 - altoThumb)) / maxScroll;

        g2.setColor(new Color(212, 175, 55));
        g2.fillRoundRect(xRail, yThumb, anchoRail, altoThumb, anchoRail, anchoRail);
    }

    private void dibujarStats(Graphics g, int xStats, int yStats, int anchoStatsFinal, int altoStatsFinal) {
        int padding = (int) (anchoStatsFinal * 0.08);
        int xTexto = xStats + padding;

        g.setColor(Color.WHITE);
        g.setFont(fuenteStats.deriveFont(Font.BOLD, 26f));

        int y = yStats + (int) (altoStatsFinal * 0.13);
        FontMetrics fm = g.getFontMetrics();

        g.drawString(nombresPersonajes[indicePersonajeActual], xTexto, y);

        g.setFont(fuenteStats.deriveFont(18f));
        fm = g.getFontMetrics();
        int alturaLinea = fm.getHeight();

        y += alturaLinea + 10;
        g.drawString("Atributo: " + atributosDistintivos[indicePersonajeActual], xTexto, y);

        String[] statsSeccion1 = {
                "Vida máxima: —",
                "Energía máxima: —",
                "Defensa física: — / Defensa mágica: —",
                "Inventario: " + capacidadInventario[indicePersonajeActual] + " objetos"
        };

        for (String linea : statsSeccion1) {
            y += alturaLinea + 6;
            g.drawString(linea, xTexto, y);
        }
    }
}