package ui;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import combate.PartyAliada;
import entidades.enemigos.Enemigo;
import entidades.personajes.Personaje;
import objetos.consumibles.Consumibles;
import objetos.equipables.Armaduras;
import objetos.equipables.Armas;
import partida.Partida;

// Antes de cada pelea: arma, armadura y objetos para cada personaje en pie, de a uno.
// Cuando la mochila se llena, pasa sola al siguiente personaje (o empieza la pelea si era el último).
public class PantallaPreparacion extends JPanel {
    private static final int ANCHO_BOTON = 236;
    private static final int ALTO_BOTON = 54;
    private static final int ANCHO_CONTENIDO = 1200;

    private VentanaPrincipal ventana;
    private Partida partida;
    private int ranura;
    private Image fondo;
    private List<Personaje> enPie = new ArrayList<>();
    private int indice;

    private JLabel titulo;
    private JLabel enemigos;
    private JLabel progreso;
    private TarjetaEntidad tarjeta;
    private JTextArea datos;
    private JPanel cajaDatos;
    private JLabel tituloArma;
    private JPanel armas;
    private JLabel tituloArmadura;
    private JPanel armaduras;
    private JLabel tituloObjetos;
    private JPanel objetos;
    private JButton botonPausa;
    private JButton botonAnterior;
    private JButton botonSiguiente;

    public PantallaPreparacion(VentanaPrincipal ventana, Partida partida, int ranura) {
        this.ventana = ventana;
        this.partida = partida;
        this.ranura = ranura;
        fondo = new ImageIcon("src/ui/imagenes/Into_The_Darkness_PlayMenu.png").getImage();
        setLayout(null);
        partida.tomarMensajes(); // el "¡Aparecen enemigos!" ya se muestra arriba

        for (Personaje p : partida.getParty().getMiembrosJugador()) {
            if (p.estaVivo()) enPie.add(p);
        }

        titulo = etiqueta("¡Combate!  ·  Preparen el equipo", 38f, Recursos.DORADO, SwingConstants.CENTER);
        List<String> nombres = new ArrayList<>();
        for (Enemigo enemigo : partida.getEnemigosPendientes().getMiembrosEnemigos()) {
            nombres.add(enemigo.getNombre() + " (" + enemigo.getPuntosDeSalud() + " HP)");
        }
        enemigos = etiqueta("Enemigos: " + String.join(",  ", nombres), 22f, Color.WHITE, SwingConstants.CENTER);
        progreso = etiqueta("", 22f, Recursos.TEXTO_SECUNDARIO, SwingConstants.LEFT);

        tarjeta = new TarjetaEntidad(400, 120);
        add(tarjeta);
        datos = Recursos.areaDeTexto(19f);
        cajaDatos = Recursos.caja(null, datos);
        add(cajaDatos);

        tituloArma = etiqueta("", 24f, Recursos.DORADO, SwingConstants.LEFT);
        armas = fila();
        tituloArmadura = etiqueta("", 24f, Recursos.DORADO, SwingConstants.LEFT);
        armaduras = fila();
        tituloObjetos = etiqueta("", 24f, Recursos.DORADO, SwingConstants.LEFT);
        objetos = fila();

        botonPausa = Recursos.botonPlaca("Pausa", 180, 58, 22f);
        botonPausa.addActionListener(e -> MenuDePausa.abrir(ventana, this, partida, ranura));
        add(botonPausa);
        MenuDePausa.conEscape(this, () -> MenuDePausa.abrir(ventana, this, partida, ranura));

        botonAnterior = Recursos.botonPlaca("Anterior", 180, 58, 22f);
        botonAnterior.addActionListener(e -> {
            if (indice > 0) indice--;
            mostrarPersonaje();
        });
        add(botonAnterior);

        botonSiguiente = Recursos.botonPlaca("Siguiente", 220, 58, 22f);
        botonSiguiente.addActionListener(e -> siguiente());
        add(botonSiguiente);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                posicionar();
            }
        });

        mostrarPersonaje();
    }

    private Personaje actual() {
        return enPie.get(indice);
    }

    private void siguiente() {
        if (indice < enPie.size() - 1) {
            indice++;
            mostrarPersonaje();
        } else {
            partida.comenzarBatalla();
            ventana.combatir(partida, ranura);
        }
    }

    // ---- Lo que se toca ----

    private void equiparArma(Armas arma) {
        partida.getParty().equiparArma(actual(), arma);
        mostrarPersonaje();
    }

    private void equiparArmadura(Armaduras armadura) {
        partida.getParty().equiparArmadura(actual(), armadura);
        mostrarPersonaje();
    }

    private void darObjeto(Consumibles consumible) {
        Personaje p = actual();
        if (!partida.getParty().darConsumible(p, consumible)) {
            Dialogo.avisar(this, "No entra", consumible.getNombre() + " ocupa " + consumible.getEspacios()
                    + " espacio/s y a " + p.getNombre() + " le quedan " + (p.getCapacidadInventario() - p.getEspaciosUsados()) + ".");
            return;
        }
        // mochila llena: se pasa solo al siguiente
        if (p.mochilaLlena()) siguiente();
        else mostrarPersonaje();
    }

    private void vaciarMochila() {
        partida.getParty().vaciarMochila(actual());
        mostrarPersonaje();
    }

    // ---- Mostrar ----

    private void mostrarPersonaje() {
        Personaje p = actual();
        PartyAliada party = partida.getParty();
        progreso.setText("Personaje " + (indice + 1) + " de " + enPie.size()
                + (enPie.size() < party.getMiembrosJugador().size() ? "   (los caídos no pelean; reviven en una fogata)" : ""));
        tarjeta.mostrar(p, "Mochila " + p.getEspaciosUsados() + " / " + p.getCapacidadInventario());

        StringBuilder texto = new StringBuilder();
        texto.append("Ataque físico ").append(p.getAtaqueFisico()).append("   ·   mágico ").append(p.getAtaqueMagico()).append('\n');
        texto.append("Defensa física ").append(p.getDefensaFisica()).append("   ·   mágica ").append(p.getDefensaMagica()).append('\n');
        texto.append("Velocidad ").append(p.getVelocidad()).append("   ·   Precisión ").append(p.getPrecision()).append("\n\n");
        texto.append("En la mochila:");
        if (p.getMochila().isEmpty()) texto.append(" nada");
        for (Consumibles c : p.getMochila()) texto.append("\n   •  ").append(c.getNombre());
        datos.setText(texto.toString());
        datos.setCaretPosition(0);

        tituloArma.setText("Arma: " + Recursos.describir(p.getArma()));
        armas.removeAll();
        for (Armas arma : party.getInventario().getArmas()) {
            JButton boton = boton(arma.getNombre(), Recursos.describir(arma) + " — " + arma.getDescripcion());
            boton.addActionListener(e -> equiparArma(arma));
            armas.add(boton);
        }
        if (armas.getComponentCount() == 0) armas.add(nota("No hay otras armas en el inventario."));

        tituloArmadura.setText("Armadura: " + Recursos.describir(p.getArmadura()));
        armaduras.removeAll();
        for (Armaduras armadura : party.getInventario().getArmaduras()) {
            JButton boton = boton(armadura.getNombre(), Recursos.describir(armadura) + " — " + armadura.getDescripcion());
            boton.addActionListener(e -> equiparArmadura(armadura));
            armaduras.add(boton);
        }
        if (armaduras.getComponentCount() == 0) armaduras.add(nota("No hay otras armaduras en el inventario."));

        tituloObjetos.setText("Objetos  ·  mochila " + p.getEspaciosUsados() + " / " + p.getCapacidadInventario()
                + (p.mochilaLlena() ? "  (llena)" : ""));
        objetos.removeAll();
        // los repetidos se agrupan en un solo botón: "Poción de vida chica ×2"
        Map<String, List<Consumibles>> grupos = new LinkedHashMap<>();
        for (Consumibles c : party.getInventario().getConsumibles()) {
            grupos.computeIfAbsent(c.getNombre(), k -> new ArrayList<>()).add(c);
        }
        for (List<Consumibles> grupo : grupos.values()) {
            Consumibles c = grupo.get(0);
            String texto2 = c.getNombre() + (grupo.size() > 1 ? "  ×" + grupo.size() : "");
            JButton boton = boton(texto2, c.getDescripcion() + " (ocupa " + c.getEspacios() + " espacio/s)");
            boton.addActionListener(e -> darObjeto(c));
            objetos.add(boton);
        }
        if (!p.getMochila().isEmpty()) {
            JButton vaciar = boton("Vaciar mochila", "Devuelve al inventario todo lo que lleva");
            vaciar.addActionListener(e -> vaciarMochila());
            objetos.add(vaciar);
        }
        if (objetos.getComponentCount() == 0) objetos.add(nota("No quedan objetos en el inventario."));

        botonAnterior.setVisible(indice > 0);
        botonSiguiente.setText(indice < enPie.size() - 1 ? "Siguiente" : "¡A pelear!");
        revalidate();
        repaint();
    }

    private JLabel etiqueta(String texto, float tamanio, Color color, int alineacion) {
        JLabel etiqueta = new JLabel(texto, alineacion);
        etiqueta.setFont(Recursos.fuente(tamanio));
        etiqueta.setForeground(color);
        add(etiqueta);
        return etiqueta;
    }

    private JPanel fila() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        panel.setOpaque(false);
        add(panel);
        return panel;
    }

    private JButton boton(String texto, String ayuda) {
        JButton boton = Recursos.botonPlaca(texto, ANCHO_BOTON, ALTO_BOTON, 19f);
        boton.setToolTipText(ayuda);
        return boton;
    }

    private JLabel nota(String texto) {
        JLabel nota = new JLabel(texto);
        nota.setFont(Recursos.fuente(19f));
        nota.setForeground(Recursos.TEXTO_SECUNDARIO);
        return nota;
    }

    private void posicionar() {
        int ancho = getWidth();
        int alto = getHeight();
        int x = Math.max(20, (ancho - ANCHO_CONTENIDO) / 2);
        titulo.setBounds(0, 14, ancho, 48);
        enemigos.setBounds(0, 60, ancho, 30);

        progreso.setBounds(x, 100, 900, 30);
        tarjeta.setBounds(x, 136, 400, 120);
        cajaDatos.setBounds(x, 266, 400, alto - 266 - 100);

        int xDerecha = x + 430;
        int anchoDerecha = ANCHO_CONTENIDO - 430;
        int y = 136;
        tituloArma.setBounds(xDerecha, y, anchoDerecha, 30);
        armas.setBounds(xDerecha, y + 32, anchoDerecha, 2 * (ALTO_BOTON + 6) + 6);
        y += 32 + 2 * (ALTO_BOTON + 6) + 14;
        tituloArmadura.setBounds(xDerecha, y, anchoDerecha, 30);
        armaduras.setBounds(xDerecha, y + 32, anchoDerecha, ALTO_BOTON + 12);
        y += 32 + ALTO_BOTON + 20;
        tituloObjetos.setBounds(xDerecha, y, anchoDerecha, 30);
        objetos.setBounds(xDerecha, y + 32, anchoDerecha, alto - (y + 32) - 90);

        int yAbajo = alto - 80;
        botonPausa.setBounds(x, yAbajo, 180, 58);
        botonSiguiente.setBounds(x + ANCHO_CONTENIDO - 220, yAbajo, 220, 58);
        botonAnterior.setBounds(x + ANCHO_CONTENIDO - 220 - 190, yAbajo, 180, 58);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(fondo, 0, 0, getWidth(), getHeight(), this);
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());
    }
}

