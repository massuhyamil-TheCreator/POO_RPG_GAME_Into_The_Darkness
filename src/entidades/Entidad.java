package entidades;
import entidades.efectos.Efecto;
import entidades.habilidades.Habilidad;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Entidad {
    private static final Random AZAR = new Random();
    public static final int PROBABILIDAD_CRITICO_BASE = 5;

    private String nombre;
    private Categoria categoria;
    private int puntosDeSaludMaximos;
    private int recurso;
    private int recursoMaximo;
    private int precision;
    private int nivel;
    private int experiencia;
    private int puntosDeSalud;
    private int velocidad;
    private int ataqueFisico;
    private int ataqueMagico;
    private int defensaFisica;
    private int defensaMagica;
    private Habilidad basico;
    private List<Habilidad> habilidades = new ArrayList<>();
    private List<Efecto> efectos = new ArrayList<>();
    private boolean enSuTurno;

    public Entidad(String nombre, Categoria categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.puntosDeSalud = puntosDeSalud;
        this.puntosDeSaludMaximos = puntosDeSaludMaximos;
        this.recurso = recurso;
        this.recursoMaximo = recursoMaximo;
        this.velocidad = velocidad;
        this.precision = precision;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.ataqueFisico = ataqueFisico;
        this.ataqueMagico = ataqueMagico;
        this.defensaFisica = defensaFisica;
        this.defensaMagica = defensaMagica;
    }

    // ---- Estadísticas: valor base + lo que modifiquen los efectos ----

    protected int getEstadisticaBase(Estadistica estadistica) {
        switch (estadistica) {
            case ATAQUE_FISICO: return ataqueFisico;
            case ATAQUE_MAGICO: return ataqueMagico;
            case DEFENSA_FISICA: return defensaFisica;
            case DEFENSA_MAGICA: return defensaMagica;
            case VELOCIDAD: return velocidad;
            case PRECISION: return precision;
            case CRITICO: return PROBABILIDAD_CRITICO_BASE;
            default: return 0;
        }
    }

    public int getEstadistica(Estadistica estadistica) {
        int valor = getEstadisticaBase(estadistica);
        for (Efecto efecto : efectos) {
            valor = efecto.modificar(estadistica, valor);
        }
        return Math.max(0, valor);
    }

    private boolean tirada(int probabilidad) {
        return AZAR.nextInt(100) < probabilidad;
    }

    public boolean acierta() {
        return tirada(getPrecision());
    }

    public boolean esquiva() {
        return tirada(getEstadistica(Estadistica.ESQUIVA));
    }

    public boolean esCritico() {
        return tirada(getEstadistica(Estadistica.CRITICO));
    }

    public boolean resisteCritico() {
        for (Efecto efecto : efectos) {
            if (efecto.anulaCritico()) return true;
        }
        return false;
    }

    // ---- Vida y recurso ----

    public int recibirDanio(int danio) {
        for (Efecto efecto : new ArrayList<>(efectos)) {
            danio = efecto.modificarDanioRecibido(danio);
        }
        efectos.removeIf(efecto -> efecto.termino());
        this.puntosDeSalud = Math.max(0, this.puntosDeSalud - danio);
        return danio;
    }

    public int curar(int cantidad) {
        for (Efecto efecto : efectos) {
            cantidad = efecto.modificarCuracionRecibida(cantidad);
        }
        int antes = this.puntosDeSalud;
        this.puntosDeSalud = Math.min(this.puntosDeSaludMaximos, this.puntosDeSalud + cantidad);
        return this.puntosDeSalud - antes;
    }

    public void desaparecer() {
        this.puntosDeSalud = 0;
    }

    // ---- Nivel ----

    protected void subirNivel(int vida, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica, int velocidad) {
        this.nivel++;
        this.puntosDeSaludMaximos += vida;
        this.puntosDeSalud += vida;
        this.ataqueFisico += ataqueFisico;
        this.ataqueMagico += ataqueMagico;
        this.defensaFisica += defensaFisica;
        this.defensaMagica += defensaMagica;
        this.velocidad += velocidad;
    }

    protected void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    // para cargar una partida guardada
    protected void setPuntosDeSalud(int puntosDeSalud) {
        this.puntosDeSalud = Math.max(0, Math.min(puntosDeSaludMaximos, puntosDeSalud));
    }

    protected void setRecurso(int recurso) {
        this.recurso = Math.max(0, Math.min(recursoMaximo, recurso));
    }

    public void recuperarRecurso(int cantidad) {
        this.recurso = Math.min(this.recursoMaximo, this.recurso + cantidad);
    }

    public boolean tieneRecurso(int cantidad) {
        return this.recurso >= cantidad;
    }

    public void gastarRecurso(int cantidad) {
        this.recurso = Math.max(0, this.recurso - cantidad);
    }

    public boolean ahorraCosto() {
        for (Efecto efecto : efectos) {
            if (efecto.ahorraCosto()) return true;
        }
        return false;
    }

    public int modificarDanioInfligido(int danio, Entidad objetivo) {
        for (Efecto efecto : efectos) {
            danio = efecto.modificarDanioInfligido(danio, objetivo);
        }
        return danio;
    }

    public boolean puedeAtacarA(Entidad objetivo) {
        for (Efecto efecto : efectos) {
            if (!efecto.permiteAtacarA(objetivo)) return false;
        }
        return true;
    }

    public boolean estaDescontrolado() {
        for (Efecto efecto : efectos) {
            if (efecto.descontrola()) return true;
        }
        return false;
    }

    public boolean actuaPrimero() {
        return false;
    }

    public boolean esJefe() {
        return false;
    }

    public Entidad getObjetivoForzado() {
        for (Efecto efecto : efectos) {
            Entidad forzado = efecto.objetivoForzado();
            if (forzado != null && forzado.estaVivo()) return forzado;
        }
        return null;
    }

    public boolean estaVivo() {
        return this.puntosDeSalud > 0;
    }

    // ---- Habilidades ----

    protected void setBasico(Habilidad basico) {
        this.basico = basico;
    }

    protected void agregarHabilidad(Habilidad habilidad) {
        habilidades.add(habilidad);
    }

    public boolean conoce(Habilidad habilidad) {
        return habilidad == basico || habilidades.contains(habilidad);
    }

    // ---- Efectos ----

    public boolean aplicarEfecto(Efecto nuevo) {
        for (Efecto efecto : efectos) {
            if (efecto.bloquea(nuevo)) return false;
        }
        if (tieneEfecto(nuevo.getNombre())) return false;
        nuevo.setPortador(this);
        if (enSuTurno) nuevo.marcarComoRecienAplicado();
        efectos.add(nuevo);
        return true;
    }

    public void quitarEfecto(String nombreEfecto) {
        efectos.removeIf(efecto -> efecto.getNombre().equals(nombreEfecto));
    }

    public void quitarEfectosNegativos() {
        efectos.removeIf(efecto -> efecto.esNegativo());
    }

    public boolean tieneEfecto(String nombreEfecto) {
        for (Efecto efecto : efectos) {
            if (efecto.getNombre().equals(nombreEfecto)) return true;
        }
        return false;
    }

    public List<String> iniciarTurno() {
        enSuTurno = true;
        List<String> mensajes = new ArrayList<>();
        for (Efecto efecto : new ArrayList<>(efectos)) {
            String mensaje = efecto.alInicioDelTurno(this);
            if (mensaje != null) mensajes.add(mensaje);
        }
        efectos.removeIf(efecto -> efecto.termino());
        return mensajes;
    }

    public void finalizarTurno() {
        enSuTurno = false;
        for (Habilidad habilidad : habilidades) {
            habilidad.avanzarEnfriamiento();
        }
        for (Efecto efecto : efectos) {
            efecto.pasarTurno();
        }
        efectos.removeIf(efecto -> efecto.termino());
    }

    public void limpiarEfectos() {
        efectos.removeIf(efecto -> !efecto.esPasiva());
    }

    public boolean puedeActuar() {
        for (Efecto efecto : efectos) {
            if (!efecto.puedeActuar()) return false;
        }
        return true;
    }

    public boolean puedeUsarItems() {
        for (Efecto efecto : efectos) {
            if (!efecto.puedeUsarItems()) return false;
        }
        return true;
    }

    public String getNombre() { return nombre; }
    public Categoria getCategoria() { return categoria; }
    public int getPuntosDeSalud() { return puntosDeSalud; }
    public int getPuntosDeSaludMaximos() { return puntosDeSaludMaximos; }
    public int getRecurso() { return recurso; }
    public int getRecursoMaximo() { return recursoMaximo; }
    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public int getVelocidad() { return getEstadistica(Estadistica.VELOCIDAD); }
    public int getPrecision() { return getEstadistica(Estadistica.PRECISION); }
    public int getAtaqueFisico() { return getEstadistica(Estadistica.ATAQUE_FISICO); }
    public int getAtaqueMagico() { return getEstadistica(Estadistica.ATAQUE_MAGICO); }
    public int getDefensaFisica() { return getEstadistica(Estadistica.DEFENSA_FISICA); }
    public int getDefensaMagica() { return getEstadistica(Estadistica.DEFENSA_MAGICA); }
    public Habilidad getBasico() { return basico; }
    public List<Habilidad> getHabilidades() { return habilidades; }
    public List<Efecto> getEfectos() { return efectos; }

    @Override
    public String toString() {
        return this.nombre;
    }
}
