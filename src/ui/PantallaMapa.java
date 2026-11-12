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
import controlador.ControladorJuego;
import dto.EntidadDTO;
import dto.ItemDTO;
import dto.NodoDTO;
import dto.PartidaDTO;

// El mapa de la partida: la party a la izquierda, los caminos en el medio y el oro,
// el inventario y lo que va pasando a la derecha. Tocando un nodo dorado la partida avanza.
public class PantallaMapa extends JPanel {
    private static final int ANCHO_LATERAL = 320;
    private static final int ALTO_TARJETA = 96;
    private static final int MARGEN = 20;

    private Navegador ventana;
    private ControladorJuego juego;
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

    public PantallaMapa(Navegador ventana, ControladorJuego juego) {
        this.ventana = ventana;
        this.juego = juego;
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

        List<EntidadDTO> personajes = juego.getPartida().getParty();
        tarjetas = new TarjetaEntidad[personajes.size()];
        for (int i = 0; i < tarjetas.length; i++) {
            tarjetas[i] = new TarjetaEntidad(ANCHO_LATERAL, ALTO_TARJETA);
            add(tarjetas[i]);
        }

        oro = new JLabel("", SwingConstants.LEFT);
        oro.setFont(Recursos.fuente(24f));
        oro.setForeground(Recursos.DORADO);
        add(oro);

        panelMapa = new PanelMapa(juego.getMapa(), this::elegirNodo);
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
        // si se cargó la partida en una fogata o en una pelea, se resuelve apenas termina el fundido
        Timer alEntrar = new Timer(900, e -> resolverNodo());
        alEntrar.setRepeats(false);
        alEntrar.start();
    }

    // al volver de una pelea ganada: se anota el resultado y se actualiza la party
    public void volverDelCombate(String resumen) {
        Recursos.registrar(registro, resumen);
        Recursos.registrar(registro, juego.tomarMensajes());
        actualizar();
    }

    // ---- Moverse ----

    private void elegirNodo(NodoDTO nodo) {
        if (!juego.avanzarA(nodo.getId())) return;
        resolverNodo();
    }

    // según dónde quedó la partida: un aviso, la fogata o una pelea
    private void resolverNodo() {
        switch (juego.getEstado()) {
            case EN_MAPA:
                String mensajes = juego.tomarMensajes();
                Recursos.registrar(registro, mensajes);
                if (!mensajes.isEmpty()) Dialogo.avisar(this, "Mapa", mensajes);
                break;
            case EN_FOGATA:
                fogata();
                break;
            case PREPARANDO_BATALLA:
                ventana.preparar();
                return;
            case GANADA:
            case PERDIDA:
                ventana.finalizar();
                return;
            default:
                break;
        }
        actualizar();
    }

    private void fogata() {
        Recursos.registrar(registro, juego.tomarMensajes());
        Dialogo.elegir(this, "Fogata", "Descansar junto al fuego recupera toda la vida y el maná, y levanta a los caídos.",
                "Descansar");
        juego.descansar();
        Recursos.registrar(registro, juego.tomarMensajes());
    }

    // ---- Guardar y pausa ----

    private void guardar() {
        if (MenuDePausa.guardar(this, juego)) {
            Dialogo.avisar(this, "Partida guardada", "Se guardó en la ranura " + juego.getRanura() + ".");
        }
    }

    private void pausa() {
        MenuDePausa.abrir(ventana, this, juego);
    }

    // ---- Mostrar ----

    private void actualizar() {
        PartidaDTO datos = juego.getPartida();
        titulo.setText("Piso " + datos.getPiso() + " de " + datos.getUltimoPiso() + "  ·  Elegí un camino (borde dorado)");
        panelMapa.setMapa(juego.getMapa());   // el mapa se vuelve a pedir cada vez: cambia al avanzar

        List<EntidadDTO> personajes = datos.getParty();
        for (int i = 0; i < tarjetas.length && i < personajes.size(); i++) {
            EntidadDTO p = personajes.get(i);
            String experiencia = p.esNivelMaximo() ? "Nivel máximo"
                    : "Exp " + p.getExperiencia() + " / " + p.getExperienciaParaSubir();
            tarjetas[i].mostrar(p, experiencia);
            tarjetas[i].setToolTipText("<html>" + p.getNombre() + "<br>Arma: " + p.getArma()
                    + "<br>Ataque " + p.getAtaqueFisico() + " fís / " + p.getAtaqueMagico() + " mág"
                    + "<br>Defensa " + p.getDefensaFisica() + " fís / " + p.getDefensaMagica() + " mág"
                    + "<br>Mochila " + p.getEspaciosUsados() + " / " + p.getCapacidadInventario() + "</html>");
        }
        oro.setText("Oro: " + datos.getOro());

        // los objetos repetidos se agrupan: "Poción de vida chica ×2"
        Map<String, Integer> cantidades = new LinkedHashMap<>();
        for (ItemDTO item : datos.getItems()) {
            String nombre = item.getResumen();
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
