package entidades;
import java.util.List;

public class Entidad {
    private String nombre;
    private String categoria;
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
    private List<Habilidad> habilidades = new java.util.ArrayList<>();
    private int criticoPorcentaje, evasionPorcentaje, regeneracionRecurso;

    /** Inicialización directa a partir del catálogo. No resuelve ataques. */
    protected Entidad(String nombre, String categoria, EstadisticasBase base) {
        this(nombre, categoria, base.getVidaMaxima(), base.getVidaMaxima(),
            base.getRecursoMaximo(), base.getRecursoMaximo(), base.getVelocidad(), base.getPrecision(),
            1, 0, base.getAtaqueFisico(), base.getAtaqueMagico(), base.getDefensaFisica(), base.getDefensaMagica());
        this.criticoPorcentaje=base.getCriticoPorcentaje();
        this.evasionPorcentaje=base.getEvasionPorcentaje();
        this.regeneracionRecurso=base.getRegeneracionRecurso();
    }
    protected void definirHabilidades(List<Habilidad> nuevasHabilidades) {
        this.habilidades = nuevasHabilidades;
    }
    public java.util.List<Habilidad> getHabilidades(){return habilidades;}
    public String getNombre(){return nombre;}
    public String getCategoria(){return categoria;}
    public int getPuntosDeSalud(){return puntosDeSalud;}
    public int getPuntosDeSaludMaximos(){return puntosDeSaludMaximos;}
    public int getRecurso(){return recurso;}
    public int getRecursoMaximo(){return recursoMaximo;}
    public int getVelocidad(){return velocidad;}
    public int getPrecision(){return precision;}
    public int getNivel(){return nivel;}
    public int getExperiencia(){return experiencia;}
    public int getAtaqueFisico(){return ataqueFisico;}
    public int getAtaqueMagico(){return ataqueMagico;}
    public int getDefensaFisica(){return defensaFisica;}
    public int getDefensaMagica(){return defensaMagica;}
    public int getCriticoPorcentaje(){return criticoPorcentaje;}
    public int getEvasionPorcentaje(){return evasionPorcentaje;}
    public int getRegeneracionRecurso(){return regeneracionRecurso;}

    public Entidad(String nombre, String categoria, int puntosDeSalud, int puntosDeSaludMaximos, int recurso, int recursoMaximo, int velocidad, int precision, int nivel, int experiencia, int ataqueFisico, int ataqueMagico, int defensaFisica, int defensaMagica) {
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

   public int atacar(Entidad entidadObjetivo) {
        entidadObjetivo.puntosDeSalud = entidadObjetivo.puntosDeSalud - this.ataqueFisico;
        return this.ataqueFisico;
    }

   public boolean estaVivo() {
        return this.puntosDeSalud > 0;
    }

   public boolean esMasVeloz(Entidad otraEntidad) {
        return this.velocidad > otraEntidad.velocidad;
    }

   public void mostrarEstado() {
        System.out.println("El " + this.nombre + " tiene " + this.puntosDeSalud + " puntos de salud.");
    }

    public String toString() {
        return this.nombre;
    }

}

