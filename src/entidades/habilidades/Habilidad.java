package entidades.habilidades;
import combate.Batalla;
import entidades.Categoria;
import entidades.Entidad;
import entidades.efectos.Efecto;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class Habilidad {

    protected static final Random AZAR = new Random();

    private String nombre;
    private int costo;
    private Objetivo objetivo;
    private int danioFisico;
    private int danioMagico;
    private int curacion;
    private int mana;
    private int variacion;
    private boolean quitaNegativos;
    private int enfriamiento;
    private int turnosParaVolverAUsar;
    private int minimoObjetivos = 2;
    private int maximoObjetivos = 3;
    private int roboDeVida;
    private boolean retrasaTurno;
    private Categoria categoriaDebil;
    private int porcentajeContraDebil = 100;
    private int extraContraDebil;
    private List<Supplier<Efecto>> creadoresDeEfectos = new ArrayList<>();
    private List<Integer> probabilidades = new ArrayList<>();
    private List<Supplier<Efecto>> creadoresDeEfectosPropios = new ArrayList<>();

    public Habilidad(String nombre, int costo, Objetivo objetivo, int danioFisico, int danioMagico) {
        this.nombre = nombre;
        this.costo = costo;
        this.objetivo = objetivo;
        this.danioFisico = danioFisico;
        this.danioMagico = danioMagico;
    }

    public Habilidad conEfecto(Supplier<Efecto> creadorDeEfecto, int probabilidad) {
        creadoresDeEfectos.add(creadorDeEfecto);
        probabilidades.add(probabilidad);
        return this;
    }

    public Habilidad conEfectoSobreSiMismo(Supplier<Efecto> creadorDeEfecto) {
        creadoresDeEfectosPropios.add(creadorDeEfecto);
        return this;
    }

    public Habilidad conCuracion(int curacion) {
        this.curacion = curacion;
        return this;
    }

    public Habilidad conVariacion(int variacion) {
        this.variacion = variacion;
        return this;
    }

    public Habilidad conMana(int mana) {
        this.mana = mana;
        return this;
    }

    public Habilidad conEnfriamiento(int turnos) {
        this.enfriamiento = turnos;
        return this;
    }

    public Habilidad conCantidadDeObjetivos(int minimo, int maximo) {
        this.minimoObjetivos = minimo;
        this.maximoObjetivos = maximo;
        return this;
    }

    public Habilidad conRoboDeVida(int vida) {
        this.roboDeVida = vida;
        return this;
    }

    public Habilidad retrasandoTurno() {
        this.retrasaTurno = true;
        return this;
    }

    public Habilidad conMultiplicadorContra(Categoria categoria, int porcentaje) {
        this.categoriaDebil = categoria;
        this.porcentajeContraDebil = porcentaje;
        return this;
    }

    public Habilidad conDanioExtraContra(Categoria categoria, int extra) {
        this.categoriaDebil = categoria;
        this.extraContraDebil = extra;
        return this;
    }

    public Habilidad quitandoNegativos() {
        this.quitaNegativos = true;
        return this;
    }

    // ---- Para que los enemigos decidan ----

    public boolean estaDisponible() {
        return turnosParaVolverAUsar == 0;
    }

    public void registrarUso() {
        if (enfriamiento > 0) iniciarEnfriamiento(enfriamiento);
    }

    protected void iniciarEnfriamiento(int turnos) {
        // +1 porque al terminar este mismo turno ya se descuenta uno
        turnosParaVolverAUsar = turnos + 1;
    }

    public void avanzarEnfriamiento() {
        if (turnosParaVolverAUsar > 0) turnosParaVolverAUsar--;
    }

    public boolean convieneUsar(Entidad usuario, Batalla batalla) {
        if (haceDanio()) return true;
        List<Entidad> candidatos;
        if (objetivo == Objetivo.SI_MISMO) candidatos = List.of(usuario);
        else if (objetivo.esSobreAliados()) candidatos = batalla.aliadosDe(usuario);
        else candidatos = batalla.enemigosDe(usuario);
        for (Entidad candidato : candidatos) {
            if (curacion > 0 && candidato.getPuntosDeSalud() < candidato.getPuntosDeSaludMaximos()) return true;
            if (!creadoresDeEfectos.isEmpty() && !candidato.tieneEfecto(creadoresDeEfectos.get(0).get().getNombre())) return true;
        }
        return false;
    }

    public List<String> ejecutar(Entidad usuario, List<Entidad> objetivos, Batalla batalla) {
        List<String> mensajes = new ArrayList<>();
        for (Entidad objetivo : objetivos) {
            if (haceDanio()) {
                if (!usuario.acierta() || objetivo.esquiva()) {
                    mensajes.add("  Falló contra " + objetivo.getNombre() + ".");
                    continue;
                }
                int danio = calcularDanio(usuario, objetivo);
                danio = usuario.modificarDanioInfligido(danio, objetivo);
                if (esFuerteContra(objetivo)) {
                    mensajes.add("  ¡Es muy efectivo!");
                }
                if (usuario.esCritico() && !objetivo.resisteCritico()) {
                    danio = danio * 3 / 2;
                    mensajes.add("  ¡Golpe crítico!");
                }
                danio = objetivo.recibirDanio(danio);
                mensajes.add("  " + objetivo.getNombre() + " recibe " + danio + " de daño.");
                if (roboDeVida > 0) {
                    int robado = usuario.curar(roboDeVida);
                    if (robado > 0) mensajes.add("  " + usuario.getNombre() + " absorbe " + robado + " de vida.");
                }
                if (!objetivo.estaVivo()) {
                    mensajes.add("  " + objetivo.getNombre() + " fue derrotado.");
                    continue;
                }
                if (retrasaTurno && batalla.retrasarTurno(objetivo)) {
                    mensajes.add("  " + objetivo.getNombre() + " queda rezagado en el orden de turnos.");
                }
            }
            if (curacion > 0) {
                int curado = objetivo.curar(curacion);
                mensajes.add("  " + objetivo.getNombre() + " recupera " + curado + " de vida.");
            }
            if (mana > 0) {
                objetivo.recuperarRecurso(mana);
                mensajes.add("  " + objetivo.getNombre() + " recupera " + mana + " de maná.");
            }
            if (quitaNegativos) {
                objetivo.quitarEfectosNegativos();
            }
            for (int i = 0; i < creadoresDeEfectos.size(); i++) {
                if (AZAR.nextInt(100) < probabilidades.get(i)) {
                    Efecto efecto = creadoresDeEfectos.get(i).get();
                    if (objetivo.aplicarEfecto(efecto)) {
                        mensajes.add("  " + objetivo.getNombre() + " recibe el efecto " + efecto.getNombre() + ".");
                    }
                }
            }
        }
        for (Supplier<Efecto> creador : creadoresDeEfectosPropios) {
            Efecto efecto = creador.get();
            if (usuario.aplicarEfecto(efecto)) {
                mensajes.add("  " + usuario.getNombre() + " recibe el efecto " + efecto.getNombre() + ".");
            }
        }
        return mensajes;
    }

    protected int calcularDanio(Entidad usuario, Entidad objetivo) {
        int danio = 0;
        if (danioFisico > 0) {
            danio += Math.max(0, usuario.getAtaqueFisico() + danioFisico - objetivo.getDefensaFisica());
        }
        if (danioMagico > 0) {
            danio += Math.max(0, usuario.getAtaqueMagico() + danioMagico - objetivo.getDefensaMagica());
        }
        if (variacion > 0) danio += AZAR.nextInt(variacion + 1);
        if (esFuerteContra(objetivo)) {
            danio = (danio + extraContraDebil) * porcentajeContraDebil / 100;
        }
        return Math.max(1, danio);
    }

    public boolean esFuerteContra(Entidad objetivo) {
        return categoriaDebil != null && objetivo.getCategoria() == categoriaDebil;
    }

    public boolean haceDanio() {
        return danioFisico > 0 || danioMagico > 0;
    }

    public String getNombre() { return nombre; }
    public int getCosto() { return costo; }
    public Objetivo getObjetivo() { return objetivo; }
    public int getDanioFisico() { return danioFisico; }
    public int getDanioMagico() { return danioMagico; }
    public int getMinimoObjetivos() { return minimoObjetivos; }
    public int getMaximoObjetivos() { return maximoObjetivos; }

    @Override
    public String toString() {
        return costo == 0 ? nombre : nombre + " (" + costo + " maná)";
    }
}

