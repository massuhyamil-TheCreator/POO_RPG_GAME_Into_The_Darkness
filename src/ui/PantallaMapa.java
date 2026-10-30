package ui;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import entidades.personajes.Personaje;
import mapa.Nodo;
import mapa.PanelMapa;
import mapa.TipoNodo;
import objetos.Item;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import partida.EstadoPartida;
import partida.Partida;

// El mapa de la partida: la party a la izquierda, los caminos en el medio y el oro,
// el inventario y lo que va pasando a la derecha. Tocando un nodo dorado la partida avanza.
public class PantallaMapa extends JPanel {
    private static final int ANCHO_LATERAL = 320;
    private static final int ALTO_TARJETA = 96;
    private static final int MARGEN = 20;

    private VentanaPrincipal ventana;
    private Partida partida;
    private int ranura;
    private Image fondo;

    private JLabel titulo;
    private JLabel tituloParty;
    private TarjetaEntidad[] tarjetas;
    private JLabel oro;
    private PanelMapa panelMapa;
    private JPanel cajaInventario;
    private JTextArea inventario;
    private JPanel cajaRegistro;
    private JTextArea registro;
    private JButton botonGuardar;
    private JButton botonPausa;

    public PantallaMapa(VentanaPrincipal ventana, Partida partida, int ranura) {
        this.ventana = ventana;
        this.partida = partida;
        this.ranura = ranura;
        // (merge) el fondo del mapa es el castillo del kit; la franja de piedra y los nodos los dibuja PanelMapa
        fondo = new ImageIcon("src/mapa/imagenes/fondo_mazmorra.png").getImage();
        setLayout(null);

        titulo = new JLabel("", SwingConstants.CENTER);
        titulo.setFont(Recursos.fuente(34f));
        titulo.setForeground(Recursos.DORADO);
        add(titulo);

        tituloParty = new JLabel("Party", SwingConstants.LEFT);
        tituloParty.setFont(Recursos.fuente(28f));
        tituloParty.setForeground(Color.WHITE);
        add(tituloParty);

        List<Personaje> personajes = partida.getParty().getMiembrosJugador();
        tarjetas = new TarjetaEntidad[personajes.size()];
        for (int i = 0; i < tarjetas.length; i++) {
            tarjetas[i] = new TarjetaEntidad(ANCHO_LATERAL, ALTO_TARJETA);
            add(tarjetas[i]);
        }

        oro = new JLabel("", SwingConstants.LEFT);
        oro.setFont(Recursos.fuente(24f));
        oro.setForeground(Recursos.DORADO);
        add(oro);

        panelMapa = new PanelMapa(partida.getMapa(), this::elegirNodo);
        panelMapa.setOpaque(false); // así se ve el fondo detrás de los caminos
        add(panelMapa);

        inventario = Recursos.areaDeTexto(18f);
        cajaInventario = Recursos.caja("Inventario", inventario);
        add(cajaInventario);

        registro = Recursos.areaDeTexto(17f);
        cajaRegistro = Recursos.caja("Lo que va pasando", registro);
        add(cajaRegistro);

        botonGuardar = Recursos.botonPlaca("Guardar", 150, 58, 22f);
        botonGuardar.addActionListener(e -> guardar());
        add(botonGuardar);

        botonPausa = Recursos.botonPlaca("Pausa", 150, 58, 22f);
        botonPausa.addActionListener(e -> pausa());
        add(botonPausa);
        MenuDePausa.conEscape(this, this::pausa);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionar();
            }
        });

        actualizar();
        // si se cargó la partida en una fogata, herrero o pelea, se resuelve apenas termina el fundido
        Timer alEntrar = new Timer(900, e -> resolverNodo());
        alEntrar.setRepeats(false);
        alEntrar.start();
    }

    // al volver de una pelea ganada: se anota el resultado y se actualiza la party
    public void volverDelCombate(String resumen) {
        Recursos.registrar(registro, resumen);
        Recursos.registrar(registro, partida.tomarMensajes());
        actualizar();
    }

    // ---- Moverse ----

    private void elegirNodo(Nodo nodo) {
        if (!partida.avanzarA(nodo)) return;
        resolverNodo();
    }

    // según dónde quedó la partida: un aviso, la fogata, el herrero o una pelea
    private void resolverNodo() {
        switch (partida.getEstado()) {
            case EN_MAPA:
                String mensajes = partida.tomarMensajes();
                Recursos.registrar(registro, mensajes);
                if (!mensajes.isEmpty()) Dialogo.avisar(this, nombreDelNodo(), mensajes);
                break;
            case EN_FOGATA:
                fogata();
                break;
            case EN_HERRERO:
                herrero();
                break;
            case PREPARANDO_BATALLA:
                ventana.preparar(partida, ranura);
                return;
            case GANADA:
            case PERDIDA:
                ventana.finalizar(partida, ranura);
                return;
            default:
                break;
        }
        actualizar();
    }

    private String nombreDelNodo() {
        TipoNodo tipo = partida.getNodoActual().getTpe();
        if (tipo == TipoNodo.Tesoro) return "Tesoro";
        if (tipo == TipoNodo.Evento) return "Evento";
        return "Mapa";
    }

    private void fogata() {
        Recursos.registrar(registro, partida.tomarMensajes());
        Dialogo.elegir(this, "Fogata", "Descansar junto al fuego recupera toda la vida y el maná, y levanta a los caídos.",
                "Descansar");
        partida.descansar();
        Recursos.registrar(registro, partida.tomarMensajes());
    }

    // se mejora una sola cosa por visita: el arma o la armadura de un personaje
    private void herrero() {
        Recursos.registrar(registro, partida.tomarMensajes());
        List<Personaje> personajes = partida.getParty().getMiembrosJugador();
        while (partida.getEstado() == EstadoPartida.EN_HERRERO) {
            String[] opciones = new String[personajes.size() + 1];
            for (int i = 0; i < personajes.size(); i++) opciones[i] = personajes.get(i).getNombre();
            opciones[personajes.size()] = "Irse";
            int elegido = Dialogo.elegir(this, "Herrero",
                    "Puede mejorar una sola cosa: el arma (+2 de daño) o la armadura (+1 a cada defensa). ¿De quién?", opciones);
            if (elegido < 0 || elegido == personajes.size()) {
                partida.salirDelHerrero();
                break;
            }
            Personaje personaje = personajes.get(elegido);
            int mejora = Dialogo.elegir(this, personaje.getNombre(),
                    "Arma: " + Recursos.describir(personaje.getArma()) + "\nArmadura: " + Recursos.describir(personaje.getArmadura()),
                    "Mejorar arma", "Reforzar armadura", "Volver");
            if (mejora == 0) partida.mejorarArma(personaje);
            else if (mejora == 1) partida.mejorarArmadura(personaje);
            String mensajes = partida.tomarMensajes();
            Recursos.registrar(registro, mensajes);
            // si no tenía nada equipado, se avisa y se vuelve a preguntar
            if (!mensajes.isEmpty() && partida.getEstado() == EstadoPartida.EN_HERRERO) Dialogo.avisar(this, "Herrero", mensajes);
        }
    }

    // ---- Guardar y pausa ----

    private void guardar() {
        if (MenuDePausa.guardar(this, partida, ranura)) {
            Dialogo.avisar(this, "Partida guardada", "Se guardó en la ranura " + ranura + ".");
        }
    }

    private void pausa() {
        MenuDePausa.abrir(ventana, this, partida, ranura);
    }

    // ---- Mostrar ----

    private void actualizar() {
        Nodo actual = partida.getNodoActual();
        int ultimoPiso = partida.getMapa().getFilas().size() - 1;
        titulo.setText("Piso " + actual.getFila() + " de " + ultimoPiso + "  ·  Elegí un camino (borde dorado)");

        List<Personaje> personajes = partida.getParty().getMiembrosJugador();
        for (int i = 0; i < tarjetas.length && i < personajes.size(); i++) {
            Personaje p = personajes.get(i);
            String experiencia = p.getNivel() >= Personaje.NIVEL_MAXIMO ? "Nivel máximo"
                    : "Exp " + p.getExperiencia() + " / " + p.getExperienciaParaSubir();
            tarjetas[i].mostrar(p, experiencia);
            tarjetas[i].setToolTipText("<html>" + p.getNombre() + "<br>Arma: " + Recursos.describir(p.getArma())
                    + "<br>Armadura: " + Recursos.describir(p.getArmadura())
                    + "<br>Ataque " + p.getAtaqueFisico() + " fís / " + p.getAtaqueMagico() + " mág"
                    + "<br>Defensa " + p.getDefensaFisica() + " fís / " + p.getDefensaMagica() + " mág"
                    + "<br>Mochila " + p.getEspaciosUsados() + " / " + p.getCapacidadInventario() + "</html>");
        }
        oro.setText("Oro: " + partida.getParty().getOro());

        // los objetos repetidos se agrupan: "Poción de vida chica ×2"
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        for (Item item : partida.getParty().getInventario().getItems()) {
            String nombre = item instanceof Armas ? Recursos.describir((Armas) item)
                    : item instanceof Armaduras ? Recursos.describir((Armaduras) item) : item.getNombre();
            cantidades.merge(nombre, 1, Integer::sum);
        }
        StringBuilder texto = new StringBuilder();
        for (Map.Entry<String, Integer> entrada : cantidades.entrySet()) {
            if (texto.length() > 0) texto.append('\n');
            texto.append("•  ").append(entrada.getKey());
            if (entrada.getValue() > 1) texto.append("  ×").append(entrada.getValue());
        }
        inventario.setText(texto.length() == 0 ? "Vacío" : texto.toString());
        inventario.setCaretPosition(0);
        repaint();
    }

    private void posicionar() {
        int ancho = getWidth();
        int alto = getHeight();
        titulo.setBounds(0, 14, ancho, 46);

        tituloParty.setBounds(MARGEN, 70, ANCHO_LATERAL, 34);
        for (int i = 0; i < tarjetas.length; i++) {
            tarjetas[i].setBounds(MARGEN, 110 + i * (ALTO_TARJETA + 10), ANCHO_LATERAL, ALTO_TARJETA);
        }
        oro.setBounds(MARGEN + 10, 110 + tarjetas.length * (ALTO_TARJETA + 10) + 4, ANCHO_LATERAL, 32);

        int xDerecha = ancho - MARGEN - ANCHO_LATERAL;
        int altoInventario = 230;
        cajaInventario.setBounds(xDerecha, 70, ANCHO_LATERAL, altoInventario);
        cajaRegistro.setBounds(xDerecha, 70 + altoInventario + 12, ANCHO_LATERAL, alto - 70 - altoInventario - 12 - 90);

        int xMapa = MARGEN + ANCHO_LATERAL + 20;
        panelMapa.setBounds(xMapa, 64, xDerecha - 20 - xMapa, alto - 64 - 10);

        int yAbajo = alto - 78;
        botonGuardar.setBounds(xDerecha, yAbajo, 150, 58);
        botonPausa.setBounds(xDerecha + ANCHO_LATERAL - 150, yAbajo, 150, 58);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        // un poco más oscuro, para que se lean los textos de los costados sin tapar el castillo
        g.setColor(new Color(0, 0, 0, 80));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}
