package ui;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import controlador.ControladorCombate;
import controlador.ControladorJuego;
import dto.CombateDTO;
import dto.EntidadDTO;
import dto.EstadoDelCombate;
import dto.EstadoDelJuego;
import dto.HabilidadDTO;
import dto.ItemDTO;
import dto.ResumenDeBatalla;
import dto.SeleccionDeObjetivo;

// La pelea: arriba el escenario con los personajes animados (la party a la izquierda, los enemigos a la
// derecha) y abajo lo que va pasando (izquierda) y las acciones (derecha).
// Para atacar o curar a alguien se lo toca en el escenario (los que se pueden elegir se marcan en dorado).
// Los turnos de los enemigos se juegan solos, de a uno, con una pausa corta para que se vean.
//
// (merge) La lógica de turnos, menús, objetos, defender y pausa es la del amigo. Lo que cambió es cómo
// se muestra: en vez de las tarjetas de arriba y abajo, cada entidad se dibuja con su tira de 6 cuadros
// (animación de respiración), el fondo de la sala y los jefes más grandes.
public class PantallaCombate extends JPanel {
    private static final int ESPERA_ENEMIGO = 800;
    private static final int ALTO_TITULO = 50;
    private static final int ALTO_ABAJO = 250;   // registro + acciones

    // =====================================================================================
    // IMAGENES. Si un archivo no existe se dibuja un rectángulo de color en su lugar.
    // =====================================================================================
    private static final String RUTA_FONDO = "src/ui/imagenes/combate/fondo.png";

    // Cada entidad busca su arte según su tipo (EntidadDTO.getTipo(): "Mago", "Zombie"...), no por la
    // posición en la lista. Cada archivo es una tira de 6 cuadros de la misma animación, uno al lado
    // del otro. Para sumar una clase nueva: poner el archivo en la carpeta y agregar una línea acá.
    private static final String DIR_ALIADOS = "src/ui/imagenes/combate/aliados/";
    private static final Map<String, String> ARCHIVO_ALIADO = Map.of(
            "Mago", "mago.png",
            "Guerrero", "guerrero.png",
            "Curador", "curador.png",
            "Arquero", "arquero.png",
            "Asesino", "asesino.png",
            "Caballero Sagrado", "caballero.png",
            "Cazador", "cazador.png",
            "Guardia Real", "guardiareal.png"   // invocación aliada
    );

    // Incluye los jefes y sus invocaciones (Golem -> CopiaDeGolem, Rey Vampiro -> Murcielago).
    private static final String DIR_ENEMIGOS = "src/ui/imagenes/combate/enemigos/";
    private static final Map<String, String> ARCHIVO_ENEMIGO = Map.ofEntries(
            Map.entry("Esqueleto", "esqueleto.png"),
            Map.entry("Zombie", "zombie.png"),
            Map.entry("Hombre Lobo", "hombrelobo.png"),
            Map.entry("Vampiro", "vampiro.png"),
            Map.entry("Lobo Salvaje", "lobosalvaje.png"),
            Map.entry("Bruja", "bruja.png"),
            Map.entry("Golem", "copiadegolem.png"),
            Map.entry("Copia de Golem", "golem.png"),
            Map.entry("Dragon", "dragon.png"),
            Map.entry("Rey Vampiro", "reyvampiro.png"),
            Map.entry("Murcielago", "murcielago.png")
    );
    private static final int CUADROS_POR_TIRA = 6;

    // Las tiras son grandes (2172x724): se leen una sola vez para todo el juego, no en cada pelea.
    private static final Map<String, BufferedImage[]> CACHE_CUADROS = new HashMap<>();
    private static BufferedImage fondoCombate;

    // ---- Animación de respiración: se ciclan los 6 cuadros ----
    private static final int TICKS_POR_CUADRO = 6;   // a 50ms por tick, 300ms por cuadro
    private static final double FACTOR_JEFE = 1.8;   // cuánto más grande se dibuja un jefe (si hay lugar)
    private int contadorAnimacion;
    private final Timer animacion;

    private Navegador ventana;
    private ControladorJuego juego;
    private ControladorCombate combate;
    private CombateDTO foto;   // cómo está la pelea; se vuelve a pedir después de cada acción
    private Timer turnoEnemigo;

    private JLabel titulo;
    private Escenario escenario;
    private JTextArea registro;
    private JPanel cajaRegistro;
    private JLabel indicacion;
    private JPanel acciones;

    // mientras se elige objetivo: a quiénes se puede tocar y qué hacer con el elegido
    private List<EntidadDTO> elegibles = new ArrayList<>();
    private Consumer<EntidadDTO> alElegir;

    public PantallaCombate(Navegador ventana, ControladorJuego juego) {
        this.ventana = ventana;
        this.juego = juego;
        this.combate = juego.getCombate();
        this.foto = combate.getEstado();
        if (fondoCombate == null) fondoCombate = cargarImagen(RUTA_FONDO);
        setLayout(null);

        titulo = new JLabel("", SwingConstants.CENTER);
        titulo.setFont(Recursos.fuente(30f));
        titulo.setForeground(Recursos.DORADO);
        add(titulo);

        escenario = new Escenario();
        add(escenario);

        registro = Recursos.areaDeTexto(17f);
        cajaRegistro = Recursos.caja("Lo que va pasando", registro);
        add(cajaRegistro);

        indicacion = new JLabel("", SwingConstants.LEFT);
        indicacion.setFont(Recursos.fuente(24f));
        indicacion.setForeground(Color.WHITE);
        add(indicacion);

        acciones = new JPanel(new GridLayout(0, 3, 10, 10));
        acciones.setOpaque(false);
        add(acciones);

        MenuDePausa.conEscape(this, this::pausa);

        turnoEnemigo = new Timer(ESPERA_ENEMIGO, e -> {
            combate.turnoEnemigo();
            actualizar();
        });
        turnoEnemigo.setRepeats(false);

        animacion = new Timer(50, e -> {
            contadorAnimacion++;
            escenario.repaint();
        });
        animacion.start();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionar();
            }
        });

        actualizar();
    }

    // VentanaPrincipal saca esta pantalla cuando arma la próxima: ahí se frenan los timers,
    // si no seguirían corriendo (y repintando) para siempre.
    @Override
    public void removeNotify() {
        animacion.stop();
        turnoEnemigo.stop();
        super.removeNotify();
    }

    // ---- El turno ----

    private void actualizar() {
        Recursos.registrar(registro, combate.tomarMensajes());
        foto = combate.getEstado();
        titulo.setText("Ronda " + foto.getRonda() + "  ·  Turno de " + foto.getTurnoActual().getNombre());
        EstadoDelCombate estado = foto.getEstado();
        if (estado == EstadoDelCombate.VICTORIA || estado == EstadoDelCombate.DERROTA) {
            mostrarTarjetas();
            menuVacio(estado == EstadoDelCombate.VICTORIA ? "¡Victoria!" : "La party cayó…");
            // un instante para que se vea el último golpe
            Timer fin = new Timer(600, e -> terminar());
            fin.setRepeats(false);
            fin.start();
            return;
        }
        if (estado != EstadoDelCombate.EN_CURSO) return;
        elegibles.clear();
        if (foto.esTurnoDelJugador()) {
            menuPrincipal();
        } else {
            menuVacio(foto.getTurnoActual().getNombre() + " está actuando…");
            turnoEnemigo.restart();
        }
        mostrarTarjetas();
    }

    private EntidadDTO actual() {
        return foto.getTurnoActual();
    }

    private void menuPrincipal() {
        EntidadDTO p = actual();
        indicacion.setText("¿Qué hace " + p.getNombre() + "?");
        acciones.removeAll();
        agregarAccion("Atacar", "Ataque básico: " + foto.getBasico().getNombre(), () -> usar(-1));
        agregarAccion("Defender", "Recibe la mitad del daño hasta su próximo turno", () -> {
            combate.defender();
            actualizar();
        });
        agregarAccion("Habilidad", "Usar una habilidad (gasta maná)", this::menuHabilidades);
        agregarAccion("Objeto", "Usar algo de la mochila", this::menuObjetos);
        agregarAccion("Pasar", "No hace nada este turno", () -> {
            combate.pasarTurno();
            actualizar();
        });
        agregarAccion("Pausa", "Reanudar, guardar o salir", this::pausa);
        acciones.revalidate();
        acciones.repaint();
    }

    private void menuHabilidades() {
        EntidadDTO p = actual();
        indicacion.setText("Habilidades de " + p.getNombre() + "  ·  maná " + p.getRecurso());
        acciones.removeAll();
        List<HabilidadDTO> habilidades = foto.getHabilidades();
        for (int i = 0; i < habilidades.size(); i++) {
            HabilidadDTO h = habilidades.get(i);
            int indice = i;
            String texto = h.getNombre() + (h.getCosto() > 0 ? " (" + h.getCosto() + ")" : "");
            agregarAccion(texto, h.getDescripcion(), () -> {
                // el controlador dice si se puede usar ahora (recarga, maná) y por qué no
                String motivo = combate.motivoNoUsable(indice);
                if (motivo != null) {
                    Recursos.registrar(registro, motivo);
                } else {
                    usar(indice);
                }
            });
        }
        agregarAccion("Volver", null, this::actualizar);
        acciones.revalidate();
        acciones.repaint();
    }

    private void menuObjetos() {
        String motivo = combate.motivoSinObjetos();
        if (motivo != null) {
            Recursos.registrar(registro, motivo);
            return;
        }
        EntidadDTO p = actual();
        indicacion.setText("Mochila de " + p.getNombre());
        acciones.removeAll();
        for (ItemDTO c : foto.getMochila()) {
            agregarAccion(c.getNombre(), c.getDescripcion(), () -> elegirObjetivo("¿Sobre quién usás " + c.getNombre() + "?",
                    combate.destinosDeObjeto(), objetivo -> {
                        combate.usarConsumible(c.getIndice(), objetivo.getId());
                        actualizar();
                    }));
        }
        agregarAccion("Volver", null, this::actualizar);
        acciones.revalidate();
        acciones.repaint();
    }

    // indice -1 = el ataque básico. El controlador dice si hay que elegir a alguien, si se usa directo o si no se puede.
    private void usar(int indice) {
        EntidadDTO p = actual();
        HabilidadDTO habilidad = indice < 0 ? foto.getBasico() : foto.getHabilidades().get(indice);
        SeleccionDeObjetivo seleccion = combate.objetivosDe(indice);
        switch (seleccion.getModo()) {
            case NINGUNO:
                Recursos.registrar(registro, seleccion.getMensaje());
                break;
            case ELEGIR:
                String pregunta = habilidad.eligeEnemigo() ? "¿A quién ataca " + p.getNombre() + "?"
                        : "¿Sobre quién usa " + habilidad.getNombre() + "?";
                elegirObjetivo(pregunta, seleccion.getCandidatos(), elegido -> ejecutar(indice, elegido.getId()));
                break;
            default:
                ejecutar(indice, -1);
                break;
        }
    }

    private void ejecutar(int indice, int idObjetivo) {
        combate.usarHabilidad(indice, idObjetivo);
        actualizar();
    }

    private void elegirObjetivo(String pregunta, List<EntidadDTO> posibles, Consumer<EntidadDTO> accion) {
        elegibles = new ArrayList<>(posibles);
        alElegir = accion;
        indicacion.setText(pregunta);
        acciones.removeAll();
        agregarAccion("Cancelar", null, this::actualizar);
        acciones.revalidate();
        acciones.repaint();
        mostrarTarjetas();
    }

    // las entidades se nombran por su id (cada foto de la pelea trae objetos nuevos)
    private boolean esElegible(EntidadDTO entidad) {
        if (entidad == null) return false;
        for (EntidadDTO e : elegibles) {
            if (e.getId() == entidad.getId()) return true;
        }
        return false;
    }

    private void tocar(EntidadDTO entidad) {
        if (!esElegible(entidad)) return;
        Consumer<EntidadDTO> accion = alElegir;
        elegibles.clear();
        accion.accept(entidad);
    }

    private void pausa() {
        if (combate.estado() != EstadoDelCombate.EN_CURSO) return;
        turnoEnemigo.stop();
        combate.pausar();
        boolean seguir = MenuDePausa.abrir(ventana, this, juego);
        combate.reanudar();
        if (seguir) actualizar();
    }

    // ---- Fin de la pelea ----

    private void terminar() {
        animacion.stop();
        ResumenDeBatalla resultado = juego.terminarBatalla();
        String resumen = resultado.texto();
        EstadoDelJuego estado = juego.getEstado();
        if (estado == EstadoDelJuego.GANADA || estado == EstadoDelJuego.PERDIDA) {
            ventana.finalizar();
            return;
        }
        Dialogo.avisar(this, "¡Victoria!", resumen);
        ventana.volverAlMapa("¡Victoria! " + resumen.replace("\n", " "));
    }

    // ---- Mostrar ----

    private void menuVacio(String texto) {
        indicacion.setText(texto);
        acciones.removeAll();
        acciones.revalidate();
        acciones.repaint();
    }

    private void agregarAccion(String texto, String ayuda, Runnable accion) {
        JButton boton = Recursos.botonPlaca(texto, 186, 58, 20f);
        boton.setToolTipText(ayuda);
        boton.addActionListener(e -> accion.run());
        acciones.add(boton);
    }

    // Antes rehacía las tarjetas; ahora el escenario se dibuja solo a partir de la batalla
    // (incluye las invocaciones que aparezcan), así que alcanza con repintarlo.
    private void mostrarTarjetas() {
        escenario.repaint();
    }

    // los estados que tiene encima (sin las pasivas, que son permanentes): ya vienen armados en el DTO
    private String efectos(EntidadDTO entidad) {
        return entidad.getEfectos();
    }

    private void posicionar() {
        int ancho = getWidth();
        int alto = getHeight();
        titulo.setBounds(0, 6, ancho, 42);

        int yAbajo = alto - ALTO_ABAJO;
        escenario.setBounds(0, ALTO_TITULO, ancho, yAbajo - ALTO_TITULO);

        int altoAbajo = ALTO_ABAJO - 16;
        int anchoRegistro = ancho / 2 - 40;
        cajaRegistro.setBounds(20, yAbajo + 6, anchoRegistro, altoAbajo);
        int xAcciones = 20 + anchoRegistro + 30;
        indicacion.setBounds(xAcciones, yAbajo + 6, ancho - xAcciones - 20, 32);
        acciones.setBounds(xAcciones, yAbajo + 44, 3 * 186 + 20, altoAbajo - 38);
    }

    @Override
    protected void paintComponent(Graphics g0) {
        super.paintComponent(g0);
        Graphics2D g = (Graphics2D) g0;
        int ancho = getWidth();
        int alto = getHeight();
        if (fondoCombate != null) {
            g.drawImage(fondoCombate, 0, 0, ancho, alto, this);
        } else {
            g.setColor(new Color(30, 30, 40));
            g.fillRect(0, 0, ancho, alto);
        }
        // franja oscura arriba (título) y abajo (registro y acciones) para que se lea el texto
        g.setPaint(new GradientPaint(0, 0, new Color(0, 0, 0, 170), 0, ALTO_TITULO + 20, new Color(0, 0, 0, 0)));
        g.fillRect(0, 0, ancho, ALTO_TITULO + 20);
        int yAbajo = alto - ALTO_ABAJO;
        g.setPaint(new GradientPaint(0, yAbajo - 30, new Color(0, 0, 0, 0), 0, yAbajo + 10, new Color(0, 0, 0, 190)));
        g.fillRect(0, yAbajo - 30, ancho, ALTO_ABAJO + 30);
    }

    // =====================================================================================
    // El escenario: dibuja a cada entidad con su animación, nombre, barras y efectos.
    // Se toca a una entidad para elegirla como objetivo.
    // =====================================================================================
    private class Escenario extends JPanel {
        private EntidadDTO bajoElMouse;

        Escenario() {
            setOpaque(false);
            setToolTipText("");   // activa los tooltips (el texto lo da getToolTipText(MouseEvent))
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    EntidadDTO entidad = entidadEn(e.getPoint());
                    if (entidad != null) tocar(entidad);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    bajoElMouse = null;
                }
            });
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    bajoElMouse = entidadEn(e.getPoint());
                    setCursor(Cursor.getPredefinedCursor(
                            esElegible(bajoElMouse) ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
                }
            });
        }

        @Override
        public String getToolTipText(MouseEvent e) {
            EntidadDTO entidad = entidadEn(e.getPoint());
            if (entidad == null) return null;
            return entidad.getNombre() + "  ·  ataque " + entidad.getAtaqueFisico() + "/" + entidad.getAtaqueMagico()
                    + "  ·  defensa " + entidad.getDefensaFisica() + "/" + entidad.getDefensaMagica()
                    + "  ·  velocidad " + entidad.getVelocidad();
        }

        private List<EntidadDTO> entidades(boolean aliados) {
            return aliados ? foto.getAliados() : foto.getEnemigos();
        }

        private boolean esElMouse(EntidadDTO e) {
            return bajoElMouse != null && e.getId() == bajoElMouse.getId();
        }

        // Zona de cada entidad: party en la mitad izquierda, enemigos en la derecha.
        // e.esJefe() (Jefe lo pisa a true) hace que el jefe se vea más grande que sus invocaciones;
        // si tiene invocaciones al lado se achica lo justo para no pisarlas.
        private Rectangle area(EntidadDTO e, boolean aliado, int indice, int total) {
            int mitad = getWidth() / 2;
            int hueco = mitad / Math.max(total, 1);
            int altoMaximo = getHeight() - 90;   // lugar para nombre y barras abajo
            int anchoNormal = Math.min(Math.min(150, hueco - 10), altoMaximo * 3 / 4);
            int ancho = e.esJefe()
                    ? Math.min(Math.min(hueco - 10, altoMaximo * 3 / 4), (int) Math.round(anchoNormal * FACTOR_JEFE))
                    : anchoNormal;
            int alto = ancho * 4 / 3;
            int x = (aliado ? 0 : mitad) + indice * hueco + (hueco - ancho) / 2;
            int y = getHeight() - alto - 80;
            return new Rectangle(x, y, ancho, alto);
        }

        private EntidadDTO entidadEn(Point p) {
            for (boolean aliado : new boolean[]{true, false}) {
                List<EntidadDTO> lista = entidades(aliado);
                for (int i = 0; i < lista.size(); i++) {
                    if (area(lista.get(i), aliado, i, lista.size()).contains(p)) return lista.get(i);
                }
            }
            return null;
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            EntidadDTO turno = foto.getEstado() == EstadoDelCombate.EN_CURSO ? foto.getTurnoActual() : null;
            dibujarBando(g, true, turno);
            dibujarBando(g, false, turno);
            g.dispose();
        }

        private void dibujarBando(Graphics2D g, boolean aliado, EntidadDTO turno) {
            List<EntidadDTO> lista = entidades(aliado);
            for (int i = 0; i < lista.size(); i++) {
                EntidadDTO e = lista.get(i);
                Rectangle r = area(e, aliado, i, lista.size());
                boolean vivo = e.estaVivo();
                boolean elegible = esElegible(e);

                // marca de "elegible": un halo dorado que late, más fuerte con el mouse encima
                if (elegible) {
                    double latido = (Math.sin(contadorAnimacion * 0.25) + 1) / 2;   // 0..1
                    int alfa = (esElMouse(e) ? 150 : 70) + (int) (latido * 60);
                    g.setColor(new Color(226, 178, 84, Math.min(255, alfa)));
                    g.fillOval(r.x - 6, r.y + r.height - 22, r.width + 12, 30);
                }

                BufferedImage[] cuadros = cuadrosDe(e, aliado);
                Graphics2D gSprite = (Graphics2D) g.create();
                if (!vivo) gSprite.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.35f));
                if (cuadros != null) {
                    // desfasaje por índice para que no respiren todos juntos; los caídos quedan quietos
                    int desfasaje = i + (aliado ? 0 : 1);
                    int cuadro = vivo ? (contadorAnimacion / TICKS_POR_CUADRO + desfasaje) % cuadros.length : 0;
                    gSprite.drawImage(cuadros[cuadro], r.x, r.y, r.width, r.height, null);
                } else {
                    gSprite.setColor(aliado ? new Color(70, 130, 180) : new Color(170, 60, 60));
                    gSprite.fillRoundRect(r.x, r.y, r.width, r.height, 20, 20);
                }
                gSprite.dispose();

                if (elegible) {
                    g.setColor(Recursos.DORADO);
                    g.setStroke(new BasicStroke(esElMouse(e) ? 3f : 2f));
                    g.drawRoundRect(r.x - 4, r.y - 4, r.width + 8, r.height + 8, 14, 14);
                } else if (turno != null && e.getId() == turno.getId()) {
                    // flechita dorada sobre quien tiene el turno
                    int cx = r.x + r.width / 2;
                    int[] xs = {cx - 10, cx + 10, cx};
                    int[] ys = {r.y - 18, r.y - 18, r.y - 4};
                    g.setColor(Recursos.DORADO);
                    g.fillPolygon(xs, ys, 3);
                }

                dibujarDatos(g, e, aliado, r, vivo);
            }
        }

        // nombre y nivel, vida, maná (si tiene) y los efectos que tenga encima
        private void dibujarDatos(Graphics2D g, EntidadDTO e, boolean aliado, Rectangle r, boolean vivo) {
            int anchoBarra = Math.max(110, Math.min(r.width, 170));
            int x = r.x + (r.width - anchoBarra) / 2;
            int y = r.y + r.height + 4;

            g.setFont(Recursos.fuente(18f));
            FontMetrics fm = g.getFontMetrics();
            String nombre = e.getNombre() + "  ·  nv " + e.getNivel();
            g.setColor(new Color(0, 0, 0, 160));
            g.drawString(nombre, r.x + (r.width - fm.stringWidth(nombre)) / 2 + 1, y + fm.getAscent() + 1);
            g.setColor(vivo ? Color.WHITE : Recursos.TEXTO_SECUNDARIO);
            g.drawString(nombre, r.x + (r.width - fm.stringWidth(nombre)) / 2, y + fm.getAscent());
            y += fm.getAscent() + 4;

            barra(g, x, y, anchoBarra, 12, e.getPuntosDeSalud(), e.getPuntosDeSaludMaximos(), new Color(176, 44, 44),
                    vivo ? e.getPuntosDeSalud() + " / " + e.getPuntosDeSaludMaximos() : "Caído");
            y += 14;
            if (e.getRecursoMaximo() > 0) {
                barra(g, x, y, anchoBarra, 7, e.getRecurso(), e.getRecursoMaximo(), new Color(60, 110, 205), null);
                y += 9;
            }
            String efectos = efectos(e);
            if (!efectos.isEmpty()) {
                g.setFont(Recursos.fuente(14f));
                fm = g.getFontMetrics();
                String texto = recortar(fm, efectos, Math.max(anchoBarra, r.width) + 20);
                g.setColor(Recursos.DORADO);
                g.drawString(texto, r.x + (r.width - fm.stringWidth(texto)) / 2, y + fm.getAscent());
            }
        }

        private void barra(Graphics2D g, int x, int y, int ancho, int alto, int valor, int maximo, Color color, String texto) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(x, y, ancho, alto);
            int lleno = maximo <= 0 ? 0 : (int) Math.round((double) ancho * Math.max(0, valor) / maximo);
            g.setColor(color);
            g.fillRect(x, y, Math.min(ancho, lleno), alto);
            g.setColor(new Color(255, 255, 255, 70));
            g.drawRect(x, y, ancho, alto);
            if (texto != null) {
                g.setColor(Color.WHITE);
                g.setFont(Recursos.fuente(alto * 1.1f));
                FontMetrics fm = g.getFontMetrics();
                g.drawString(texto, x + (ancho - fm.stringWidth(texto)) / 2, y + (alto + fm.getAscent() - fm.getDescent()) / 2);
            }
        }

        private String recortar(FontMetrics fm, String texto, int anchoMaximo) {
            if (fm.stringWidth(texto) <= anchoMaximo) return texto;
            while (texto.length() > 1 && fm.stringWidth(texto + "…") > anchoMaximo) {
                texto = texto.substring(0, texto.length() - 1);
            }
            return texto + "…";
        }
    }

    // ---- Carga de imágenes ----

    // Busca (y guarda) los 6 cuadros que le tocan a "e" según su tipo. null si no hay arte
    // para esa clase o falta el archivo: en ese caso se dibuja un rectángulo de color.
    private static BufferedImage[] cuadrosDe(EntidadDTO e, boolean aliado) {
        String clase = e.getTipo();
        if (CACHE_CUADROS.containsKey(clase)) return CACHE_CUADROS.get(clase);
        Map<String, String> archivos = aliado ? ARCHIVO_ALIADO : ARCHIVO_ENEMIGO;
        String directorio = aliado ? DIR_ALIADOS : DIR_ENEMIGOS;
        String nombreArchivo = archivos.get(clase);
        BufferedImage[] cuadros = nombreArchivo == null ? null : cargarCuadros(directorio + nombreArchivo);
        CACHE_CUADROS.put(clase, cuadros);
        return cuadros;
    }

    // Corta una tira horizontal de CUADROS_POR_TIRA cuadros iguales.
    private static BufferedImage[] cargarCuadros(String ruta) {
        BufferedImage tira = cargarImagen(ruta);
        if (tira == null) return null;
        int anchoCuadro = tira.getWidth() / CUADROS_POR_TIRA;
        BufferedImage[] cuadros = new BufferedImage[CUADROS_POR_TIRA];
        for (int i = 0; i < CUADROS_POR_TIRA; i++) {
            cuadros[i] = tira.getSubimage(i * anchoCuadro, 0, anchoCuadro, tira.getHeight());
        }
        return cuadros;
    }

    private static BufferedImage cargarImagen(String ruta) {
        try {
            return ImageIO.read(new File(ruta));
        } catch (Exception e) {
            return null;
        }
    }
}
